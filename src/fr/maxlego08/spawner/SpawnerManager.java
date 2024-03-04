package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.SpawnerLevel;
import fr.maxlego08.spawner.zcore.utils.ZUtils;

public class SpawnerManager extends ZUtils {

    private final SpawnerPlugin plugin;

    public SpawnerManager(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    public SpawnerLevel getSpawnerLevel(String levelName) {
        return new ZSpawnerLevel("TMP", 1);
    }

}
