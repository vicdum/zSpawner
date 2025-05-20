package fr.maxlego08.spawner.buttons.gui;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.spawner.SpawnerManager;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class SortButton extends Button {

    private final SpawnerPlugin plugin;

    public SortButton(Plugin plugin) {
        this.plugin = (SpawnerPlugin) plugin;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {

        SpawnerManager manager = this.plugin.getManager();
        PlayerSpawner playerSpawner = manager.getPlayerSpawners().computeIfAbsent(player.getUniqueId(), uuid -> new PlayerSpawner());
        playerSpawner.toggleSort();

        manager.openSpawner(player, 1);

        super.onClick(player, event, inventory, slot, placeholders);
    }
}
