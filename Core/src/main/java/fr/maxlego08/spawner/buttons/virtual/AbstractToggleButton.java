package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

public abstract class AbstractToggleButton extends AbstractSpawnerButton {

    protected final String enableText;
    protected final String disableText;

    public AbstractToggleButton(SpawnerPlugin plugin, String enableText, String disableText) {
        super(plugin);
        this.enableText = enableText;
        this.disableText = disableText;
    }

    @Override
    public void onClick(@NonNull Player player, @NonNull InventoryClickEvent event, @NonNull InventoryEngine inventory, int slot, @NonNull Placeholders placeholders) {
        super.onClick(player, event, inventory, slot, placeholders);

        boolean hasPermission = this.checkPermission(player, inventory, placeholders);
        if (!hasPermission) return;

        PlayerSpawner playerSpawner = this.plugin.getManager().getPlayerSpawners().get(player.getUniqueId());
        Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner();
        if (spawner == null) return;

        toggleOption(spawner);

        inventory.getSpigotInventory().setItem(slot, getCustomItemStack(player, false, placeholders));
    }

    @Override
    public ItemStack getCustomItemStack(Player player, boolean useCache, Placeholders placeholders) {
        PlayerSpawner playerSpawner = this.plugin.getManager().getPlayerSpawners().get(player.getUniqueId());
        Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner();
        if (spawner == null) return super.getCustomItemStack(player, useCache, placeholders);

        placeholders.register("state", getCurrentState(spawner) ? this.disableText : this.enableText);

        return getItemStack().build(player, false, placeholders);
    }

    @Override
    public boolean isPermanent() {
        return true;
    }

    /**
     * Toggle the specific option on the spawner
     *
     * @param spawner The spawner to toggle the option on
     */
    protected abstract void toggleOption(Spawner spawner);

    /**
     * Get the current state of the option
     *
     * @param spawner The spawner to check
     * @return true if enabled, false if disabled
     */
    protected abstract boolean getCurrentState(Spawner spawner);
}

