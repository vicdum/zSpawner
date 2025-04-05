package fr.maxlego08.spawner.api.utils;

import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public record SpawnerResult(SpawnerType spawnerType, EntityType entityType, UUID spawnerId) {

}
