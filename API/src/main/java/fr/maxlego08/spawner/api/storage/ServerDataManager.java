package fr.maxlego08.spawner.api.storage;

import java.util.Optional;

public interface ServerDataManager {
    Optional<ServerProfile> getServerProfile();

    ServerProfile getOrCreate();

    void clearAll();

    void loadServerData();
}
