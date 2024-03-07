package fr.maxlego08.spawner.api.utils;

import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class SpawnerResult {

    private final SpawnerType spawnerType;
    private final EntityType entityType;
    private final UUID spawnerId;

    public SpawnerResult(SpawnerType spawnerType, EntityType entityType, UUID spawnerId) {
        this.spawnerType = spawnerType;
        this.entityType = entityType;
        this.spawnerId = spawnerId;
    }

    public SpawnerType getSpawnerType() {
        return spawnerType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public UUID getSpawnerId() {
        return spawnerId;
    }
}
