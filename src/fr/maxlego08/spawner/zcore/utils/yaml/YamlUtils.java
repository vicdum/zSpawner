package fr.maxlego08.spawner.zcore.utils.yaml;

import fr.maxlego08.spawner.ZSpawnerLevel;
import fr.maxlego08.spawner.api.SpawnerLevel;
import fr.maxlego08.spawner.zcore.logger.Logger;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
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

    protected List<SpawnerLevel> loadLevels() {
        List<SpawnerLevel> levels = new ArrayList<>();
        FileConfiguration config = getConfig();
        for (String key : config.getConfigurationSection("virtual.levels").getKeys(false)) {
            String path = "virtual.levels." + key + ".";
            int level = config.getInt(path + "level");
            String displayName = config.getString(path + "displayName");
            List<Material> blacklistMaterials = config.getStringList(path + "blacklistMaterials").stream().map(Material::valueOf).collect(Collectors.toList());
            List<Material> whitelistMaterials = config.getStringList(path + "whitelistMaterials").stream().map(Material::valueOf).collect(Collectors.toList());
            int distance = config.getInt(path + "distance");
            double experienceMultiplier = config.getDouble(path + "experienceMultiplier");
            double lootMultiplier = config.getDouble(path + "lootMultiplier");
            boolean autoKill = config.getBoolean(path + "autoKill");
            int maxEntity = config.getInt(path + "maxEntity");
            int minDelay = config.getInt(path + "minDelay");
            int maxDelay = config.getInt(path + "maxDelay");
            int minSpawn = config.getInt(path + "minSpawn");
            int maxSpawn = config.getInt(path + "maxSpawn");
            int mobPerMinute = config.getInt(path + "mobPerMinute");

            SpawnerLevel levelConfig = new ZSpawnerLevel(level, key, displayName, blacklistMaterials, whitelistMaterials, distance, experienceMultiplier, lootMultiplier, autoKill, maxEntity, minDelay, maxDelay, minSpawn, maxSpawn, mobPerMinute);
            levels.add(levelConfig);
        }
        return levels;
    }


}
