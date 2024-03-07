package fr.maxlego08.spawner.zcore.utils.yaml;

import fr.maxlego08.spawner.ZSpawnerOption;
import fr.maxlego08.spawner.api.SpawnerOption;
import fr.maxlego08.spawner.zcore.logger.Logger;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class YamlUtils extends ZUtils {

    protected transient final JavaPlugin plugin;

    /**
     * @param plugin
     */
    public YamlUtils(JavaPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    /**
     * @return file confirguration
     */
    protected FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    protected YamlConfiguration getConfig(File file) {
        if (file == null) return null;
        return YamlConfiguration.loadConfiguration(file);
    }

    protected YamlConfiguration getConfig(String path) {
        File file = new File(plugin.getDataFolder() + "/" + path);
        if (!file.exists()) return null;
        return getConfig(file);
    }

    /**
     * Send info to console
     *
     * @param message
     */
    protected void info(String message) {
        Logger.info(message);
    }

    /**
     * Send success to console
     *
     * @param message
     */
    protected void success(String message) {
        Logger.info(message, Logger.LogType.SUCCESS);
    }

    /**
     * Send error to console
     *
     * @param message
     */
    protected void error(String message) {
        Logger.info(message, Logger.LogType.ERROR);
    }

    /**
     * Send warn to console
     *
     * @param message
     */
    protected void warn(String message) {
        Logger.info(message, Logger.LogType.WARNING);
    }

    protected List<EntityType> loadEntityList(String path) {
        return getConfig().getStringList(path).stream().map(name -> {
            try {
                return EntityType.valueOf(name.toUpperCase());
            } catch (Exception ignored) {
                warn("Warning: Entity type " + name + " not found and will be ignored.");
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected Map<EntityType, String> loadEntityMaterials() {
        Map<EntityType, String> spawnerMaterials = new HashMap<>();

        List<?> list = getConfig().getList("entitiesMaterial");
        if (list != null) {
            for (Object object : list) {
                if (object instanceof Map<?, ?>) {
                    Map<String, String> currentMap = (Map<String, String>) object;
                    currentMap.forEach((entity, material) -> {
                        Arrays.stream(EntityType.values()).filter(e -> e.name().equalsIgnoreCase(entity)).findFirst().ifPresent(entityType -> spawnerMaterials.put(entityType, material));
                    });
                }
            }
        }

        return spawnerMaterials;
    }

    protected List<Material> loadBlacklist() {
        return getConfig().getStringList("blacklistBlocks").stream().map(material -> {
            try {
                return Material.valueOf(material.toUpperCase());
            } catch (Exception exception) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected SpawnerOption loadDefaultSpawnerOption() {

        FileConfiguration configuration = getConfig();
        String path = "virtual.defaultSpawnerOption.";
        double distance = configuration.getDouble(path + "distance", 6);
        double experienceMultiplier = configuration.getDouble(path + "experienceMultiplier", 1);
        double lootMultiplier = configuration.getDouble(path + "lootMultiplier", 1);
        boolean autoKill = configuration.getBoolean(path + "autoKill", false);
        boolean autoSell = configuration.getBoolean(path + "autoSell", false);
        int mobPerMinute = configuration.getInt(path + "mobPerMinute", 0);
        int maxEntity = configuration.getInt(path + "maxEntity", 1000);
        int minDelay = configuration.getInt(path + "minDelay", 8000);
        int maxDelay = configuration.getInt(path + "maxDelay", 15000);
        int minSpawn = configuration.getInt(path + "minSpawn", 1);
        int maxSpawn = configuration.getInt(path + "maxSpawn", 3);

        return new ZSpawnerOption(distance, experienceMultiplier, lootMultiplier, autoKill, autoSell, maxEntity, minDelay, maxDelay, minSpawn, maxSpawn, mobPerMinute);
    }


}
