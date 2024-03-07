package fr.maxlego08.spawner.save;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    public static boolean enableLimit = true;
    public static int globalLimit = 5;
    public static Map<EntityType, Integer> entityLimits = new HashMap<>();
    public static boolean enableDebug = true;
    public static boolean enableDebugTime = false;
    public static boolean disableNaturalSpawnerExplosion = false;
    public static boolean dropNaturalSpawnerOnExplose = false;
    public static boolean ownerCanBreakSpawner = false;
    public static Map<SpawnerType, Boolean> spawnerExplosion = new HashMap<>();
    public static Map<SpawnerType, Boolean> spawnerDrop = new HashMap<>();
    public static Material virtualMaterial = Material.LODESTONE;
    public static String virtualName = "&6x%amount%";
    public static String defaultLevelName = "level1";

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

        enableLimit = configuration.getBoolean("chunkLimit.enable", false);
        globalLimit = configuration.getInt("chunkLimit.global", 5);

        entityLimits.clear();
        List<?> limitsList = configuration.getList("stackableSpawner.limits");
        if (limitsList != null) {
            for (Object limitObject : limitsList) {
                if (limitObject instanceof Map<?, ?>) {
                    Map<String, Integer> limitMap = (Map<String, Integer>) limitObject;
                    limitMap.forEach((entity, amount) -> {
                        Arrays.stream(EntityType.values()).filter(e -> e.name().equalsIgnoreCase(entity)).findFirst().ifPresent(entityType -> entityLimits.put(entityType, amount));
                    });
                }
            }
        }

        dropNaturalSpawnerOnExplose = configuration.getBoolean("dropNaturalSpawnerOnExplose", true);
        disableNaturalSpawnerExplosion = configuration.getBoolean("disableNaturalSpawnerExplosion", true);

        spawnerExplosion.put(SpawnerType.GUI, configuration.getBoolean("disableSpawnerExplosion.GUI", false));
        spawnerExplosion.put(SpawnerType.CLASSIC, configuration.getBoolean("disableSpawnerExplosion.CLASSIC", false));

        spawnerDrop.put(SpawnerType.GUI, configuration.getBoolean("dropSpawnerOnExplose.GUI", false));
        spawnerDrop.put(SpawnerType.CLASSIC, configuration.getBoolean("dropSpawnerOnExplose.CLASSIC", false));

        virtualMaterial = Material.valueOf(configuration.getString("virtual.material", "LODESTONE"));
        virtualName = configuration.getString("virtual.name", "&6x%amount%");
        defaultLevelName = configuration.getString("virtual.defaultLevelName", "level1");
    }
}
