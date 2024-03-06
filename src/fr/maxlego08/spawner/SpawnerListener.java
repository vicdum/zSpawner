package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import fr.maxlego08.spawner.api.utils.SpawnerResult;
import fr.maxlego08.spawner.listener.ListenerAdapter;
import fr.maxlego08.spawner.save.Config;
import fr.maxlego08.spawner.stackable.StackableManager;
import fr.maxlego08.spawner.zcore.enums.Message;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;
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

        Optional<SpawnerResult> optionalSpawner = this.plugin.getManager().getSpawnerResult(itemStack);
        if (!optionalSpawner.isPresent()) return;
        SpawnerResult spawnerResult = optionalSpawner.get();

        SpawnerType spawnerType = spawnerResult.getSpawnerType();
        EntityType entityType = spawnerResult.getEntityType();

        IStorage storage = this.plugin.getStorage();
        StackableManager stackableManager = this.plugin.getStackableManager();

        if (stackableManager.isEnable() && spawnerType == SpawnerType.CLASSIC) {

            Block blockAgainst = event.getBlockAgainst();
            Optional<Spawner> optional = storage.getSpawner(blockAgainst.getLocation());

            if (optional.isPresent()) {
                Spawner spawner = optional.get();

                if (spawner.getType() == spawnerType && spawner.getEntityType() == entityType) {

                    int limit = stackableManager.getLimit(entityType);
                    if (spawner.getAmount() < limit) {

                        List<EntityType> whitelist = stackableManager.getWhitelist();
                        if (!stackableManager.getBlacklist().contains(entityType) || (!whitelist.isEmpty() && whitelist.contains(entityType))) {

                            spawner.setAmount(spawner.getAmount() + 1);
                            event.setCancelled(true);

                            if (itemStack.getAmount() > 0) itemStack.setAmount(itemStack.getAmount() - 1);
                            else player.getInventory().setItem(equipmentSlot, new ItemStack(Material.AIR));

                            return;
                        }
                    }
                }
            }
        }

        Spawner spawner = new ZSpawner(plugin, player.getUniqueId(), spawnerType, entityType);
        spawner.place(block.getLocation());

        storage.addSpawner(spawner);
    }


    @Override
    protected void onBlockBreak(BlockBreakEvent event, Player player) {

        Block block = event.getBlock();

        SpawnerManager manager = this.plugin.getManager();
        PlayerSpawner playerSpawner = manager.getPlayerSpawners().get(player.getUniqueId());
        if (playerSpawner != null && playerSpawner.isPlacingSpawner()) {

            event.setCancelled(true);

            if (manager.getBlacklistMaterials().contains(block.getType())) {
                message(this.plugin, player, Message.PLACE_ERROR_BLACKLIST);
                return;
            }

            // Gérer la limite par chunk

            Spawner spawner = playerSpawner.getPlacingSpawner();
            playerSpawner.placeSpawner();
            spawner.place(block.getLocation());
            message(this.plugin, player, Message.PLACE_SUCCESS);

            return;
        }

        if (block.getType() == Material.SPAWNER) {

            IStorage storage = this.plugin.getStorage();
            StackableManager stackableManager = this.plugin.getStackableManager();

            Optional<Spawner> optional = storage.getSpawner(block.getLocation());
            optional.ifPresent(spawner -> {

                event.setCancelled(true);

                if (spawner.getType() != SpawnerType.CLASSIC) {

                    if (spawner.getOwner().equals(player.getUniqueId())) {

                        if (spawner.getType() == SpawnerType.GUI) {

                            // TODO ajouter une option pour casser le spawner directement

                            if (Config.ownerCanBreakSpawner) {
                                spawner.breakBlock();
                                message(this.plugin, player, Message.BREAK_GUI);
                                return;
                            }

                            message(this.plugin, player, Message.BREAK_ERROR_GUI);
                            return;
                        }

                        if (spawner.getType() == SpawnerType.VIRTUAL) {

                            message(this.plugin, player, Message.BREAK_ERROR_VIRTUAL);
                            return;
                        }
                    }

                    message(this.plugin, player, Message.BREAK_ERROR_OTHER);
                    return;
                }

                block.getWorld().dropItemNaturally(block.getLocation(), this.plugin.getManager().getSpawnerItemStack(player, spawner.getType(), spawner.getEntityType()));

                if (stackableManager.isEnable() && spawner.getAmount() > 1) {
                    spawner.setAmount(spawner.getAmount() - 1);
                    return;
                }

                spawner.disable();
                block.setType(Material.AIR);
                storage.removeSpawner(spawner.getLocation());
            });
        }
    }

    @Override
    protected void onBlockExplode(List<Block> blocks) {

        if (!Config.checkSpawnerExplosion()) return;

        IStorage storage = this.plugin.getStorage();

        blocks.removeIf(block -> {

            Optional<Spawner> optional = storage.getSpawner(block.getLocation());
            if (optional.isPresent()) {
                Spawner spawner = optional.get();
                if (spawner.getType() == SpawnerType.VIRTUAL || Config.spawnerExplosion.get(spawner.getType())) {
                    return true;
                }

                System.out.println(spawner.getType() +" - " + Config.spawnerDrop.getOrDefault(spawner.getType(), false));
                if (Config.spawnerDrop.getOrDefault(spawner.getType(), false)) {

                    spawner.breakBlock();
                    storage.removeSpawner(spawner);

                    block.getWorld().dropItemNaturally(block.getLocation(), this.plugin.getManager().getSpawnerItemStack(null, spawner.getType(), spawner.getEntityType()));
                    return false;
                }
            }

            if (block.getType() == Material.SPAWNER && Config.disableNaturalSpawnerExplosion) {
                return true;
            }

            if (block.getType() == Material.SPAWNER && Config.dropNaturalSpawnerOnExplose) {
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                block.getWorld().dropItemNaturally(block.getLocation(), this.plugin.getManager().getSpawnerItemStack(null, SpawnerType.CLASSIC, spawner.getSpawnedType()));
                return false;
            }

            return false;
        });
    }
}
