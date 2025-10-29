package fr.maxlego08.spawner.zcore.utils;

import fr.maxlego08.spawner.zcore.enums.Permission;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public abstract class ZUtils extends MessageUtils {

    /**
     * Returns a random integer between two specified values, inclusive.
     *
     * @param a - the first boundary value
     * @param b - the second boundary value
     * @return a random integer between a and b, inclusive; returns a if both values are equal
     */
    protected int getNumberBetween(int a, int b) {
        if (a == b) return a;
        return ThreadLocalRandom.current().nextInt(Math.min(a, b), Math.max(a, b));
    }

    /**
     * Allows to check if an itemstack has a display name
     *
     * @return boolean
     */
    protected boolean hasDisplayName(ItemStack itemStack) {
        return itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName();
    }

    /**
     * Check if the item name contains the given string
     *
     * @param itemStack
     * @param name
     * @return true if the item name contains the string
     */
    protected boolean contains(ItemStack itemStack, String name) {
        return this.hasDisplayName(itemStack) && itemStack.getItemMeta().getDisplayName().contains(name);
    }

    /**
     * Remove the item from the player's hand
     *
     * @param player
     * @param how
     */
    protected void removeItemInHand(Player player, int how) {
        if (player.getItemInHand().getAmount() > how)
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - how);
        else
            player.setItemInHand(new ItemStack(Material.AIR));
        player.updateInventory();
    }

    /**
     * Format a double in a String
     *
     * @param decimal
     * @return formatting current duplicate
     */
    public String format(double decimal) {
        return format(decimal, "#.#");
    }

    /**
     * Format a double in a String
     *
     * @param decimal
     * @param format
     * @return formatting current double according to the given format
     */
    protected String format(double decimal, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(decimal);
    }

    /**
     * @param string
     * @return
     */
    protected String name(String string) {
        String name = string.replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * @param string
     * @return
     */
    protected String name(Material string) {
        String name = string.name().replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * @param itemStack
     * @return
     */
    protected String name(ItemStack itemStack) {
        return this.getItemName(itemStack);
    }

    /**
     * @param permissible
     * @param permission
     * @return
     */
    protected boolean hasPermission(Permissible permissible, Permission permission) {
        return permissible.hasPermission(permission.getPermission());
    }

    /**
     * @param permissible
     * @param permission
     * @return
     */
    protected boolean hasPermission(Permissible permissible, String permission) {
        return permissible.hasPermission(permission);
    }

    /**
     * @param message
     * @return
     */
    protected String color(String message) {
        if (message == null)
            return null;
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, String.valueOf(net.md_5.bungee.api.ChatColor.of(color)));
            matcher = pattern.matcher(message);
        }
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * @param message
     * @return
     */
    protected String colorReverse(String message) {
        Pattern pattern = Pattern.compile(net.md_5.bungee.api.ChatColor.COLOR_CHAR + "x[a-fA-F0-9-"
                + net.md_5.bungee.api.ChatColor.COLOR_CHAR + "]{12}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            String colorReplace = color.replace("§x", "#");
            colorReplace = colorReplace.replace("§", "");
            message = message.replace(color, colorReplace);
            matcher = pattern.matcher(message);
        }

        return message == null ? null : message.replace("§", "&");
    }

    /**
     * @param messages
     * @return
     */
    protected List<String> colorReverse(List<String> messages) {
        return messages.stream().map(message -> colorReverse(message)).collect(Collectors.toList());
    }

    /**
     * @param list
     * @return
     */
    protected String toList(Stream<String> list) {
        return toList(list.collect(Collectors.toList()), "§e", "§6");
    }

    /**
     * @param list
     * @return
     */
    protected String toList(List<String> list) {
        return toList(list, "§e", "§6§n");
    }

    /**
     * @param list
     * @param color
     * @param color2
     * @return
     */
    protected String toList(List<String> list, String color, String color2) {
        if (list == null || list.isEmpty())
            return null;
        if (list.size() == 1)
            return list.getFirst();
        StringBuilder str = new StringBuilder();
        for (int a = 0; a != list.size(); a++) {
            if (a == list.size() - 1)
                str.append(color).append(" et ").append(color2);
            else if (a != 0)
                str.append(color).append(", ").append(color2);
            str.append(list.get(a));
        }
        return str.toString();
    }

    protected String format(long l) {
        return format(l, '.');
    }


    protected String format(long l, char c) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(c);
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(l);
    }

    protected BlockFace getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }

        if (rotation > 45 && rotation <= 135) {
            return BlockFace.NORTH;
        } else if (rotation > 135 && rotation <= 225) {
            return BlockFace.EAST;
        } else if (rotation > 225 && rotation <= 315) {
            return BlockFace.SOUTH;
        } else {
            return BlockFace.WEST;
        }
    }

}
