package fr.maxlego08.spawner.actions;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import fr.maxlego08.spawner.zcore.enums.Permission;
import org.bukkit.entity.Player;

public class SpawnerLocationPriceAction extends Action {
    private final SpawnerPlugin plugin;
    private final double amount;

    public SpawnerLocationPriceAction(SpawnerPlugin plugin, double amount) {
        this.plugin = plugin;
        this.amount = amount;
    }

    @Override
    protected void execute(Player player, Button button, InventoryEngine inventoryEngine, Placeholders placeholders) {
        PlayerSpawner playerSpawner = this.plugin.getManager().getPlayerSpawners().get(player.getUniqueId());
        Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner();
        if (spawner == null) return;

        if (!(spawner.getOwner().equals(player.getUniqueId()) || player.hasPermission(Permission.ZSPAWNER_BYPASS.getPermission()))) return;

        spawner.getOption().addLocationPrice(amount);
        this.plugin.getInventoryManager().updateInventory(player, this.plugin);
    }
}