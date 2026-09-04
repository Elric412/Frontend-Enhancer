package dev.detent.mobile;

import static org.junit.Assert.*;

import org.junit.Test;

public class MobileCoreTest {
    @Test
    public void authAliasesToProviderConfig() {
        CommandRouter.Command c = CommandRouter.parse("/auth");
        assertEquals(CommandRouter.Action.OPEN_PROVIDER_SETTINGS, c.action);
    }

    @Test
    public void commandParserSeparatesCommandAndArgument() {
        CommandRouter.Command c = CommandRouter.parse("  /model   openai/gpt-5  ");
        assertEquals(CommandRouter.Action.SET_MODEL, c.action);
        assertEquals("openai/gpt-5", c.argument);
    }

    @Test
    public void unknownCommandReturnsHelpfulSuggestion() {
        CommandRouter.Command c = CommandRouter.parse("/auht");
        assertEquals(CommandRouter.Action.UNKNOWN, c.action);
        assertTrue(c.message.contains("/auth"));
        assertTrue(c.message.contains("/help"));
    }

    @Test
    public void providerConfigRejectsNonHttpsEndpoint() {
        ProviderConfig.Validation result = ProviderConfig.validate("http://example.com/v1/chat/completions", "key", "model", "medium");
        assertFalse(result.valid);
        assertTrue(result.message.toLowerCase().contains("https"));
    }

    @Test
    public void providerConfigNormalizesUnknownEffort() {
        ProviderConfig.Validation result = ProviderConfig.validate("https://example.com/v1/chat/completions", "key", "model", "turbo");
        assertTrue(result.valid);
        assertEquals("medium", result.config.effort);
    }

    @Test
    public void sessionNamesAreSafeAndBounded() {
        String safe = SessionNames.safe(" ../My weird session name?? ");
        assertFalse(safe.contains(" "));
        assertFalse(safe.contains("/"));
        assertTrue(safe.length() <= 48);
        assertFalse(safe.isEmpty());
    }
}
