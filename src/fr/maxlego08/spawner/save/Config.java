package fr.maxlego08.spawner.save;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static boolean enableDebug = true;
    public static boolean enableDebugTime = false;
    public static boolean disableNaturalSpawnerExplosion = false;
    public static boolean dropNaturalSpawnerOnExplose = false;
    public static boolean ownerCanBreakSpawner = false;
    public static Map<SpawnerType, Boolean> spawnerExplosion = new HashMap<>();
    public static Map<SpawnerType, Boolean> spawnerDrop = new HashMap<>();

    /**
     * static Singleton instance.
     */
    private static volatile Config instance;


    /**
     * Private constructor for singleton.
     */
    private Config() {
    }

    /**
     * Return a singleton instance of Config.
     */
    public static Config getInstance() {
        // Double lock for thread safety.
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    public static boolean checkSpawnerExplosion() {
        return dropNaturalSpawnerOnExplose || disableNaturalSpawnerExplosion || spawnerExplosion.values().stream().anyMatch(Boolean::booleanValue) || spawnerDrop.values().stream().anyMatch(Boolean::booleanValue);
    }


    public void load(SpawnerPlugin plugin) {

        FileConfiguration configuration = plugin.getConfig();

        enableDebug = configuration.getBoolean("enableDebug", false);
        enableDebugTime = configuration.getBoolean("enableDebugTime", false);
        ownerCanBreakSpawner = configuration.getBoolean("ownerCanBreakSpawner", true);

        dropNaturalSpawnerOnExplose = configuration.getBoolean("dropNaturalSpawnerOnExplose", true);
        disableNaturalSpawnerExplosion = configuration.getBoolean("disableNaturalSpawnerExplosion", true);

        spawnerExplosion.put(SpawnerType.GUI, configuration.getBoolean("disableSpawnerExplosion.GUI", false));
        spawnerExplosion.put(SpawnerType.CLASSIC, configuration.getBoolean("disableSpawnerExplosion.CLASSIC", false));

        spawnerDrop.put(SpawnerType.GUI, configuration.getBoolean("dropSpawnerOnExplose.GUI", false));
        spawnerDrop.put(SpawnerType.CLASSIC, configuration.getBoolean("dropSpawnerOnExplose.CLASSIC", false));
    }
}
