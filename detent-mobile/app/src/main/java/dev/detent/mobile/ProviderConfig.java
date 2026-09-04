package dev.detent.mobile;

import java.util.Locale;

public final class ProviderConfig {
    public final String endpoint;
    public final String apiKey;
    public final String model;
    public final String effort;

    public ProviderConfig(String endpoint, String apiKey, String model, String effort) {
        this.endpoint = endpoint == null ? "" : endpoint.trim();
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.model = model == null ? "" : model.trim();
        this.effort = normalizeEffort(effort);
    }

    public static final class Validation {
        public final boolean valid;
        public final String message;
        public final ProviderConfig config;

        Validation(boolean valid, String message, ProviderConfig config) {
            this.valid = valid;
            this.message = message == null ? "" : message;
            this.config = config;
        }
    }

    private ProviderConfig() { this("", "", "", "medium"); }

    public static Validation validate(String endpoint, String apiKey, String model, String effort) {
        ProviderConfig c = new ProviderConfig(endpoint, apiKey, model, effort);
        if (!c.endpoint.startsWith("https://")) {
            return new Validation(false, "HTTPS endpoint required", c);
        }
        if (c.model.isEmpty()) {
            return new Validation(false, "Model is required", c);
        }
        return new Validation(true, "", c);
    }

    public static String normalizeEffort(String effort) {
        String v = effort == null ? "medium" : effort.trim().toLowerCase(Locale.ROOT);
        if (v.equals("low") || v.equals("medium") || v.equals("high")) return v;
        return "medium";
    }
}
