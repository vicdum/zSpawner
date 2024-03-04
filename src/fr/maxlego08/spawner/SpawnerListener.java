package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.listener.ListenerAdapter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

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

        if (block.getType() == Material.SPAWNER) {

            Spawner spawner = new ZSpawner(UUID.randomUUID(), player.getUniqueId(), SpawnerType.CLASSIC, EntityType.SKELETON, System.currentTimeMillis(), this.plugin.getManager().getSpawnerLevel("TODO"), block.getLocation());
            spawner.place(block.getLocation());

            this.plugin.getStorage().placeSpawner(block.getLocation(), spawner);
        }
    }
}
