package fr.maxlego08.spawner.stackable;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import fr.maxlego08.spawner.zcore.utils.storage.Persist;
import fr.maxlego08.spawner.zcore.utils.storage.Savable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StackableManager extends ZUtils implements Savable {

    private final SpawnerPlugin plugin;
    private final List<StackLevel> levels = new ArrayList<>();
    private boolean enable;
    private Map<EntityType, Integer> limits;
    private List<EntityType> blacklist;
    private List<EntityType> whitelist;

    public StackableManager(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void save(Persist persist) {

    }

    @Override
    public void load(Persist persist) {

        FileConfiguration config = this.plugin.getConfig();

        enable = config.getBoolean("stackableSpawner.enable");
        limits = (Map<EntityType, Integer>) config.get("stackableSpawner.limits");
        blacklist = config.getStringList("stackableSpawner.blacklist").stream().map(EntityType::valueOf).collect(Collectors.toList());
        whitelist = config.getStringList("stackableSpawner.whitelist").stream().map(EntityType::valueOf).collect(Collectors.toList());
        List<?> levelsList = config.getList("stackableSpawner.levels");
        if (levelsList != null) {
            for (Object levelObject : levelsList) {
                if (levelObject instanceof Map<?, ?>) {
                    Map<?, ?> levelMap = (Map<?, ?>) levelObject;
                    StackLevel level = new StackLevel(((Number) levelMap.get("stackAmount")).intValue(), ((Number) levelMap.get("delay")).intValue(), ((Number) levelMap.get("minSpawnDelay")).intValue(), ((Number) levelMap.get("maxSpawnDelay")).intValue(), ((Number) levelMap.get("spawnCount")).intValue(), ((Number) levelMap.get("maxNearbyEntities")).intValue(), ((Number) levelMap.get("requiredPlayerRange")).intValue(), ((Number) levelMap.get("spawnRange")).intValue());
                    levels.add(level);
                }
            }
        }


    }
}
