package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.SpawnerItem;
import fr.maxlego08.spawner.api.storage.StorageManager;
import fr.maxlego08.spawner.storage.Updatable;
import fr.maxlego08.spawner.zcore.utils.nms.Base64ItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ZSpawnerItem extends Updatable implements SpawnerItem {
    private final StorageManager storageManager;
    private final UUID spawnerUUID;

    private final UUID uuid;
    private final ItemStack itemStack;
    private long amount;

    public ZSpawnerItem(@NotNull ItemStack itemStack, long amount, @NotNull StorageManager storageManager, @NotNull UUID spawnerUUID) {
        this.uuid = UUID.randomUUID();
        this.itemStack = itemStack;
        this.amount = amount;
        this.storageManager = storageManager;
        this.spawnerUUID = spawnerUUID;
        this.canUpdate();
    }

    public ZSpawnerItem(@NotNull UUID uuid, @NotNull String itemStack, long amount, @NotNull StorageManager storageManager, @NotNull UUID spawnerUUID) {
        this.uuid = uuid;
        this.itemStack = Base64ItemStack.decode(itemStack);
        this.amount = amount;
        this.storageManager = storageManager;
        this.spawnerUUID = spawnerUUID;
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
        this.canUpdate();
    }

    @Override
    public void addAmount(long amount) {
        this.amount += amount;
        this.canUpdate();
    }

    @Override
    public void removeAmount(long amount) {
        this.amount -= amount;
        this.canUpdate();
    }

    @Override
    public boolean isSimilar(ItemStack itemStack) {
        return this.itemStack.isSimilar(itemStack);
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public @NotNull UUID getSpawnerUUID() {
        return this.spawnerUUID;
    }

    @Override
    public void save() {
        this.storageManager.upsertItem(this, spawnerUUID);
    }
}
