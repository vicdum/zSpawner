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

import java.math.BigDecimal;

public class LocationPriceDisplayButton extends Button {

    private final SpawnerPlugin plugin;

    public LocationPriceDisplayButton(Plugin plugin) {
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

            // Add location price placeholders
            int locationTime = playerSpawner.getLocationTime();
            double locationPricePerMinute = spawner.getOption().getLocationPrice();
            BigDecimal totalPrice = BigDecimal.valueOf(locationTime * locationPricePerMinute);

            placeholders.register("location_time", String.valueOf(locationTime));
            placeholders.register("location_price", String.valueOf(locationPricePerMinute));
            placeholders.register("location_price_total", totalPrice.toString());
        }

        return getItemStack().build(player, false, placeholders);
    }
}

