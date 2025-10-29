package fr.maxlego08.spawner.api;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface SpawnerItem extends Updatable{

    ItemStack getItemStack();

    long getAmount();

    void setAmount(long amount);

    void addAmount(long amount);

    void removeAmount(long amount);

    boolean isSimilar(ItemStack itemStack);

    UUID getUniqueId();

}
