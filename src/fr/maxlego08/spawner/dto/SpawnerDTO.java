package fr.maxlego08.spawner.dto;

import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public record SpawnerDTO(UUID owner, UUID spawnerId, String location, SpawnerType type, long placedAt, EntityType entityType, int amount, BlockFace blockFace) {
}
