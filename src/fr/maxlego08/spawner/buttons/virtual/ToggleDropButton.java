package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ToggleDropButton extends Button {

    private final SpawnerPlugin plugin;
    private final String textEnable;
    private final String textDisable;

    public ToggleDropButton(SpawnerPlugin plugin, String textEnable, String textDisable) {
        this.plugin = plugin;
        this.textEnable = textEnable;
        this.textDisable = textDisable;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event, InventoryEngine inventory, int slot, Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);

        PlayerSpawner playerSpawner = this.plugin.getManager().getPlayerSpawners().get(player.getUniqueId());
        Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner() == null ? null : playerSpawner.getVirtualSpawner();
        if (spawner == null) return;

        var option = spawner.getOption();
        option.setDropLoots(!option.dropLoots());

        inventory.getSpigotInventory().setItem(slot, getCustomItemStack(player));
    }

    @Override
    public ItemStack getCustomItemStack(Player player) {

        PlayerSpawner playerSpawner = this.plugin.getManager().getPlayerSpawners().get(player.getUniqueId());
        Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner() == null ? null : playerSpawner.getVirtualSpawner();
        if (spawner == null) return super.getCustomItemStack(player);

        var option = spawner.getOption();

        var placeholders = new Placeholders();
        placeholders.register("state", option.dropLoots() ? this.textDisable : this.textEnable);

        return getItemStack().build(player, false, placeholders);
    }

    @Override
    public boolean isPermanent() {
        return true;
    }
}
