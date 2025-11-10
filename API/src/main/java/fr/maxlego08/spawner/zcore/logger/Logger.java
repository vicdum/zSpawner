package fr.maxlego08.spawner.zcore.logger;

import fr.maxlego08.menu.api.configuration.Config;
import org.bukkit.Bukkit;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Logger {

    private final String prefix;
    private static Logger logger;

    public Logger(String prefix) {
        this.prefix = prefix;
        logger = this;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void info(String message, LogType type) {
        getLogger().log(message, type);
    }

    public static void info(String message) {
        getLogger().log(message, LogType.INFO);
    }

    public static void showException(String errorName,Throwable throwable) {
        if (Config.enableDebug) {
            getLogger().printException(errorName,throwable);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public enum LogType {
        ERROR("§c"),
        INFO("§7"),
        WARNING("§6"),
        SUCCESS("§2");

        private final String color;

        LogType(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }

    public void log(String message, LogType type) {
        Bukkit.getConsoleSender().sendMessage("§8[§e" + prefix + "§8] " + type.getColor() + getColoredMessage(message));
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage("§8[§e" + prefix + "§8] §e" + getColoredMessage(message));
    }

    public void log(String message, Object... args) {
        log(String.format(message, args));
    }

    public void log(String message, LogType type, Object... args) {
        log(String.format(message, args), type);
    }

    public void log(String[] messages, LogType type) {
        for (String message : messages) {
            log(message, type);
        }
    }

    public void printException(String errorName, Throwable throwable) {
        this.log("An error occurred while "+errorName+".",LogType.ERROR);
        this.log("Exception error message: "+throwable.getMessage(),LogType.ERROR);
        this.log("Please check the stack trace below for more details. If you don't understand the issue report it to the developer.",LogType.ERROR);
        this.log("------------------- Stack Trace ------------------",LogType.ERROR);
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
        }
        log(sw.toString(), LogType.ERROR);
        this.log("--------------------------------------------------",LogType.ERROR);
    }

    public String getColoredMessage(String message) {
        return message.replace("<&>", "§");
    }
}
