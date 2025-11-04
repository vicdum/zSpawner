package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.SpawnerItem;
import fr.maxlego08.spawner.storage.storages.interfaces.StorageManager;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import fr.maxlego08.spawner.zcore.utils.nms.Base64ItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ZSpawnerItem extends ZUtils implements SpawnerItem {
    private final StorageManager storageManager;
    private final UUID spawnerUUID;

    private final UUID uuid;
    private final ItemStack itemStack;
    private long amount;

    public ZSpawnerItem(ItemStack itemStack, long amount, StorageManager storageManager, UUID spawnerUUID) {
        this.uuid = UUID.randomUUID();
        this.itemStack = itemStack;
        this.amount = amount;
        this.storageManager = storageManager;
        this.spawnerUUID = spawnerUUID;
        this.updateDB();
    }

    public ZSpawnerItem(UUID uuid, String itemStack, long amount, StorageManager storageManager, UUID spawnerUUID) {
        this.uuid = uuid;
        this.itemStack = Base64ItemStack.decode(itemStack);
        this.amount = amount;
        this.storageManager = storageManager;
        this.spawnerUUID = spawnerUUID;
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
        this.updateDB();
    }

    @Override
    public void addAmount(long amount) {
        this.amount += amount;
        this.updateDB();
    }

    @Override
    public void removeAmount(long amount) {
        this.amount -= amount;
        this.updateDB();
    }

    @Override
    public boolean isSimilar(ItemStack itemStack) {
        return this.itemStack.isSimilar(itemStack);
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public UUID getSpawnerUUID() {
        return this.spawnerUUID;
    }

    public void updateDB() {
        this.storageManager.upsertItem(this, spawnerUUID);
    }
}
