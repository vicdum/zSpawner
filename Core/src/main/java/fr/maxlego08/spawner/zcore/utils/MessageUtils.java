package fr.maxlego08.spawner.zcore.utils;

import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.zcore.enums.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Allows you to manage messages sent to players and the console
 *
 * @author Maxence
 */
public abstract class MessageUtils extends LocationUtils {

    private final static int CENTER_PX = 154;

    public void messageWO(SpawnerPlugin plugin, CommandSender sender, Message message, Object... args) {
        MetaUpdater metaUpdater = plugin.getMetaUpdater();

        if (sender instanceof ConsoleCommandSender) {
            if (!message.getMessages().isEmpty()) {
                for (String msg : message.getMessages()) {
                    metaUpdater.sendMessage(sender,Message.PREFIX.msg() + getMessage(msg, args));
                }
            } else {
                metaUpdater.sendMessage(sender,Message.PREFIX.msg() + getMessage(message, args));
            }
        } else {
            Player player = (Player) sender;
            if (!message.getMessages().isEmpty()) {
                for (String msg : message.getMessages()) {
                    metaUpdater.sendMessage(sender, this.papi(getMessage(msg, args), player));
                }
            } else {
                metaUpdater.sendMessage(sender, this.papi(getMessage(message, args), player));
            }
        }
    }

    /**
     * @param sender
     * @param message
     * @param args
     */
    public void message(SpawnerPlugin plugin, CommandSender sender, String message, Object... args) {
        plugin.getMetaUpdater().sendMessage(sender, Message.PREFIX.msg() + getMessage(message, args));
    }

    /**
     * Allows you to send a message to a command sender
     *
     * @param sender  User who sent the command
     * @param message The message - Using the Message enum for simplified message
     *                management
     * @param args    The arguments - The arguments work in pairs, you must put for
     *                example %test% and then the value
     */
    public void message(SpawnerPlugin plugin, CommandSender sender, Message message, Object... args) {

        MetaUpdater updater = plugin.getMetaUpdater();

        if (sender instanceof ConsoleCommandSender) {
            if (!message.getMessages().isEmpty()) {
                for (String msg : message.getMessages()) {
                    updater.sendMessage(sender, Message.PREFIX.msg() + getMessage(msg, args));
                }
            } else {
                updater.sendMessage(sender, Message.PREFIX.msg() + getMessage(message, args));
            }
        } else {

            Player player = (Player) sender;
            switch (message.getType()) {
                case CENTER -> {
                    if (!message.getMessages().isEmpty()) {
                        for (String msg : message.getMessages()) {
                            updater.sendMessage(sender, this.getCenteredMessage(this.papi(getMessage(msg, args), player)));
                        }
                    } else {
                        updater.sendMessage(sender, this.getCenteredMessage(this.papi(getMessage(message, args), player)));
                    }
                }
                case ACTION -> {
                    updater.sendAction(player, this.papi(getMessage(message, args), player));
                }
                case TCHAT -> {
                    if (!message.getMessages().isEmpty()) {
                        for (String msg : message.getMessages()) {
                            updater.sendMessage(sender, this.papi(Message.PREFIX.msg() + getMessage(msg, args), player));
                        }
                    } else {
                        updater.sendMessage(sender, this.papi(Message.PREFIX.msg() + getMessage(message, args), player));
                    }
                }
                case TITLE -> {
                    String title = message.getTitle();
                    String subTitle = message.getSubTitle();
                    int fadeInTime = message.getStart();
                    int showTime = message.getTime();
                    int fadeOutTime = message.getEnd();
                    updater.sendTitle(player, this.papi(this.getMessage(title, args), player), this.papi(this.getMessage(subTitle, args), player), fadeInTime, showTime, fadeOutTime);
                }
            }

        }
    }

    protected String getMessage(Message message, Object... args) {
        return getMessage(message.getMessage(), args);
    }

    public String getMessage(String message, Object... args) {

        if (args.length % 2 != 0)
            throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");

        for (int i = 0; i < args.length; i += 2) {
            if (args[i] == null || args[i + 1] == null)
                throw new IllegalArgumentException("Keys and replacement values must not be null.");
            message = message.replace(args[i].toString(), args[i + 1].toString());
        }
        return message;
    }

    /**
     * @param message
     * @return message
     */
    protected String getCenteredMessage(String message) {
        if (message == null || message.equals(""))
            return "";
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                } else
                    isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb + message;
    }
}
