package fr.maxlego08.spawner.stackable;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import fr.maxlego08.spawner.zcore.utils.storage.Persist;
import fr.maxlego08.spawner.zcore.utils.storage.Savable;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StackableManager extends ZUtils implements Savable {

    private final SpawnerPlugin plugin;
    private final List<StackLevel> levels = new ArrayList<>();
    private boolean enable;
    private int globalLimit;
    private final Map<EntityType, Integer> limits = new HashMap<>();
    private List<EntityType> blacklist = new ArrayList<>();
    private List<EntityType> whitelist = new ArrayList<>();
    private String hologram;

    public StackableManager(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void save(Persist persist) {

    }

    @Override
    public void load(Persist persist) {

        FileConfiguration config = this.plugin.getConfig();

        hologram = config.getString("stackableSpawner.hologram", null);
        enable = config.getBoolean("stackableSpawner.enable", false);
        globalLimit = config.getInt("stackableSpawner.globalLimit", 5);

        this.limits.clear();
        List<?> limitsList = config.getList("stackableSpawner.limits");
        if (limitsList != null) {
            for (Object limitObject : limitsList) {
                if (limitObject instanceof Map<?, ?>) {
                    Map<String, Integer> limitMap = (Map<String, Integer>) limitObject;
                    limitMap.forEach((entity, amount) -> {
                        Arrays.stream(EntityType.values()).filter(e -> e.name().equalsIgnoreCase(entity)).findFirst().ifPresent(entityType -> this.limits.put(entityType, amount));
                    });
                }
            }
        }


        blacklist = config.getStringList("stackableSpawner.blacklist").stream().map(EntityType::valueOf).collect(Collectors.toList());
        whitelist = config.getStringList("stackableSpawner.whitelist").stream().map(EntityType::valueOf).collect(Collectors.toList());
        List<?> levelsList = config.getList("stackableSpawner.levels");
        this.levels.clear();
        if (levelsList != null) {
            for (Object levelObject : levelsList) {
                if (levelObject instanceof Map<?, ?>) {
                    Map<?, ?> levelMap = (Map<?, ?>) levelObject;
                    StackLevel level = new StackLevel(((Number) levelMap.get("stackAmount")).intValue(), ((Number) levelMap.get("delay")).intValue(), ((Number) levelMap.get("minSpawnDelay")).intValue(), ((Number) levelMap.get("maxSpawnDelay")).intValue(), ((Number) levelMap.get("spawnCount")).intValue(), ((Number) levelMap.get("maxNearbyEntities")).intValue(), ((Number) levelMap.get("requiredPlayerRange")).intValue(), ((Number) levelMap.get("spawnRange")).intValue());
                    this.levels.add(level);
                }
            }
        }
    }


    @Override
    public String toString() {
        return "StackableManager{" +
                "plugin=" + plugin +
                ", levels=" + levels +
                ", enable=" + enable +
                ", globalLimit=" + globalLimit +
                ", limits=" + limits +
                ", blacklist=" + blacklist +
                ", whitelist=" + whitelist +
                ", hologram='" + hologram + '\'' +
                '}';
    }

    public List<StackLevel> getLevels() {
        return levels;
    }

    public boolean isEnable() {
        return enable;
    }

    public Map<EntityType, Integer> getLimits() {
        return limits;
    }

    public List<EntityType> getBlacklist() {
        return blacklist;
    }

    public List<EntityType> getWhitelist() {
        return whitelist;
    }

    public Optional<StackLevel> getLevel(int amount) {
        return this.levels.stream().filter(e -> e.getStackAmount() == amount).findFirst();
    }

    public void updateSpawner(CreatureSpawner spawner, int amount) {
        getLevel(amount).ifPresent(stackLevel -> stackLevel.updateSpawner(spawner));
    }

    public String getHologram() {
        return hologram;
    }

    public int getGlobalLimit() {
        return globalLimit;
    }

    public int getLimit(EntityType entityType) {
        return this.limits.getOrDefault(entityType, this.globalLimit);
    }
}
