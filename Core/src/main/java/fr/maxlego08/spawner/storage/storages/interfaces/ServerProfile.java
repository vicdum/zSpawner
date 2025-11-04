package fr.maxlego08.spawner.storage.storages.interfaces;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ServerProfile {
    Optional<Spawner> getSpawner(UUID uuid, SpawnerType spawnerType);

    Optional<Spawner> getSpawner(UUID uuid);

    Collection<Spawner> getSpawners();

    Collection<Spawner> getSpawners(SpawnerType spawnerType);

    void addSpawner(Spawner spawner);

    void deleteSpawner(Spawner spawner);

    void loadSpawner(Spawner spawner);
}
