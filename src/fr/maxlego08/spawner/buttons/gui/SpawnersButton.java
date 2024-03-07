package fr.maxlego08.spawner.buttons.gui;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.menu.zcore.utils.inventory.Pagination;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.api.enums.Sort;
import fr.maxlego08.spawner.zcore.enums.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.stream.Collectors;

public class SpawnersButton extends ZButton implements PaginateButton {

    private final SpawnerPlugin plugin;

    public SpawnersButton(Plugin plugin) {
        this.plugin = (SpawnerPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    @Override
    public void onRender(Player player, InventoryDefault inventory) {
        Sort sort = this.plugin.getManager().getPlayerSort(player);
        List<Spawner> spawners = this.plugin.getStorage().getSpawners(player, SpawnerType.GUI).stream().sorted(sort.getComparator()).collect(Collectors.toList());
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
                placeholders.register("location", getMessage(Message.SPAWNER_LOCATION.getMessage(), "%world%", location.getWorld().getName(), "%x%", location.getBlockX(), "%y%", location.getBlockY(), "%z%", location.getBlockZ()));
            } else {
                placeholders.register("location", Message.SPAWNER_UNPLACED.getMessage());
            }
            placeholders.register("status", (spawner.isPlace() ? Message.SPAWNER_REMOVE : Message.SPAWNER_PLACE).getMessage());
            placeholders.register("material", this.plugin.getManager().getEntitiesMaterials().getOrDefault(spawner.getEntityType(), "SPAWNER"));

            ItemStack itemStack = menuItemStack.build(player, false, placeholders);
            inventory.addItem(slot, itemStack).setClick(event -> onClick(spawner, player, event, inventory.getPage()));
        }
    }

    private void onClick(Spawner spawner, Player player, InventoryClickEvent event, int page) {

        if (spawner.isPlace()) {
            this.plugin.getManager().breakSpawner(player, spawner, page);
            return;
        }

        this.plugin.getManager().startPlacement(player, spawner);
    }

    @Override
    public int getPaginationSize(Player player) {
        return this.plugin.getStorage().getSpawners(player).size();
    }
}
