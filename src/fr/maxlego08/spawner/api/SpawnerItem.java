package fr.maxlego08.spawner.api;

import org.bukkit.inventory.ItemStack;

public interface SpawnerItem {

    ItemStack getItemStack();

    long getAmount();

    void setAmount(long amount);

    boolean needUpdate();

    void addAmount(long amount);

    void removeAmount(long amount);

    boolean isSimilar(ItemStack itemStack);

}
