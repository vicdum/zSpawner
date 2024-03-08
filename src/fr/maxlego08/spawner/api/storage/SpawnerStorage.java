package fr.maxlego08.spawner.api.storage;

import fr.maxlego08.spawner.zcore.utils.storage.NoReloadable;
import fr.maxlego08.spawner.zcore.utils.storage.Savable;

public interface SpawnerStorage extends Savable, NoReloadable {

    void setStorage(IStorage storage);

    IStorage getStorage();

    StorageType getStorageType();

}
