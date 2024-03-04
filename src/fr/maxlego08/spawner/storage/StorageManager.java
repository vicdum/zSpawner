package fr.maxlego08.spawner.storage;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.api.storage.SpawnerStorage;
import fr.maxlego08.spawner.api.storage.StorageType;
import fr.maxlego08.spawner.storage.storages.SqliteStorage;
import fr.maxlego08.spawner.zcore.utils.storage.Persist;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StorageManager implements SpawnerStorage {

    private final StorageType storageType;
    private final ScheduledFuture<?> scheduledTask;
    private IStorage storage;

    public StorageManager(SpawnerPlugin plugin) {

        FileConfiguration configuration = plugin.getConfig();
        this.storageType = StorageType.valueOf(configuration.getString("storage", "SQLITE"));
        long updateInterval = configuration.getLong("updateInterval", 12000);

        this.storage = new SqliteStorage(plugin, true);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        this.scheduledTask = executorService.scheduleAtFixedRate(this::saveTask, updateInterval, updateInterval, TimeUnit.MILLISECONDS);
    }

    @Override
    public IStorage getStorage() {
        return this.storage;
    }

    @Override
    public void setStorage(IStorage storage) {
        this.storage = storage;
    }

    @Override
    public StorageType getStorageType() {
        return this.storageType;
    }

    @Override
    public void save(Persist persist) {
        this.scheduledTask.cancel(true);
        this.storage.save();
    }

    @Override
    public void load(Persist persist) {
        this.storage.load();
    }

    public void saveTask() {
        this.storage.update();
    }
}
