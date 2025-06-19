package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.spawner.SpawnerPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class RemoveButton extends Button {

    private final SpawnerPlugin plugin;

    public RemoveButton(Plugin plugin) {
        this.plugin = (SpawnerPlugin) plugin;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {
        player.closeInventory();
        this.plugin.getManager().removeVirtualSpawner(player);
        super.onClick(player, event, inventory, slot, placeholders);
    }

    @Override
    public boolean isPermanent() {
        return true;
    }
}
