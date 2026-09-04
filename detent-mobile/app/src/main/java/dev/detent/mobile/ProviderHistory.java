package dev.detent.mobile;

public final class ProviderHistory {
    private ProviderHistory() {}

    public static boolean isSendableRole(String role) {
        return "user".equals(role) || "assistant".equals(role);
    }
}
