package fr.maxlego08.spawner.storage.storages.interfaces;

import java.util.Optional;

public interface ServerDataManager {
    Optional<ServerProfile> getServerProfile();

    ServerProfile getOrCreate();

    void clearAll();

    void loadServerData();
}
