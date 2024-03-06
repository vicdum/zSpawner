package fr.maxlego08.spawner.zcore.utils.yaml;

import fr.maxlego08.spawner.zcore.logger.Logger;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
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

}
