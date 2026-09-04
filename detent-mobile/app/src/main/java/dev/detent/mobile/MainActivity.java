package dev.detent.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.database.Cursor;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private static final int PICK_FILE = 41;
    private static final String PREFS = "detent_mobile";
    private static final int BG = Color.rgb(11, 12, 14);
    private static final int SURFACE = Color.rgb(18, 20, 23);
    private static final int TEXT = Color.rgb(226, 229, 234);
    private static final int MUTED = Color.rgb(139, 145, 154);
    private static final int ACCENT = Color.rgb(116, 233, 174);
    private static final int WARN = Color.rgb(240, 190, 95);
    private static final int BAD = Color.rgb(242, 112, 112);

    private SharedPreferences prefs;
    private TextView status;
    private TextView transcript;
    private TextView hint;
    private EditText composer;
    private Button send;
    private Button stop;
    private ScrollView transcriptScroll;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final ArrayDeque<String> queue = new ArrayDeque<>();
    private volatile HttpURLConnection activeConnection;
    private volatile boolean active;
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
        setContentView(buildUi());
        render();
        if (prefs.getString("api.key", "").isEmpty()) {
            appendSystem("No provider key configured. Tap CONFIG or run /model to set one.");
        }
    }

    private void configureWindow() {
        Window w = getWindow();
        w.setStatusBarColor(BG);
        w.setNavigationBarColor(BG);
    }

    private View buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);
        root.setPadding(dp(10), dp(8), dp(10), dp(8));

        HorizontalScrollView headerScroll = new HorizontalScrollView(this);
        headerScroll.setHorizontalScrollBarEnabled(false);
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(0, 0, 0, dp(6));
        TextView logo = mono("DETENT", ACCENT, 13);
        logo.setPadding(0, 0, dp(10), 0);
        header.addView(logo);
        status = mono("", MUTED, 11);
        header.addView(status);
        headerScroll.addView(header);
        root.addView(headerScroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        transcriptScroll = new ScrollView(this);
        transcriptScroll.setFillViewport(true);
        transcript = mono("", TEXT, 13);
        transcript.setTextIsSelectable(true);
        transcript.setLineSpacing(0, 1.12f);
        transcript.setPadding(0, dp(8), 0, dp(12));
        transcriptScroll.addView(transcript, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        root.addView(transcriptScroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        actions.setGravity(Gravity.CENTER_VERTICAL);
        Button config = compactButton("CONFIG");
        config.setOnClickListener(v -> showSettings());
        Button file = compactButton("FILE");
        file.setOnClickListener(v -> pickFile());
        Button slash = compactButton("/");
        slash.setOnClickListener(v -> showCommandPalette());
        stop = compactButton("STOP");
        stop.setEnabled(false);
        stop.setOnClickListener(v -> stopCurrent());
        actions.addView(config);
        actions.addView(file);
        actions.addView(slash);
        actions.addView(stop);
        root.addView(actions);

        LinearLayout composeRow = new LinearLayout(this);
        composeRow.setOrientation(LinearLayout.HORIZONTAL);
        composeRow.setGravity(Gravity.BOTTOM);
        composer = new EditText(this);
        composer.setTextColor(TEXT);
        composer.setHintTextColor(MUTED);
        composer.setHint("Ask Detent…  /help for commands");
        composer.setTextSize(14);
        composer.setSingleLine(false);
        composer.setMinLines(1);
        composer.setMaxLines(5);
        composer.setBackgroundColor(SURFACE);
        composer.setPadding(dp(10), dp(8), dp(10), dp(8));
        composer.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && event.isCtrlPressed()) {
                submitComposer();
                return true;
            }
            return false;
        });
        composeRow.addView(composer, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        send = compactButton("SEND");
        send.setOnClickListener(v -> submitComposer());
        LinearLayout.LayoutParams sendLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        sendLp.setMargins(dp(6), 0, 0, 0);
        composeRow.addView(send, sendLp);
        root.addView(composeRow);

        hint = mono("", MUTED, 10);
        hint.setPadding(0, dp(5), 0, 0);
        root.addView(hint);
        return root;
    }

    private Button compactButton(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(TEXT);
        b.setTextSize(10);
        b.setAllCaps(false);
        b.setMinHeight(0);
        b.setMinWidth(0);
        b.setPadding(dp(10), dp(4), dp(10), dp(4));
        return b;
    }

    private TextView mono(String value, int color, int sizeSp) {
        TextView v = new TextView(this);
        v.setText(value);
        v.setTextColor(color);
        v.setTextSize(sizeSp);
        v.setTypeface(android.graphics.Typeface.MONOSPACE);
        return v;
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
            queue.add(text);
            appendSystem("queued +1  ·  " + queue.size() + " waiting");
            renderStatus();
        } else {
            runPrompt(text);
        }
    }

    private void runPrompt(String prompt) {
        String key = prefs.getString("api.key", "").trim();
        if (key.isEmpty()) {
            composer.setText(prompt);
            showSettings();
            return;
        }
        appendHistory("user", prompt);
        active = true;
        render();
        send.setEnabled(true);
        stop.setEnabled(true);
        io.execute(() -> executeRequest(prompt));
    }

    private void executeRequest(String prompt) {
        String endpoint = prefs.getString("api.endpoint", "https://openrouter.ai/api/v1/chat/completions").trim();
        String model = prefs.getString("model", "openai/gpt-5").trim();
        String key = prefs.getString("api.key", "").trim();
        String effort = prefs.getString("effort", "medium");
        try {
            URL url = new URL(endpoint);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            activeConnection = c;
            c.setRequestMethod("POST");
            c.setConnectTimeout(30000);
            c.setReadTimeout(180000);
            c.setDoOutput(true);
            c.setRequestProperty("Content-Type", "application/json");
            c.setRequestProperty("Authorization", "Bearer " + key);
            if (endpoint.contains("openrouter.ai")) {
                c.setRequestProperty("X-Title", "Detent Mobile");
                c.setRequestProperty("HTTP-Referer", "https://github.com/Elric412/Detent-Legacy");
            }

            JSONObject body = new JSONObject();
            body.put("model", model);
            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system").put("content", systemPrompt()));
            synchronized (this) {
                for (int i = 0; i < history.length(); i++) {
                    JSONObject m = history.optJSONObject(i);
                    if (m != null) messages.put(new JSONObject(m.toString()));
                }
            }
            body.put("messages", messages);
            if (endpoint.contains("openrouter.ai")) {
                body.put("reasoning", new JSONObject().put("effort", effort));
            } else {
                body.put("reasoning_effort", effort);
            }

            byte[] bytes = body.toString().getBytes(StandardCharsets.UTF_8);
            try (OutputStream out = c.getOutputStream()) {
                out.write(bytes);
            }
            int code = c.getResponseCode();
            InputStream stream = code >= 200 && code < 300 ? c.getInputStream() : c.getErrorStream();
            String raw = readAll(stream);
            if (code < 200 || code >= 300) {
                throw new RuntimeException("HTTP " + code + ": " + compactError(raw));
            }
            JSONObject response = new JSONObject(raw);
            String reply = response.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .optString("content", "");
            if (reply.isEmpty()) reply = "(provider returned an empty response)";
            final String assistant = reply;
            appendHistory("assistant", assistant);
            runOnUiThread(() -> finishRequest(null));
        } catch (Exception e) {
            runOnUiThread(() -> finishRequest(e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()));
        } finally {
            activeConnection = null;
        }
    }

    private String systemPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("You are Detent running in its Android mobile client. Be concise and engineering-focused. ");
        sb.append("This client is intentionally capability-aware: arbitrary shell/PTY, Docker, system package management, systemd, unrestricted filesystem access, local executable plugins, local stdio MCP, and external LSP processes are unavailable. ");
        sb.append("Do not claim those actions ran. You may reason about code, inspect user-attached text, produce patches/instructions, and help orchestrate work within mobile-safe limits. ");
        if (!attachmentText.isEmpty()) {
            sb.append("The user attached a file named ").append(attachmentName).append(". Its current contents follow:\n--- ATTACHMENT ---\n");
            sb.append(attachmentText);
            sb.append("\n--- END ATTACHMENT ---");
        }
        return sb.toString();
    }

    private void finishRequest(String error) {
        active = false;
        stop.setEnabled(false);
        if (error != null && !error.isEmpty()) appendSystem("error · " + error);
        render();
        String next = queue.poll();
        if (next != null) runPrompt(next);
    }

    private void stopCurrent() {
        queue.clear();
        HttpURLConnection c = activeConnection;
        if (c != null) c.disconnect();
        active = false;
        stop.setEnabled(false);
        appendSystem("cancelled · queue cleared");
        renderStatus();
    }

    private synchronized void appendHistory(String role, String content) {
        history.put(new JSONObject().put("role", role).put("content", content));
        saveHistory();
    }

    private void appendSystem(String content) {
        try {
            history.put(new JSONObject().put("role", "system-note").put("content", content));
            saveHistory();
        } catch (Exception ignored) { }
        render();
    }

    private void render() {
        renderStatus();
        StringBuilder out = new StringBuilder();
        if (history.length() == 0) {
            out.append("detent\n");
            out.append("mobile harness client · session ").append(session).append("\n\n");
            out.append("/help        commands\n");
            out.append("/model       provider + model\n");
            out.append("/context     mobile context\n");
            out.append("/cage        capability boundary\n");
            out.append("/sessions    local sessions\n\n");
            out.append("Attach a code/text file with FILE, or start typing.\n");
        } else {
            for (int i = 0; i < history.length(); i++) {
                JSONObject m = history.optJSONObject(i);
                if (m == null) continue;
                String role = m.optString("role", "");
                String content = m.optString("content", "");
                String label;
                if ("user".equals(role)) label = "you";
                else if ("assistant".equals(role)) label = "detent";
                else label = "system";
                out.append(label).append("\n").append(content).append("\n\n");
            }
            if (active) out.append("detent\n… running\n\n");
        }
        transcript.setText(out.toString());
        hint.setText((attachmentName.isEmpty() ? "no file" : attachmentName) + "  ·  Ctrl+Enter send  ·  " + queue.size() + " queued");
        transcriptScroll.post(() -> transcriptScroll.fullScroll(View.FOCUS_DOWN));
    }

    private void renderStatus() {
        String model = prefs.getString("model", "openai/gpt-5");
        String effort = prefs.getString("effort", "medium");
        String state = active ? "● running" : "○ ready";
        status.setText(session + "  ·  " + model + "  ·  " + effort + "  ·  cage:on  ·  " + state);
        status.setTextColor(active ? ACCENT : MUTED);
    }

    private void handleCommand(String raw) {
        String[] bits = raw.trim().split("\\s+", 2);
        String cmd = bits[0].toLowerCase(Locale.ROOT);
        String arg = bits.length > 1 ? bits[1].trim() : "";
        switch (cmd) {
            case "/help":
                appendSystem("commands\n/model [id] · /thinking [low|medium|high] · /context · /tasks · /permissions · /cage · /sessions · /session <name> · /new [name] · /clear · /file · /stop");
                break;
            case "/model":
            case "/models":
                if (arg.isEmpty()) showSettings();
                else { prefs.edit().putString("model", arg).apply(); appendSystem("model → " + arg); }
                break;
            case "/thinking":
                if (arg.equals("low") || arg.equals("medium") || arg.equals("high")) {
                    prefs.edit().putString("effort", arg).apply();
                    appendSystem("reasoning effort → " + arg);
                } else appendSystem("usage: /thinking low|medium|high");
                break;
            case "/context":
                appendSystem("android context\nsession: " + session + "\nattached: " + (attachmentName.isEmpty() ? "none" : attachmentName + " (" + attachmentText.length() + " chars)") + "\nhistory messages: " + history.length() + "\nqueue: " + queue.size());
                break;
            case "/tasks":
                appendSystem("tasks\nrequest: " + (active ? "running" : "idle") + "\nqueued prompts: " + queue.size() + "\nmobile worker concurrency: 1");
                break;
            case "/permissions":
            case "/cage":
                appendSystem("CAGE · Android\n✓ HTTPS provider access\n✓ user-selected document read\n✓ app-local session storage\n✕ arbitrary shell / PTY\n✕ unrestricted filesystem\n✕ Docker / package managers / systemd\n✕ executable local plugins / stdio MCP / external LSP\nThis build fails closed instead of pretending desktop capabilities exist.");
                break;
            case "/sessions":
                appendSystem("sessions\n" + String.join("\n", new ArrayList<>(prefs.getStringSet("sessions", java.util.Collections.singleton("default")))));
                break;
            case "/session":
                if (arg.isEmpty()) appendSystem("usage: /session <name>");
                else switchSession(safeSession(arg));
                break;
            case "/new":
                switchSession(arg.isEmpty() ? "session-" + System.currentTimeMillis() : safeSession(arg));
                synchronized (this) { history = new JSONArray(); saveHistory(); }
                render();
                break;
            case "/clear":
                synchronized (this) { history = new JSONArray(); saveHistory(); }
                render();
                break;
            case "/file": pickFile(); break;
            case "/stop": stopCurrent(); break;
            default: appendSystem("unknown command: " + cmd + " · /help");
        }
    }

    private void switchSession(String name) {
        session = name;
        prefs.edit().putString("session.current", session).apply();
        Set<String> existing = new java.util.HashSet<>(prefs.getStringSet("sessions", new java.util.HashSet<>()));
        existing.add(session);
        prefs.edit().putStringSet("sessions", existing).apply();
        attachmentName = "";
        attachmentText = "";
        loadHistory();
        render();
    }

    private String safeSession(String value) {
        String cleaned = value.replaceAll("[^a-zA-Z0-9._-]", "-");
        return cleaned.isEmpty() ? "default" : cleaned.substring(0, Math.min(48, cleaned.length()));
    }

    private synchronized void loadHistory() {
        try { history = new JSONArray(prefs.getString("history." + session, "[]")); }
        catch (Exception e) { history = new JSONArray(); }
        Set<String> existing = new java.util.HashSet<>(prefs.getStringSet("sessions", new java.util.HashSet<>()));
        existing.add(session);
        prefs.edit().putStringSet("sessions", existing).apply();
    }

    private synchronized void saveHistory() {
        prefs.edit().putString("history." + session, history.toString()).apply();
    }

    private void showSettings() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        int p = dp(16);
        box.setPadding(p, p, p, 0);

        EditText endpoint = field("Endpoint", prefs.getString("api.endpoint", "https://openrouter.ai/api/v1/chat/completions"), false);
        EditText key = field("API key", prefs.getString("api.key", ""), true);
        EditText model = field("Model", prefs.getString("model", "openai/gpt-5"), false);
        EditText effort = field("Reasoning: low / medium / high", prefs.getString("effort", "medium"), false);
        box.addView(endpoint); box.addView(key); box.addView(model); box.addView(effort);

        new AlertDialog.Builder(this)
                .setTitle("Detent provider")
                .setView(box)
                .setPositiveButton("Save", (d, which) -> {
                    String e = endpoint.getText().toString().trim();
                    String k = key.getText().toString().trim();
                    String m = model.getText().toString().trim();
                    String r = effort.getText().toString().trim().toLowerCase(Locale.ROOT);
                    if (!e.startsWith("https://")) {
                        Toast.makeText(this, "HTTPS endpoint required", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!(r.equals("low") || r.equals("medium") || r.equals("high"))) r = "medium";
                    prefs.edit().putString("api.endpoint", e).putString("api.key", k).putString("model", m).putString("effort", r).apply();
                    render();
                })
                .setNegativeButton("Cancel", null)
                .show();
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
        String[] commands = {"/help", "/model", "/thinking high", "/context", "/tasks", "/permissions", "/cage", "/sessions", "/new", "/clear"};
        new AlertDialog.Builder(this).setTitle("Detent commands").setItems(commands, (d, i) -> {
            String c = commands[i];
            if (c.equals("/model")) showSettings();
            else handleCommand(c);
        }).show();
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
        io.execute(() -> {
            try {
                String name = displayName(uri);
                String text;
                try (InputStream in = getContentResolver().openInputStream(uri)) {
                    text = readLimited(in, 240_000);
                }
                final String n = name;
                final String t = text;
                runOnUiThread(() -> {
                    attachmentName = n;
                    attachmentText = t;
                    appendSystem("attached · " + n + " · " + t.length() + " chars");
                });
            } catch (Exception e) {
                runOnUiThread(() -> appendSystem("file error · " + e.getMessage()));
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
            return String.valueOf(e);
        } catch (Exception ignored) {
            return raw.length() > 500 ? raw.substring(0, 500) : raw;
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onDestroy() {
        HttpURLConnection c = activeConnection;
        if (c != null) c.disconnect();
        io.shutdownNow();
        super.onDestroy();
    }
}
