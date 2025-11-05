package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.requirement.Requirement;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.hooks.currencies.Currencies;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import fr.maxlego08.spawner.storage.ZSpawnerLocationHistory;
import fr.maxlego08.spawner.storage.storages.interfaces.StorageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class PlayerPurchaseSpawnerLocationButton extends Button {
    private final StorageManager storageManager;
    private final SpawnerPlugin plugin;
    private final Currencies currencies;
    private final String economyName;

    public PlayerPurchaseSpawnerLocationButton(SpawnerPlugin plugin, Currencies currencies, String economyName) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
        this.currencies = currencies;
        this.economyName = economyName;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {
        var playerSpawners = this.plugin.getManager().getPlayerSpawners();
        UUID playerUniqueId = player.getUniqueId();
        PlayerSpawner playerSpawner = playerSpawners.get(playerUniqueId);
        if (playerSpawner == null) {
            playerSpawners.put(playerUniqueId, new PlayerSpawner());
            playerSpawner = playerSpawners.get(playerUniqueId);
        }
        Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner();
        if (spawner == null) return;

        int locationTime = playerSpawner.getLocationTime();
        BigDecimal locationPrice = BigDecimal.valueOf(locationTime * spawner.getOption().getLocationPrice());

        BigDecimal balance = this.currencies.getBalance(player, this.economyName == null ? "default" : this.economyName);
        if (balance.compareTo(locationPrice) >= 0) {
            currencies.withdraw(player, locationPrice, this.economyName == null ? "default" : this.economyName, "Location of"+spawner.getSpawnerId()+" for "+locationTime+" seconds");
            placeholders.register("location_price", locationPrice.toString());
            placeholders.register("location_time", String.valueOf(locationTime));
            Requirement first = this.getClickRequirements().getFirst();
            if (first != null){
                List<Action> successActions = first.getSuccessActions();
                for (Action action : successActions) {
                    action.preExecute(player, this, inventory,placeholders);
                }
            }
            long startTime = System.currentTimeMillis();
            spawner.setLastLocationStartTime(startTime);
            spawner.setLastLocationUser(playerUniqueId);
            long locationTimeMs = locationTime * 60000L;
            spawner.setLastLocationTime(locationTimeMs);
            spawner.addLocationHistory(new ZSpawnerLocationHistory(this.storageManager, spawner.getSpawnerId(),startTime,locationTimeMs, playerUniqueId, locationPrice.doubleValue()));
            resetPlayerLocationTime(playerSpawner);
            player.closeInventory();
        } else {
            Requirement first = this.getClickRequirements().getLast();
            if (first != null){
                List<Action> denyActions = first.getDenyActions();
                for (Action action : denyActions) {
                    action.preExecute(player, this, inventory,placeholders);
                }
            }
            resetPlayerLocationTime(playerSpawner);
        }
    }

    private void resetPlayerLocationTime(PlayerSpawner playerSpawner) {
        playerSpawner.setLocationTime(0);
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {
        var playerSpawners = this.plugin.getManager().getPlayerSpawners();
        UUID playerUniqueId = player.getUniqueId();
        PlayerSpawner playerSpawner = playerSpawners.get(playerUniqueId);
        if (playerSpawner == null) {
            playerSpawners.put(playerUniqueId, new PlayerSpawner());
            playerSpawner = playerSpawners.get(playerUniqueId);
        }
        Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner();
        if (spawner == null) return super.getCustomItemStack(player);
        return getItemStack().build(player, false, getPlaceholders(playerSpawner, spawner));
    }

    private Placeholders getPlaceholders(PlayerSpawner playerSpawner, Spawner spawner) {
        Placeholders placeholders = new Placeholders();
        placeholders.register("location_time", String.valueOf(playerSpawner.getLocationTime()));
        placeholders.register("location_price", String.valueOf(spawner.getOption().getLocationPrice()));
        placeholders.register("min_location_time", String.valueOf(spawner.getOption().getMinLocationTime()));
        placeholders.register("max_location_time", String.valueOf(spawner.getOption().getMaxLocationTime()));
        placeholders.register("location_price_total", String.valueOf(playerSpawner.getLocationTime() * spawner.getOption().getLocationPrice()));
        return placeholders;
    }
}
