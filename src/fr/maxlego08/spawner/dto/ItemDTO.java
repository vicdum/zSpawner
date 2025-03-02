package fr.maxlego08.spawner.dto;

import java.util.UUID;

public record ItemDTO(UUID spawnerId, String itemStack, long amount) {
}
