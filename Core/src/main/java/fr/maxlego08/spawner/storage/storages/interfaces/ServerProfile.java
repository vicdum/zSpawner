package fr.maxlego08.spawner.storage.storages.interfaces;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ServerProfile {
    Optional<Spawner> getSpawner(UUID uuid, SpawnerType spawnerType);

    Optional<Spawner> getSpawner(UUID uuid);

    Optional<Spawner> getSpawner(SpawnerType spawnerType, Location location);

    Optional<Spawner> getSpawner(Location location);

    Optional<Spawner> getSpawnerByEntity(LivingEntity entity);

    Optional<Spawner> getSpawnerByDeadEntity(Entity entity);

    Collection<Spawner> getSpawners();

    Collection<Spawner> getSpawners(SpawnerType spawnerType);

    Collection<Spawner> getSpawners(UUID ownerUUID);

    Collection<Spawner> getSpawners(UUID ownerUUID, SpawnerType spawnerType);

    long getSpawnersInChunkCount(int x, int z);

    long getSpawnersInChunkCount(int x, int z, EntityType entityType);

    void addSpawner(Spawner spawner);

    void deleteSpawner(Spawner spawner);

    void deleteSpawner(Location location);

    void loadSpawner(Spawner spawner);
}
