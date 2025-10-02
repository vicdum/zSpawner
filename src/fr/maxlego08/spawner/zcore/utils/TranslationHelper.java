package fr.maxlego08.spawner.zcore.utils;

import org.bukkit.inventory.ItemStack;

public abstract class TranslationHelper {

    protected String getItemName(ItemStack itemStack) {

        if (itemStack == null) {
            return "";

        }
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().getDisplayName();
        }

        String name = itemStack.getType().name().replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

}
