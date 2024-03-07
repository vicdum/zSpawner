package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.button.ZButton;
import fr.maxlego08.menu.inventory.inventories.InventoryDefault;
import fr.maxlego08.menu.zcore.utils.inventory.Pagination;
import fr.maxlego08.spawner.SpawnerManager;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerItem;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsButton extends ZButton implements PaginateButton {

    private final SpawnerPlugin plugin;

    public ItemsButton(Plugin plugin) {
        this.plugin = (SpawnerPlugin) plugin;
    }

    @Override
    public boolean hasSpecialRender() {
        return true;
    }

    private List<SpawnerItem> getSpawnerItems(Player player) {
        PlayerSpawner playerSpawner = this.plugin.getManager().getPlayerSpawners().get(player.getUniqueId());
        return playerSpawner != null ? playerSpawner.getVirtualSpawner() != null ? playerSpawner.getVirtualSpawner().getItems() : new ArrayList<>() : new ArrayList<>();
    }

    @Override
    public void onRender(Player player, InventoryDefault inventory) {

        PlayerSpawner playerSpawner = this.plugin.getManager().getPlayerSpawners().get(player.getUniqueId());
        Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner() == null ? null : playerSpawner.getVirtualSpawner();
        if (spawner == null) return;

        List<SpawnerItem> items = getSpawnerItems(player);
        Pagination<SpawnerItem> pagination = new Pagination<>();
        List<SpawnerItem> paginatedItems = pagination.paginate(items, this.slots.size(), inventory.getPage());
        List<String> lore = this.getItemStack().getLore();

        for (int index = 0; index != Math.min(this.slots.size(), paginatedItems.size()); index++) {

            int slot = this.slots.get(index);
            SpawnerItem spawnerItem = paginatedItems.get(index);

            ItemStack itemStack = spawnerItem.getItemStack().clone();
            itemStack.setAmount(1);

            ItemMeta itemMeta = itemStack.getItemMeta();
            plugin.getInventoryManager().getMeta().updateLore(itemMeta, lore.stream().map(string -> {
                string = string.replace("%quantity%", format(spawnerItem.getAmount()));
                return string;
            }).collect(Collectors.toList()), player);
            itemStack.setItemMeta(itemMeta);

            inventory.addItem(slot, itemStack).setClick(event -> onClick(spawnerItem, spawner, player, event));
        }
    }

    private void onClick(SpawnerItem spawnerItem, Spawner spawner, Player player, InventoryClickEvent event) {

        SpawnerManager manager = plugin.getManager();
        if (event.getClick() == ClickType.SHIFT_LEFT) {
            manager.fillInventoryWithLoot(player, spawner, spawnerItem);
        } else if (event.isRightClick()) {
            manager.removeStackLoot(player, spawner, spawnerItem, 1);
        } else if (event.isLeftClick()) {
            manager.removeStackLoot(player, spawner, spawnerItem, 64);
        }

        manager.openVirtualSpawner(player, spawner);
    }

    @Override
    public boolean isPermanent() {
        return true;
    }

    @Override
    public int getPaginationSize(Player player) {
        List<SpawnerItem> items = getSpawnerItems(player);
        return items.size();
    }
}
