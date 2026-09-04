package dev.detent.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private static final int PICK_FILE = 41;
    private static final String PREFS = "detent_mobile";
    private static final int BG = Color.rgb(10, 11, 13);
    private static final int SURFACE = Color.rgb(18, 20, 24);
    private static final int SURFACE_2 = Color.rgb(24, 27, 31);
    private static final int TEXT = Color.rgb(232, 235, 239);
    private static final int MUTED = Color.rgb(142, 149, 159);
    private static final int ACCENT = Color.rgb(111, 235, 174);
    private static final int WARN = Color.rgb(242, 194, 105);
    private static final int BAD = Color.rgb(242, 112, 112);

    private SharedPreferences prefs;
    private LinearLayout header;
    private LinearLayout bottomBar;
    private LinearLayout messages;
    private ScrollView transcriptScroll;
    private TextView status;
    private TextView meta;
    private TextView footer;
    private TextView stopChip;
    private TextView streamingView;
    private EditText composer;

    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final ArrayDeque<String> queue = new ArrayDeque<>();
    private volatile HttpURLConnection activeConnection;
    private volatile boolean active;
    private volatile long requestGeneration;

    private JSONArray history = new JSONArray();
    private String session = "default";
    private String attachmentName = "";
    private String attachmentText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        session = prefs.getString("session.current", "default");
        loadHistory();
        configureWindow();
        View root = buildUi();
        setContentView(root);
        render();

        if (prefs.getString("api.key", "").trim().isEmpty()) {
            appendSystem("Provider setup required. Tap Model or run /auth.");
            root.postDelayed(this::showProviderSettings, 350);
        }
    }

    private void configureWindow() {
        Window w = getWindow();
        w.setStatusBarColor(BG);
        w.setNavigationBarColor(BG);
        w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private View buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(dp(16), dp(10), dp(16), dp(10));
        header.setBackgroundColor(BG);

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        top.setGravity(Gravity.CENTER_VERTICAL);

        TextView logo = mono("DETENT", ACCENT, 15, true);
        top.addView(logo, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        status = chip("ready", false);
        top.addView(status);

        TextView settings = chip("CFG", false);
        LinearLayout.LayoutParams settingsLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        settingsLp.setMargins(dp(8), 0, 0, 0);
        settings.setOnClickListener(v -> showProviderSettings());
        top.addView(settings, settingsLp);
        header.addView(top);

        meta = mono("", MUTED, 11, false);
        meta.setSingleLine(true);
        meta.setEllipsize(TextUtils.TruncateAt.END);
        meta.setPadding(0, dp(5), 0, 0);
        header.addView(meta);
        root.addView(header);

        View divider = new View(this);
        divider.setBackgroundColor(Color.rgb(31, 34, 39));
        root.addView(divider, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(1)));

        transcriptScroll = new ScrollView(this);
        transcriptScroll.setFillViewport(true);
        transcriptScroll.setClipToPadding(false);
        messages = new LinearLayout(this);
        messages.setOrientation(LinearLayout.VERTICAL);
        messages.setPadding(dp(16), dp(14), dp(16), dp(22));
        transcriptScroll.addView(messages, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(transcriptScroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        bottomBar = new LinearLayout(this);
        bottomBar.setOrientation(LinearLayout.VERTICAL);
        bottomBar.setPadding(dp(12), dp(8), dp(12), dp(10));
        bottomBar.setBackgroundColor(BG);

        HorizontalScrollView actionScroll = new HorizontalScrollView(this);
        actionScroll.setHorizontalScrollBarEnabled(false);
        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setGravity(Gravity.CENTER_VERTICAL);
        actions.addView(actionChip("Model", v -> showProviderSettings()));
        actions.addView(actionChip("Thinking", v -> showThinkingPicker()));
        actions.addView(actionChip("File", v -> pickFile()));
        actions.addView(actionChip("Commands", v -> showCommandPalette()));
        stopChip = actionChip("Stop", v -> stopCurrent());
        stopChip.setVisibility(View.GONE);
        actions.addView(stopChip);
        actionScroll.addView(actions);
        bottomBar.addView(actionScroll);

        LinearLayout composeRow = new LinearLayout(this);
        composeRow.setOrientation(LinearLayout.HORIZONTAL);
        composeRow.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams composeRowLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        composeRowLp.setMargins(0, dp(8), 0, 0);

        composer = new EditText(this);
        composer.setTextColor(TEXT);
        composer.setHintTextColor(MUTED);
        composer.setHint("Ask Detent…");
        composer.setTextSize(15);
        composer.setTypeface(Typeface.MONOSPACE);
        composer.setSingleLine(false);
        composer.setMinLines(1);
        composer.setMaxLines(5);
        composer.setPadding(dp(13), dp(10), dp(13), dp(10));
        composer.setBackground(rounded(SURFACE, dp(14), Color.rgb(40, 44, 50), dp(1)));
        composer.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && event.isCtrlPressed()) {
                submitComposer();
                return true;
            }
            return false;
        });
        composeRow.addView(composer, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView send = mono("↑", BG, 22, true);
        send.setGravity(Gravity.CENTER);
        send.setBackground(rounded(ACCENT, dp(14), ACCENT, 0));
        send.setOnClickListener(v -> submitComposer());
        LinearLayout.LayoutParams sendLp = new LinearLayout.LayoutParams(dp(48), dp(48));
        sendLp.setMargins(dp(8), 0, 0, 0);
        composeRow.addView(send, sendLp);
        bottomBar.addView(composeRow, composeRowLp);

        footer = mono("", MUTED, 10, false);
        footer.setPadding(dp(2), dp(6), dp(2), 0);
        bottomBar.addView(footer);
        root.addView(bottomBar);

        root.setOnApplyWindowInsetsListener((v, insets) -> {
            int topInset;
            int bottomInset;
            if (Build.VERSION.SDK_INT >= 30) {
                Insets sys = insets.getInsets(WindowInsets.Type.systemBars());
                Insets ime = insets.getInsets(WindowInsets.Type.ime());
                topInset = sys.top;
                bottomInset = Math.max(sys.bottom, ime.bottom);
            } else {
                topInset = insets.getSystemWindowInsetTop();
                bottomInset = insets.getSystemWindowInsetBottom();
            }
            header.setPadding(dp(16), topInset + dp(9), dp(16), dp(10));
            bottomBar.setPadding(dp(12), dp(8), dp(12), bottomInset + dp(8));
            return insets;
        });
        root.requestApplyInsets();
        return root;
    }

    private TextView actionChip(String label, View.OnClickListener click) {
        TextView v = chip(label, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(36));
        lp.setMargins(0, 0, dp(8), 0);
        v.setLayoutParams(lp);
        v.setOnClickListener(click);
        return v;
    }

    private TextView chip(String label, boolean activeChip) {
        TextView v = mono(label, activeChip ? BG : TEXT, 11, true);
        v.setGravity(Gravity.CENTER);
        v.setPadding(dp(12), 0, dp(12), 0);
        v.setMinHeight(dp(32));
        v.setBackground(rounded(activeChip ? ACCENT : SURFACE_2, dp(11), Color.rgb(48, 52, 59), dp(1)));
        return v;
    }

    private TextView mono(String value, int color, int sizeSp, boolean bold) {
        TextView v = new TextView(this);
        v.setText(value);
        v.setTextColor(color);
        v.setTextSize(sizeSp);
        v.setTypeface(Typeface.MONOSPACE, bold ? Typeface.BOLD : Typeface.NORMAL);
        return v;
    }

    private GradientDrawable rounded(int fill, int radius, int stroke, int strokeWidth) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(fill);
        d.setCornerRadius(radius);
        if (strokeWidth > 0) d.setStroke(strokeWidth, stroke);
        return d;
    }

    private void submitComposer() {
        String text = composer.getText().toString().trim();
        if (text.isEmpty()) return;
        composer.setText("");
        if (text.startsWith("/")) {
            handleCommand(text);
            return;
        }
        if (active) {
            if (queue.size() >= 8) {
                appendSystem("Queue is full (8). Stop or wait for the current request.");
                return;
            }
            queue.add(text);
            renderStatus();
            renderFooter();
            return;
        }
        runPrompt(text);
    }

    private void runPrompt(String prompt) {
        String key = prefs.getString("api.key", "").trim();
        if (key.isEmpty()) {
            composer.setText(prompt);
            showProviderSettings();
            return;
        }
        appendHistory("user", prompt);
        active = true;
        long requestId = ++requestGeneration;
        render();
        io.execute(() -> executeRequest(requestId));
    }

    private void executeRequest(long requestId) {
        String endpoint = prefs.getString("api.endpoint", defaultEndpointFor(prefs.getString("provider", "OpenRouter"))).trim();
        String model = prefs.getString("model", "openai/gpt-5").trim();
        String key = prefs.getString("api.key", "").trim();
        String effort = ProviderConfig.normalizeEffort(prefs.getString("effort", "medium"));
        StringBuilder streamed = new StringBuilder();
        try {
            URL url = new URL(endpoint);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            activeConnection = c;
            c.setRequestMethod("POST");
            c.setConnectTimeout(30000);
            c.setReadTimeout(180000);
            c.setDoOutput(true);
            c.setRequestProperty("Content-Type", "application/json");
            c.setRequestProperty("Accept", "text/event-stream");
            c.setRequestProperty("Authorization", "Bearer " + key);
            if (endpoint.contains("openrouter.ai")) {
                c.setRequestProperty("X-Title", "Detent Mobile");
                c.setRequestProperty("HTTP-Referer", "https://github.com/Elric412/Detent-Legacy");
            }

            JSONObject body = new JSONObject();
            body.put("model", model);
            body.put("stream", true);
            JSONArray providerMessages = new JSONArray();
            providerMessages.put(new JSONObject().put("role", "system").put("content", systemPrompt()));
            synchronized (this) {
                for (int i = 0; i < history.length(); i++) {
                    JSONObject m = history.optJSONObject(i);
                    if (m == null) continue;
                    String role = m.optString("role", "");
                    if (!ProviderHistory.isSendableRole(role)) continue;
                    providerMessages.put(new JSONObject().put("role", role).put("content", m.optString("content", "")));
                }
            }
            body.put("messages", providerMessages);
            if (endpoint.contains("openrouter.ai")) {
                body.put("reasoning", new JSONObject().put("effort", effort));
            } else if (model.contains("gpt-5") || model.matches(".*\\b(o1|o3|o4).*")) {
                body.put("reasoning_effort", effort);
            }

            byte[] bytes = body.toString().getBytes(StandardCharsets.UTF_8);
            try (OutputStream out = c.getOutputStream()) {
                out.write(bytes);
            }

            int code = c.getResponseCode();
            InputStream stream = code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream();
            if (code < 200 || code >= 300) {
                throw new RuntimeException("HTTP " + code + ": " + compactError(readAll(stream)));
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String line;
            boolean sawSse = false;
            while ((line = reader.readLine()) != null) {
                if (requestId != requestGeneration) return;
                if (!line.startsWith("data:")) continue;
                sawSse = true;
                String data = line.substring(5).trim();
                if (data.equals("[DONE]")) break;
                if (data.isEmpty()) continue;
                JSONObject event = new JSONObject(data);
                JSONArray choices = event.optJSONArray("choices");
                if (choices == null || choices.length() == 0) continue;
                JSONObject delta = choices.optJSONObject(0) == null ? null : choices.optJSONObject(0).optJSONObject("delta");
                if (delta == null) continue;
                String chunk = delta.optString("content", "");
                if (chunk.isEmpty()) continue;
                streamed.append(chunk);
                String snapshot = streamed.toString();
                runOnUiThread(() -> updateStreaming(snapshot));
            }

            if (!sawSse || streamed.length() == 0) {
                if (streamed.length() == 0) streamed.append("(provider returned no text)");
            }
            String finalText = streamed.toString();
            if (requestId != requestGeneration) return;
            appendHistory("assistant", finalText);
            runOnUiThread(() -> finishRequest(requestId, null));
        } catch (Exception e) {
            if (requestId != requestGeneration) return;
            String msg = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            runOnUiThread(() -> finishRequest(requestId, msg));
        } finally {
            activeConnection = null;
        }
    }

    private void updateStreaming(String text) {
        if (streamingView != null) {
            streamingView.setText(text);
            transcriptScroll.post(() -> transcriptScroll.fullScroll(View.FOCUS_DOWN));
        }
    }

    private String systemPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("You are Detent running in its Android mobile client. Be concise and engineering-focused. ");
        sb.append("Never claim desktop-only actions ran. Android capability limits include arbitrary shell/PTY, Docker, system package managers, systemd, unrestricted filesystem access, executable local plugins, local stdio MCP, and external LSP processes. ");
        sb.append("You may reason about code, inspect attached text/code, produce patches, plan work, and use provider-backed reasoning. ");
        if (!attachmentText.isEmpty()) {
            sb.append("Attached file: ").append(attachmentName).append("\n--- ATTACHMENT ---\n");
            sb.append(attachmentText).append("\n--- END ATTACHMENT ---");
        }
        return sb.toString();
    }

    private void finishRequest(long requestId, String error) {
        if (requestId != requestGeneration) return;
        active = false;
        if (error != null && !error.isEmpty()) appendSystem("Request failed: " + error);
        render();
        String next = queue.poll();
        if (next != null) runPrompt(next);
    }

    private void stopCurrent() {
        if (!active && queue.isEmpty()) return;
        requestGeneration++;
        queue.clear();
        HttpURLConnection c = activeConnection;
        if (c != null) c.disconnect();
        active = false;
        appendSystem("Stopped. Queue cleared.");
        render();
    }

    private synchronized void appendHistory(String role, String content) {
        try {
            JSONObject item = new JSONObject();
            item.put("role", role);
            item.put("content", content);
            history.put(item);
            saveHistory();
        } catch (Exception ignored) { }
    }

    private void appendSystem(String content) {
        appendHistory("system-note", content);
        runOnUiThread(this::render);
    }

    private void render() {
        renderStatus();
        messages.removeAllViews();
        streamingView = null;

        if (history.length() == 0) {
            TextView title = mono("Detent for Android", TEXT, 17, true);
            messages.addView(title);
            TextView sub = mono("Mobile-first harness client. Configure a provider, attach code, or type / for commands.", MUTED, 12, false);
            sub.setPadding(0, dp(6), 0, dp(18));
            messages.addView(sub);
        }

        for (int i = 0; i < history.length(); i++) {
            JSONObject m = history.optJSONObject(i);
            if (m == null) continue;
            addMessage(m.optString("role", ""), m.optString("content", ""));
        }
        if (active) {
            streamingView = addMessage("assistant-stream", "Thinking…");
        }
        renderFooter();
        transcriptScroll.post(() -> transcriptScroll.fullScroll(View.FOCUS_DOWN));
    }

    private TextView addMessage(String role, String content) {
        LinearLayout block = new LinearLayout(this);
        block.setOrientation(LinearLayout.VERTICAL);
        int top = role.equals("system-note") ? dp(5) : dp(9);
        int bottom = role.equals("system-note") ? dp(5) : dp(9);
        block.setPadding(dp(10), top, dp(10), bottom);

        boolean system = role.equals("system-note");
        boolean user = role.equals("user");
        String label = user ? "YOU" : system ? "SYSTEM" : "DETENT";
        int labelColor = user ? WARN : system ? MUTED : ACCENT;
        TextView labelView = mono(label, labelColor, 10, true);
        block.addView(labelView);

        TextView body = mono(content, system ? Color.rgb(190, 195, 202) : TEXT, system ? 12 : 14, false);
        body.setTextIsSelectable(true);
        body.setLineSpacing(0, 1.12f);
        body.setPadding(0, dp(4), 0, 0);
        block.addView(body);

        if (system) {
            block.setBackground(rounded(SURFACE, dp(10), Color.rgb(37, 40, 46), dp(1)));
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, dp(system ? 8 : 12));
        messages.addView(block, lp);
        return body;
    }

    private void renderStatus() {
        String model = prefs.getString("model", "openai/gpt-5");
        String effort = ProviderConfig.normalizeEffort(prefs.getString("effort", "medium"));
        status.setText(active ? "running" : "ready");
        status.setTextColor(active ? BG : TEXT);
        status.setBackground(rounded(active ? ACCENT : SURFACE_2, dp(11), Color.rgb(48, 52, 59), dp(1)));
        meta.setText(session + "  ·  " + model + "  ·  " + effort);
        stopChip.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    private void renderFooter() {
        String file = attachmentName.isEmpty() ? "no file" : attachmentName;
        String q = queue.isEmpty() ? "" : "  ·  " + queue.size() + " queued";
        footer.setText(file + q + "  ·  Ctrl+Enter send");
    }

    private void handleCommand(String raw) {
        CommandRouter.Command command = CommandRouter.parse(raw);
        switch (command.action) {
            case HELP:
                showCommandPalette();
                break;
            case OPEN_PROVIDER_SETTINGS:
                showProviderSettings();
                break;
            case SET_MODEL:
                prefs.edit().putString("model", command.argument).apply();
                appendSystem("Model → " + command.argument);
                break;
            case SET_THINKING:
                if (command.argument.isEmpty()) showThinkingPicker();
                else {
                    String normalized = ProviderConfig.normalizeEffort(command.argument);
                    prefs.edit().putString("effort", normalized).apply();
                    appendSystem("Reasoning → " + normalized);
                }
                break;
            case CONTEXT:
                appendSystem("Session: " + session + "\nModel: " + prefs.getString("model", "openai/gpt-5") + "\nAttachment: " + (attachmentName.isEmpty() ? "none" : attachmentName) + "\nHistory: " + history.length() + " messages\nQueue: " + queue.size());
                break;
            case TASKS:
                appendSystem("Request: " + (active ? "running" : "idle") + "\nQueued prompts: " + queue.size() + "\nMobile worker concurrency: 1");
                break;
            case PERMISSIONS:
            case CAGE:
                showCapabilities();
                break;
            case SESSIONS:
                showSessionPicker();
                break;
            case SWITCH_SESSION:
                if (command.argument.isEmpty()) showSessionPicker();
                else switchSession(SessionNames.safe(command.argument));
                break;
            case NEW_SESSION:
                switchSession(command.argument.isEmpty() ? "session-" + System.currentTimeMillis() : SessionNames.safe(command.argument));
                synchronized (this) { history = new JSONArray(); saveHistory(); }
                render();
                break;
            case CLEAR:
                synchronized (this) { history = new JSONArray(); saveHistory(); }
                render();
                break;
            case PICK_FILE:
                pickFile();
                break;
            case STOP:
                stopCurrent();
                break;
            case UNKNOWN:
            default:
                appendSystem(command.message);
                break;
        }
    }

    private void showThinkingPicker() {
        String[] levels = {"low", "medium", "high"};
        String current = ProviderConfig.normalizeEffort(prefs.getString("effort", "medium"));
        int checked = current.equals("low") ? 0 : current.equals("high") ? 2 : 1;
        new AlertDialog.Builder(this)
                .setTitle("Reasoning effort")
                .setSingleChoiceItems(levels, checked, (d, which) -> {
                    prefs.edit().putString("effort", levels[which]).apply();
                    d.dismiss();
                    renderStatus();
                })
                .show();
    }

    private void showProviderSettings() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(18), dp(8), dp(18), 0);

        Spinner provider = new Spinner(this);
        String[] providers = {"OpenRouter", "OpenAI", "Custom"};
        provider.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, providers));
        String savedProvider = prefs.getString("provider", "OpenRouter");
        provider.setSelection(savedProvider.equals("OpenAI") ? 1 : savedProvider.equals("Custom") ? 2 : 0);

        EditText endpoint = field("HTTPS endpoint", prefs.getString("api.endpoint", defaultEndpointFor(savedProvider)), false);
        EditText key = field("API key", prefs.getString("api.key", ""), true);
        EditText model = field("Model", prefs.getString("model", "openai/gpt-5"), false);
        box.addView(label("Provider"));
        box.addView(provider);
        box.addView(label("Endpoint"));
        box.addView(endpoint);
        box.addView(label("API key"));
        box.addView(key);
        box.addView(label("Model"));
        box.addView(model);

        provider.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            boolean first = true;
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String choice = providers[position];
                if (!first && !choice.equals("Custom")) endpoint.setText(defaultEndpointFor(choice));
                first = false;
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Provider & model")
                .setView(box)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();
        dialog.setOnShowListener(x -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String p = providers[provider.getSelectedItemPosition()];
            ProviderConfig.Validation valid = ProviderConfig.validate(endpoint.getText().toString(), key.getText().toString(), model.getText().toString(), prefs.getString("effort", "medium"));
            if (!valid.valid) {
                Toast.makeText(this, valid.message, Toast.LENGTH_LONG).show();
                return;
            }
            prefs.edit()
                    .putString("provider", p)
                    .putString("api.endpoint", valid.config.endpoint)
                    .putString("api.key", valid.config.apiKey)
                    .putString("model", valid.config.model)
                    .putString("effort", valid.config.effort)
                    .apply();
            dialog.dismiss();
            renderStatus();
            appendSystem("Provider configured: " + p + " · " + valid.config.model);
        }));
        dialog.show();
    }

    private String defaultEndpointFor(String provider) {
        if ("OpenAI".equals(provider)) return "https://api.openai.com/v1/chat/completions";
        return "https://openrouter.ai/api/v1/chat/completions";
    }

    private TextView label(String text) {
        TextView v = mono(text, MUTED, 11, true);
        v.setPadding(0, dp(10), 0, dp(3));
        return v;
    }

    private EditText field(String hintText, String value, boolean secret) {
        EditText e = new EditText(this);
        e.setHint(hintText);
        e.setText(value);
        e.setSingleLine(true);
        if (secret) e.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        return e;
    }

    private void showCommandPalette() {
        String[] rows = {
                "Provider / auth   · /auth",
                "Model             · /model <id>",
                "Reasoning         · /thinking",
                "Context           · /context",
                "Tasks             · /tasks",
                "Capabilities      · /cage",
                "Sessions          · /sessions",
                "New session       · /new",
                "Attach file       · /file",
                "Clear transcript  · /clear",
                "Stop              · /stop"
        };
        String[] commands = {"/auth", "/model", "/thinking", "/context", "/tasks", "/cage", "/sessions", "/new", "/file", "/clear", "/stop"};
        new AlertDialog.Builder(this)
                .setTitle("Commands")
                .setItems(rows, (d, i) -> handleCommand(commands[i]))
                .show();
    }

    private void showCapabilities() {
        new AlertDialog.Builder(this)
                .setTitle("CAGE · Android")
                .setMessage("Available\n• HTTPS provider access\n• user-selected document read\n• app-local sessions\n• attachments and context\n\nUnavailable on a normal phone\n• arbitrary shell / PTY\n• unrestricted filesystem\n• Docker / package managers / systemd\n• executable local plugins / stdio MCP / external LSP\n\nUnsupported actions fail explicitly instead of pretending to run.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showSessionPicker() {
        ArrayList<String> names = new ArrayList<>(prefs.getStringSet("sessions", Collections.singleton("default")));
        Collections.sort(names);
        new AlertDialog.Builder(this)
                .setTitle("Sessions")
                .setItems(names.toArray(new String[0]), (d, i) -> switchSession(names.get(i)))
                .setPositiveButton("New", (d, w) -> handleCommand("/new"))
                .show();
    }

    private void switchSession(String name) {
        session = SessionNames.safe(name);
        prefs.edit().putString("session.current", session).apply();
        Set<String> existing = new HashSet<>(prefs.getStringSet("sessions", new HashSet<>()));
        existing.add(session);
        prefs.edit().putStringSet("sessions", existing).apply();
        attachmentName = "";
        attachmentText = "";
        loadHistory();
        render();
    }

    private synchronized void loadHistory() {
        try { history = new JSONArray(prefs.getString("history." + session, "[]")); }
        catch (Exception e) { history = new JSONArray(); }
        Set<String> existing = new HashSet<>(prefs.getStringSet("sessions", new HashSet<>()));
        existing.add(session);
        prefs.edit().putStringSet("sessions", existing).apply();
    }

    private synchronized void saveHistory() {
        prefs.edit().putString("history." + session, history.toString()).apply();
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != PICK_FILE || resultCode != RESULT_OK || data == null || data.getData() == null) return;
        Uri uri = data.getData();
        try {
            getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } catch (Exception ignored) { }
        io.execute(() -> {
            try {
                String name = displayName(uri);
                String text;
                try (InputStream in = getContentResolver().openInputStream(uri)) {
                    text = readLimited(in, 240_000);
                }
                String n = name;
                String t = text;
                runOnUiThread(() -> {
                    attachmentName = n;
                    attachmentText = t;
                    appendSystem("Attached " + n + " · " + t.length() + " chars");
                    renderFooter();
                });
            } catch (Exception e) {
                runOnUiThread(() -> appendSystem("File error: " + e.getMessage()));
            }
        });
    }

    private String displayName(Uri uri) {
        String result = "attachment.txt";
        try (Cursor cursor = getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) result = cursor.getString(0);
        } catch (Exception ignored) { }
        return result;
    }

    private String readAll(InputStream in) throws Exception {
        if (in == null) return "";
        BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) sb.append(line).append('\n');
        return sb.toString();
    }

    private String readLimited(InputStream in, int maxChars) throws Exception {
        if (in == null) return "";
        BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        char[] buf = new char[4096];
        StringBuilder sb = new StringBuilder();
        int n;
        while ((n = r.read(buf)) > 0 && sb.length() < maxChars) {
            int remaining = maxChars - sb.length();
            sb.append(buf, 0, Math.min(n, remaining));
        }
        return sb.toString();
    }

    private String compactError(String raw) {
        try {
            JSONObject o = new JSONObject(raw);
            Object e = o.opt("error");
            if (e instanceof JSONObject) return ((JSONObject) e).optString("message", raw);
            if (e != null) return String.valueOf(e);
        } catch (Exception ignored) { }
        return raw == null ? "Unknown provider error" : (raw.length() > 500 ? raw.substring(0, 500) : raw);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onDestroy() {
        requestGeneration++;
        HttpURLConnection c = activeConnection;
        if (c != null) c.disconnect();
        io.shutdownNow();
        super.onDestroy();
    }
}
