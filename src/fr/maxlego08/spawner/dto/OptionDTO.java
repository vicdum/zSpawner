package fr.maxlego08.spawner.dto;

import java.util.UUID;

public record OptionDTO(UUID spawnerId, double distance, double experienceMultiplier, double lootMultiplier, boolean autoKill, boolean autoSell, int maxEntity, int minDelay, int maxDelay, int minSpawn, int maxSpawn, int mobPerMinute) {
}
