package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import fr.maxlego08.spawner.zcore.enums.Permission;
import org.bukkit.entity.Player;

public abstract class AbstractSpawnerButton extends Button {

    protected final SpawnerPlugin plugin;

    public AbstractSpawnerButton(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean checkPermission(Player player, InventoryEngine inventory, Placeholders placeholders) {

        PlayerSpawner playerSpawner = this.plugin.getManager().getPlayerSpawners().get(player.getUniqueId());
        Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner() == null ? null : playerSpawner.getVirtualSpawner();
        if (spawner == null) return false;

        return super.checkPermission(player, inventory, placeholders) && (spawner.getOwner().equals(player.getUniqueId()) || player.hasPermission(Permission.ZSPAWNER_BYPASS.getPermission()));
    }

}
