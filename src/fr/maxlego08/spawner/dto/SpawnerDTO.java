package fr.maxlego08.spawner.dto;

import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public record SpawnerDTO(UUID owner, UUID spawner_id, String location, SpawnerType type, long placed_at, EntityType entity_type, int amount, BlockFace block_face) {
}
