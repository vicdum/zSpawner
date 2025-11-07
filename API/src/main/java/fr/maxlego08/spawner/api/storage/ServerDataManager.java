package fr.maxlego08.spawner.api.storage;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ServerDataManager {
    Optional<ServerProfile> getServerProfile();

    @NotNull ServerProfile getOrCreate();

    void clearAll();

    void loadServerData();
}
