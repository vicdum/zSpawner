package fr.maxlego08.spawner.actions;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PlayerLocationPriceAction extends Action {
    private final SpawnerPlugin plugin;
    private final int amount;

    public PlayerLocationPriceAction(SpawnerPlugin plugin, int amount) {
        this.plugin = plugin;
        this.amount = amount;
    }

    @Override
    protected void execute(Player player, Button button, InventoryEngine inventoryEngine, Placeholders placeholders) {
        Map<UUID, PlayerSpawner> playerSpawners = this.plugin.getManager().getPlayerSpawners();

        PlayerSpawner playerSpawner;
        UUID playerUniqueId = player.getUniqueId();

        if (playerSpawners.containsKey(playerUniqueId)) {
            playerSpawner = playerSpawners.get(playerUniqueId);
        } else {
            playerSpawner = new PlayerSpawner();
            playerSpawners.put(playerUniqueId, playerSpawner);
        }

        Spawner spawner = playerSpawner.getVirtualSpawner();
        if (spawner == null) {
            return;
        }

        int currentLocationTime = playerSpawner.getLocationTime();

        int newLocationTime = currentLocationTime + this.amount;

        long minTime = spawner.getOption().getMinLocationTime();
        long maxTime = spawner.getOption().getMaxLocationTime();

        boolean exceedsMax = newLocationTime > maxTime;
        boolean belowZero = newLocationTime < 0;
        boolean belowMin = this.amount <= 0 && newLocationTime < minTime;

        if (exceedsMax || belowMin || belowZero) {
            return;
        }

        playerSpawner.setLocationTime(newLocationTime);

        this.plugin.getInventoryManager().updateInventory(player, this.plugin);
    }
}
