package fr.maxlego08.spawner.api;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface SpawnerItem {

    @NotNull ItemStack getItemStack();

    long getAmount();

    void setAmount(long amount);

    void addAmount(long amount);

    void removeAmount(long amount);

    boolean isSimilar(ItemStack itemStack);

    @NotNull UUID getUniqueId();

    @NotNull UUID getSpawnerUUID();

    void save();
}
