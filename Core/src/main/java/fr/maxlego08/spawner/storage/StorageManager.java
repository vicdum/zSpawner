package fr.maxlego08.spawner.storage;

import fr.maxlego08.sarah.database.DatabaseType;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.api.storage.SpawnerStorage;
import fr.maxlego08.spawner.storage.storages.DatabaseStorage;
import fr.maxlego08.spawner.zcore.utils.compatibility.FoliaCompatibilityManager;
import fr.maxlego08.spawner.zcore.utils.storage.Persist;
import org.bukkit.configuration.file.FileConfiguration;

public class StorageManager implements SpawnerStorage {

    private final DatabaseType storageType;
    private IStorage storage;

    public StorageManager(SpawnerPlugin plugin, FoliaCompatibilityManager foliaManager) {

        FileConfiguration configuration = plugin.getConfig();
        this.storageType = DatabaseType.valueOf(configuration.getString("storage", "SQLITE"));
//        long updateInterval = configuration.getLong("updateInterval", 12000);

        this.storage = new DatabaseStorage(plugin, foliaManager);

//        foliaManager.runTimerAsync(this::saveTask, updateInterval, updateInterval, TimeUnit.MILLISECONDS);
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
    public DatabaseType getStorageType() {
        return this.storageType;
    }

    @Override
    public void save(Persist persist) {
        this.storage.save();
    }

    @Override
    public void load(Persist persist) {
//        this.storage.load();
    }

    public void saveTask() {
        this.storage.update(true);
    }
}
