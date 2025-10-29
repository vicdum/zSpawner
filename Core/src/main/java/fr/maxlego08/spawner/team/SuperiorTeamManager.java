package fr.maxlego08.spawner.team;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.team.TeamManager;

import java.util.UUID;

public class SuperiorTeamManager implements TeamManager {

    private final SpawnerPlugin plugin;

    public SuperiorTeamManager(SpawnerPlugin plugin) {
        this.plugin = plugin;
        SuperiorSkyblockAPI.getSuperiorSkyblock().getProviders().addEntitiesProvider(entity ->
                !entity.getPersistentDataContainer().has(this.plugin.getSpawnerKey()));
    }

    @Override
    public boolean canAccess(UUID ownerId, UUID playerId) {
        if (ownerId == null || playerId == null) {
            return false;
        }

        if (ownerId.equals(playerId)) {
            return true;
        }

        SuperiorPlayer owner = SuperiorSkyblockAPI.getPlayer(ownerId);
        if (owner == null) {
            return false;
        }

        Island island = owner.getIsland();
        if (island == null) {
            return false;
        }

        SuperiorPlayer target = SuperiorSkyblockAPI.getPlayer(playerId);
        if (target == null) {
            return false;
        }

        return island.isMember(target);
    }
}
