package fr.maxlego08.spawner.api.utils;

import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.entity.EntityType;

public class SpawnerResult {

    private final SpawnerType spawnerType;
    private final EntityType entityType;

    public SpawnerResult(SpawnerType spawnerType, EntityType entityType) {
        this.spawnerType = spawnerType;
        this.entityType = entityType;
    }

    public SpawnerType getSpawnerType() {
        return spawnerType;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
