package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.spawner.SpawnerManager;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class LocationTimeDisplayButton extends Button {

    private final SpawnerPlugin plugin;

    public LocationTimeDisplayButton(Plugin plugin) {
        this.plugin = (SpawnerPlugin) plugin;
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {

        Placeholders placeholders = new Placeholders();

        SpawnerManager manager = this.plugin.getManager();
        PlayerSpawner playerSpawner = manager.getPlayerSpawners().get(player.getUniqueId());
        Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner();

        if (spawner != null) {
            manager.registerPlaceholders(placeholders, spawner);

            int locationTime = playerSpawner.getLocationTime();
            placeholders.register("location_time", String.valueOf(locationTime));
        }

        return getItemStack().build(player, false, placeholders);
    }
}

