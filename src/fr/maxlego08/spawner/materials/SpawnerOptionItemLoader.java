package fr.maxlego08.spawner.materials;

import fr.maxlego08.menu.api.loader.MaterialLoader;
import fr.maxlego08.spawner.SpawnerPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnerOptionItemLoader implements MaterialLoader {

    private final SpawnerPlugin plugin;

    public SpawnerOptionItemLoader(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getKey() {
        return "zspawner-option";
    }

    @Override
    public ItemStack load(Player player, YamlConfiguration configuration, String path, String materialString) {
        try {
            var optional = this.plugin.getUpgradeManager().getUpgrade(materialString);
            if (optional.isPresent()) {
                var upgrade = optional.get();
                return upgrade.getItemStack().build(player, false);
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
