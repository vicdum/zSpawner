package fr.maxlego08.spawner.dto;

import java.util.UUID;

public record SpawnerLocationHistoryDTO(UUID spawner_id, long timestamp, long duration, UUID player_id, double price) {
}
