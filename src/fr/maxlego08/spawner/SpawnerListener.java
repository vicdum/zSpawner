package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.listener.ListenerAdapter;
import fr.maxlego08.spawner.stackable.StackableManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class SpawnerListener extends ListenerAdapter {

    private final SpawnerPlugin plugin;

    public SpawnerListener(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void onBlockPlace(BlockPlaceEvent event, Player player) {

        ItemStack itemStack = event.getItemInHand();
        EquipmentSlot equipmentSlot = event.getHand();
        Block block = event.getBlock();

        if (itemStack.getType() == Material.SPAWNER) {

            IStorage storage = this.plugin.getStorage();
            StackableManager stackableManager = this.plugin.getStackableManager();

            if (stackableManager.isEnable()) {

                Block blockAgainst = event.getBlockAgainst();
                Optional<Spawner> optional = storage.getSpawner(blockAgainst.getLocation());

                if (optional.isPresent()) {
                    Spawner spawner = optional.get();

                    int limit = stackableManager.getLimit(EntityType.SKELETON);
                    if (spawner.getAmount() < limit) {

                        // ToDo - Blacklist / Whitelist

                        spawner.setAmount(spawner.getAmount() + 1);
                        event.setCancelled(true);

                        if (itemStack.getAmount() > 0) itemStack.setAmount(itemStack.getAmount() - 1);
                        else player.getInventory().setItem(equipmentSlot, new ItemStack(Material.AIR));

                        return;
                    }
                }
            }

            Spawner spawner = new ZSpawner(plugin, player.getUniqueId(), SpawnerType.CLASSIC, EntityType.SKELETON);
            spawner.place(block.getLocation());

            storage.placeSpawner(block.getLocation(), spawner);
        }
    }

    @Override
    protected void onBlockBreak(BlockBreakEvent event, Player player) {

        Block block = event.getBlock();
        if (block.getType() == Material.SPAWNER) {

            IStorage storage = this.plugin.getStorage();
            StackableManager stackableManager = this.plugin.getStackableManager();

            Optional<Spawner> optional = storage.getSpawner(block.getLocation());
            optional.ifPresent(spawner -> {

                event.setCancelled(true);
                if (stackableManager.isEnable() && spawner.getAmount() > 1) {

                    // ToDo drop du bon type de spawner
                    spawner.setAmount(spawner.getAmount() - 1);
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SPAWNER));
                    return;
                }

                spawner.disable();
                block.setType(Material.AIR);
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.SPAWNER));
                storage.removeSpawner(spawner.getLocation());
            });
        }
    }
}
