package dev.detent.mobile;

import java.util.Locale;

public final class CommandRouter {
    public enum Action {
        HELP, OPEN_PROVIDER_SETTINGS, SET_MODEL, SET_THINKING, CONTEXT, TASKS,
        PERMISSIONS, CAGE, SESSIONS, SWITCH_SESSION, NEW_SESSION, CLEAR,
        PICK_FILE, STOP, UNKNOWN
    }

    public static final class Command {
        public final Action action;
        public final String argument;
        public final String message;

        Command(Action action, String argument, String message) {
            this.action = action;
            this.argument = argument == null ? "" : argument;
            this.message = message == null ? "" : message;
        }
    }

    private CommandRouter() {}

    public static Command parse(String raw) {
        String value = raw == null ? "" : raw.trim();
        if (value.isEmpty()) return new Command(Action.UNKNOWN, "", "Empty command. Try /help");
        String[] bits = value.split("\\s+", 2);
        String cmd = bits[0].toLowerCase(Locale.ROOT);
        String arg = bits.length > 1 ? bits[1].trim() : "";
        switch (cmd) {
            case "/help": return new Command(Action.HELP, arg, "");
            case "/auth":
            case "/config": return new Command(Action.OPEN_PROVIDER_SETTINGS, arg, "");
            case "/model":
            case "/models": return new Command(arg.isEmpty() ? Action.OPEN_PROVIDER_SETTINGS : Action.SET_MODEL, arg, "");
            case "/thinking": return new Command(Action.SET_THINKING, arg, "");
            case "/context": return new Command(Action.CONTEXT, arg, "");
            case "/tasks": return new Command(Action.TASKS, arg, "");
            case "/permissions": return new Command(Action.PERMISSIONS, arg, "");
            case "/cage": return new Command(Action.CAGE, arg, "");
            case "/sessions": return new Command(Action.SESSIONS, arg, "");
            case "/session": return new Command(Action.SWITCH_SESSION, arg, "");
            case "/new": return new Command(Action.NEW_SESSION, arg, "");
            case "/clear": return new Command(Action.CLEAR, arg, "");
            case "/file": return new Command(Action.PICK_FILE, arg, "");
            case "/stop": return new Command(Action.STOP, arg, "");
            default:
                String suggestion = nearest(cmd);
                return new Command(Action.UNKNOWN, arg,
                        "Unknown command " + cmd + ". " +
                                (suggestion.isEmpty() ? "Try /help" : "Did you mean " + suggestion + "? Try /help"));
        }
    }

    private static String nearest(String cmd) {
        if (cmd.contains("auth") || cmd.contains("auht") || cmd.contains("login")) return "/auth";
        if (cmd.contains("mod")) return "/model";
        if (cmd.contains("think")) return "/thinking";
        if (cmd.contains("perm")) return "/permissions";
        if (cmd.contains("sess")) return "/sessions";
        return "";
    }
}
