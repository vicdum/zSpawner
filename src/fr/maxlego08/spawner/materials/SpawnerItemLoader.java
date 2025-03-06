package fr.maxlego08.spawner.materials;

import fr.maxlego08.menu.api.loader.MaterialLoader;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SpawnerItemLoader implements MaterialLoader {

    private final SpawnerPlugin plugin;

    public SpawnerItemLoader(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getKey() {
        return "zspawner";
    }

    @Override
    public ItemStack load(Player player, YamlConfiguration configuration, String path, String materialString) {
        try {
            String[] array = materialString.split(":");
            if (array.length != 2) {
                plugin.getLogger().info("Material " + materialString + " is not valid, expected format: zspawner:entityType:spawnerType");
                return null;
            }

            EntityType entityType = EntityType.valueOf(array[0].toUpperCase());
            SpawnerType spawnerType = SpawnerType.valueOf(array[1].toUpperCase());
            return plugin.getManager().getSpawnerItemStack(player, spawnerType, entityType, null);
        } catch (Exception ignored) {
        }
        return null;
    }
}
