package fr.maxlego08.spawner.dto;

import java.util.UUID;

public record ItemDTO(UUID unique_id, UUID spawner_id, String item_stack, long amount) {
}
