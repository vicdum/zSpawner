package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.spawner.SpawnerPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class RemoveButton extends ZButton {

    private final SpawnerPlugin plugin;

    public RemoveButton(Plugin plugin) {
        this.plugin = (SpawnerPlugin) plugin;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryDefault inventory, int slot, Placeholders placeholders) {
        player.closeInventory();
        this.plugin.getManager().removeVirtualSpawner(player);
        super.onClick(player, event, inventory, slot, placeholders);
    }

    @Override
    public boolean isPermanent() {
        return true;
    }
}
