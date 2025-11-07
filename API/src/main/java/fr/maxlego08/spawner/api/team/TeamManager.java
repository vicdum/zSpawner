package fr.maxlego08.spawner.api.team;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents an external team plugin capable of sharing access to a spawner
 * between the owner and other players.
 */
public interface TeamManager {

    /**
     * Checks whether the given player can interact with a spawner owned by the
     * specified owner. Implementations should handle null identifiers
     * themselves if necessary.
     *
     * @param ownerId  the unique identifier of the spawner owner
     * @param playerId the unique identifier of the player requesting access
     * @return {@code true} if the player is part of the owner's team and should
     * be granted access, {@code false} otherwise
     */
    boolean canAccess(@Nullable UUID ownerId, @Nullable UUID playerId);
}
