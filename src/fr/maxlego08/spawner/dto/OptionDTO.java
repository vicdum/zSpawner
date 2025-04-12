package fr.maxlego08.spawner.dto;

import java.util.UUID;

public record OptionDTO(
        UUID spawner_id,
        double distance,
        double experience_multiplier,
        double loot_multiplier,
        boolean auto_kill,
        boolean auto_sell,
        int max_entity,
        int min_delay,
        int max_delay,
        int min_spawn,
        int max_spawn,
        int mob_per_minute,
        boolean drop_loots
) {
}
