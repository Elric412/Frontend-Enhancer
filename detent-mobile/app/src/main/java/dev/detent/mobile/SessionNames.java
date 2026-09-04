package dev.detent.mobile;

public final class SessionNames {
    private SessionNames() {}

    public static String safe(String value) {
        String v = value == null ? "" : value.trim();
        String cleaned = v.replaceAll("[^a-zA-Z0-9._-]+", "-")
                .replaceAll("^-+", "")
                .replaceAll("-+$", "");
        if (cleaned.isEmpty() || cleaned.equals(".") || cleaned.equals("..")) cleaned = "default";
        return cleaned.substring(0, Math.min(48, cleaned.length()));
    }
}
