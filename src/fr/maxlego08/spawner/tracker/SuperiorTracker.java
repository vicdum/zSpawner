package fr.maxlego08.spawner.tracker;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import fr.maxlego08.spawner.SpawnerPlugin;

public class SuperiorTracker {

    public SuperiorTracker(SpawnerPlugin plugin) {
        SuperiorSkyblockAPI.getSuperiorSkyblock().getProviders().addEntitiesProvider(entity -> !entity.getPersistentDataContainer().has(plugin.getSpawnerKey()));
    }

}
