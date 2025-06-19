package fr.maxlego08.spawner.buttons;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.engine.Pagination;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import fr.maxlego08.spawner.zcore.enums.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ShowButton extends PaginateButton {

    private final SpawnerPlugin plugin;

    public ShowButton(Plugin plugin) {
        this.plugin = (SpawnerPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryEngine inventory) {
        List<Spawner> spawners = getSpawners(player);
        Pagination<Spawner> pagination = new Pagination<>();
        List<Spawner> paginatedSpawners = pagination.paginate(spawners, this.slots.size(), inventory.getPage());

        for (int index = 0; index != Math.min(this.slots.size(), paginatedSpawners.size()); index++) {

            int slot = this.slots.get(index);
            Spawner spawner = paginatedSpawners.get(index);

            MenuItemStack menuItemStack = this.getItemStack();

            Placeholders placeholders = new Placeholders();
            placeholders.register("type", spawner.getEntityType().name().toLowerCase());
            if (spawner.isPlace()) {
                Location location = spawner.getLocation();
                placeholders.register("location", plugin.getManager().getMessage(Message.SPAWNER_LOCATION.getMessage(), "%world%", location.getWorld().getName(), "%x%", location.getBlockX(), "%y%", location.getBlockY(), "%z%", location.getBlockZ()));
            } else {
                placeholders.register("location", Message.SPAWNER_UNPLACED.getMessage());
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(spawner.getOwner());
            placeholders.register("player", target.getName());
            placeholders.register("status", (spawner.isPlace() ? Message.SPAWNER_REMOVE : Message.SPAWNER_PLACE).getMessage());
            placeholders.register("material", this.plugin.getManager().getEntitiesMaterials().getOrDefault(spawner.getEntityType(), "SPAWNER"));
            placeholders.register("key", spawner.getSpawnerKey());
            placeholders.register("uuid", spawner.getSpawnerId().toString());
            placeholders.register("spawner_type", spawner.getType().name().toLowerCase());

            ItemStack itemStack = menuItemStack.build(player, false, placeholders);
            inventory.addItem(slot, itemStack).setClick(event -> onClick(spawner, player, event, inventory.getPage(), target));
        }
    }

    private void onClick(Spawner spawner, Player player, InventoryClickEvent event, int page, OfflinePlayer offlinePlayer) {

        if (event.isRightClick()) {

            plugin.getManager().removeSpawnerGui(spawner, player, offlinePlayer, page);

        } else if (event.isLeftClick()) {
            if (spawner.isPlace()) {
                player.closeInventory();
                player.teleport(spawner.getLocation());
            }
        }
    }

    @Override
    public int getPaginationSize(Player player) {
        return getSpawners(player).size();
    }

    private List<Spawner> getSpawners(Player player) {
        PlayerSpawner playerSpawner = this.plugin.getManager().getPlayerSpawners().computeIfAbsent(player.getUniqueId(), uuid -> new PlayerSpawner());
        OfflinePlayer offlinePlayer = playerSpawner.getTargetPlayer();
        return offlinePlayer == null ? this.plugin.getStorage().getSpawners() : this.plugin.getStorage().getSpawners(offlinePlayer);
    }
}
