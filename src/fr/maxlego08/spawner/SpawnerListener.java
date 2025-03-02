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
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.logger.Logger;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SpawnerListener extends ListenerAdapter {

    private final SpawnerPlugin plugin;

    public SpawnerListener(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    private boolean checkBlockPlaceVirtualSpawner(BlockPlaceEvent event, Block block) {
        Optional<Spawner> optional = this.plugin.getStorage().getSpawner(block.getLocation(), SpawnerType.VIRTUAL);
        if (optional.isPresent()) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    @Override
    protected void onBlockPlace(BlockPlaceEvent event, Player player) {

        ItemStack itemStack = event.getItemInHand();
        EquipmentSlot equipmentSlot = event.getHand();
        Block block = event.getBlock();

        if (checkBlockPlaceVirtualSpawner(event, block)) return;

        Optional<SpawnerResult> optionalSpawner = this.plugin.getManager().getSpawnerResult(itemStack);
        if (optionalSpawner.isEmpty()) return;
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

        if (Config.enableLimit) {

            Chunk chunk = block.getChunk();
            if (hasSpawnerLimit(event, player, entityType, chunk)) return;
        }


        BlockFace blockFace = getCardinalDirection(player);
        Spawner spawner = new ZSpawner(plugin, spawnerResult.getSpawnerId(), player.getUniqueId(), spawnerType, entityType, blockFace);
        spawner.place(block.getLocation());

        storage.addSpawner(spawner);
    }


    @Override
    protected void onBlockBreak(BlockBreakEvent event, Player player) {

        Block block = event.getBlock();

        SpawnerManager manager = this.plugin.getManager();
        PlayerSpawner playerSpawner = manager.getPlayerSpawners().get(player.getUniqueId());
        if (playerSpawner != null && playerSpawner.isPlacingSpawner()) {
            placeSpawner(player, playerSpawner, event, manager, block);
            return;
        }

        if (block.getType() == Material.SPAWNER || block.getType() == Config.virtualMaterial) {

            IStorage storage = this.plugin.getStorage();
            StackableManager stackableManager = this.plugin.getStackableManager();

            Optional<Spawner> optional = storage.getSpawner(block.getLocation());
            if (optional.isPresent()) {
                Spawner spawner = optional.get();

                event.setCancelled(true);

                if (spawner.getType() != SpawnerType.CLASSIC) {
                    breakOther(player, spawner);
                    return;
                }

                if (Config.enableSilkSpawner) {
                    if (cantSilkSpawner(player)) {
                        storage.removeSpawner(spawner.getLocation());
                        event.setCancelled(false);
                        return;
                    }
                }

                block.getWorld().dropItemNaturally(block.getLocation(), this.plugin.getManager().getSpawnerItemStack(player, spawner.getType(), spawner.getEntityType(), spawner));

                if (stackableManager.isEnable() && spawner.getAmount() > 1) {
                    spawner.setAmount(spawner.getAmount() - 1);
                    return;
                }

                spawner.disable();
                block.setType(Material.AIR);
                storage.removeSpawner(spawner.getLocation());

                return;
            }

            // On est dans le cas d'un spawner naturel
            if (block.getType() == Material.SPAWNER && Config.enableSilkSpawner && block.getState() instanceof CreatureSpawner spawner && Config.silkNaturalSpawner) {

                if (cantSilkSpawner(player)) return;

                block.getWorld().dropItemNaturally(block.getLocation(), this.plugin.getManager().getSpawnerItemStack(player, Config.naturelSpawnerInto, spawner.getSpawnedType(), null));
            }
        }
    }

    private boolean cantSilkSpawner(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (!Config.whitelistMaterialSilkSpawner.contains(itemStack.getType())) {
            return true;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        return Config.needSilkTouchEnchant && !itemMeta.hasEnchant(Enchantment.SILK_TOUCH);
    }

    private void breakOther(Player player, Spawner spawner) {
        if (spawner.getOwner().equals(player.getUniqueId())) {

            if (spawner.getType() == SpawnerType.GUI) {

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
    }

    private void placeSpawner(Player player, PlayerSpawner playerSpawner, BlockBreakEvent event, SpawnerManager manager, Block block) {

        event.setCancelled(true);

        if (manager.getBlacklistMaterials().contains(block.getType())) {
            message(this.plugin, player, Message.PLACE_ERROR_BLACKLIST);
            return;
        }

        Spawner spawner = playerSpawner.getPlacingSpawner();

        if (Config.enableLimit) {

            Chunk chunk = block.getChunk();
            if (hasSpawnerLimit(event, player, spawner.getEntityType(), chunk)) return;
        }

        playerSpawner.placeSpawner();
        spawner.place(block.getLocation());
        message(this.plugin, player, Message.PLACE_SUCCESS);
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

                if (Config.spawnerDrop.getOrDefault(spawner.getType(), false)) {

                    spawner.breakBlock();
                    storage.removeSpawner(spawner);

                    block.getWorld().dropItemNaturally(block.getLocation(), this.plugin.getManager().getSpawnerItemStack(null, spawner.getType(), spawner.getEntityType(), spawner));
                    return false;
                }
            }

            if (block.getType() == Material.SPAWNER && Config.disableNaturalSpawnerExplosion) {
                return true;
            }

            if (block.getType() == Material.SPAWNER && Config.dropNaturalSpawnerOnExplose) {
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                block.getWorld().dropItemNaturally(block.getLocation(), this.plugin.getManager().getSpawnerItemStack(null, SpawnerType.CLASSIC, spawner.getSpawnedType(), null));
                return false;
            }

            return false;
        });
    }

    @Override
    protected void onPower(CreeperPowerEvent event, CreeperPowerEvent.PowerCause cause, Creeper entity, LightningStrike lightning) {

        IStorage storage = this.plugin.getStorage();
        storage.getSpawnerByEntity(entity).ifPresent(spawner -> event.setCancelled(true));
    }

    @Override
    protected void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event, Player player, Entity rightClicked) {

        IStorage storage = this.plugin.getStorage();
        if (rightClicked instanceof LivingEntity) {
            if (!Objects.equals(event.getHand(), EquipmentSlot.HAND)) return;
            storage.getSpawnerByEntity((LivingEntity) rightClicked).ifPresent(spawner -> {
                event.setCancelled(true);
                this.openVirtualSpawner(spawner, player, event);
            });
        }
    }

    @Override
    protected void onTeleport(EntityTeleportEvent event, Entity entity) {
        if (entity.hasMetadata("zspawner")) event.setCancelled(true);
    }

    @Override
    protected void onEntityDamage(EntityDamageEvent event, LivingEntity entity, EntityDamageEvent.DamageCause cause, double finalDamage) {

        IStorage storage = this.plugin.getStorage();
        storage.getSpawnerByEntity(entity).ifPresent(spawner -> {

            if (spawner.getAmount() <= 0 || cause == EntityDamageEvent.DamageCause.SUFFOCATION || cause == EntityDamageEvent.DamageCause.LAVA || cause == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || cause == EntityDamageEvent.DamageCause.DRYOUT || cause == EntityDamageEvent.DamageCause.POISON || cause == EntityDamageEvent.DamageCause.MAGIC || cause == EntityDamageEvent.DamageCause.DROWNING || cause == EntityDamageEvent.DamageCause.FALLING_BLOCK) {
                event.setCancelled(true);
                return;
            }

            if (event instanceof EntityDamageByEntityEvent damageByEntityEvent) {

                Entity damager = damageByEntityEvent.getDamager();

                if (damager instanceof Wolf || damager instanceof IronGolem) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (entity.getHealth() - finalDamage <= 0) {

                event.setDamage(0);
                entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

                Class<? extends Entity> entityClass = spawner.getEntityType().getEntityClass();

                if (entityClass == null) {
                    Logger.info("Error with entity class for " + spawner.getEntityType(), Logger.LogType.ERROR);
                    return;
                }

                LivingEntity clonedEntity = (LivingEntity) entity.getWorld().spawn(entity.getLocation(), entityClass);
                clonedEntity.setAI(false);
                clonedEntity.setVisualFire(false);
                spawner.getDeadEntities().add(clonedEntity);

                if (event instanceof EntityDamageByEntityEvent damageByEntityEvent) {

                    Entity damager = damageByEntityEvent.getDamager();

                    if (damager instanceof Wolf || damager instanceof IronGolem) {
                        return;
                    }

                    if (damager instanceof Player) clonedEntity.setKiller((Player) damager);
                    clonedEntity.damage(entity.getHealth() * 2, damager);

                } else {

                    clonedEntity.damage(entity.getHealth() * 2);
                }

                spawner.entityDeath();
            }
        });
    }

    @Override
    protected void onEntityDeath(EntityDeathEvent event, Entity entity) {

        IStorage storage = this.plugin.getStorage();
        storage.getSpawnerByDeadEntity(entity).ifPresent(spawner -> {

            spawner.getDeadEntities().remove(entity);
            List<ItemStack> itemStacks = new ArrayList<>(event.getDrops());

            /*if (spawner.isEnableAutoSell()) {

                ZPlayer player = this.plugin.getZPlayer(Bukkit.getOfflinePlayer(spawner.getUniqueId()));
                Iterator<ItemStack> iterator = itemStacks.iterator();
                while (iterator.hasNext()) {
                    ItemStack itemStack = iterator.next();
                    Optional<ShopItem> optional2 = this.plugin.getShopManager().getItem(itemStack);
                    if (optional2.isPresent()) {

                        ShopItem item = optional2.get();
                        if (item.getSellPrice(player) > 0) {

                            double price = itemStack.getAmount() * item.getSellPrice(player);
                            player.deposit(price);

                            iterator.remove();

                        }

                    }
                }
            }*/

            if (!itemStacks.isEmpty()) {
                spawner.addItems(itemStacks);
            }

            event.getDrops().clear();

        });
    }

    @Override
    protected void onCombust(EntityCombustEvent event, Entity entity) {

        IStorage storage = this.plugin.getStorage();
        if (entity instanceof LivingEntity) {
            storage.getSpawnerByEntity((LivingEntity) entity).ifPresent(spawner -> event.setCancelled(true));
        }
    }

    @Override
    public void onPistonExtend(BlockPistonExtendEvent event, Block block, List<Block> blocks) {

        IStorage storage = this.plugin.getStorage();
        Block finalBlock = null;
        for (Block b : blocks) {

            Optional<Spawner> optional = storage.getSpawner(b.getLocation());
            if (optional.isPresent()) {
                event.setCancelled(true);
                return;
            }
            finalBlock = b;

        }

        Directional directional = (Directional) block.getBlockData();
        BlockFace blockFace = directional.getFacing();

        if (finalBlock != null) {

            finalBlock = finalBlock.getRelative(blockFace);
            Optional<Spawner> optional = storage.getSpawner(finalBlock.getLocation());
            if (optional.isPresent()) {
                event.setCancelled(true);
                return;
            }

        }

        block = block.getRelative(blockFace);

        Optional<Spawner> optional = storage.getSpawner(block.getLocation());
        if (optional.isPresent()) event.setCancelled(true);

    }

    @Override
    protected void onChunkLoad(ChunkLoadEvent event, Chunk chunk, World world) {
        IStorage storage = this.plugin.getStorage();
        storage.getSpawners(SpawnerType.VIRTUAL).stream().filter(spawner -> spawner.sameChunk(chunk.getX(), chunk.getZ())).forEach(Spawner::load);
    }

    @Override
    protected void onChunkUnLoad(ChunkUnloadEvent event, Chunk chunk, World world) {
        IStorage storage = this.plugin.getStorage();
        storage.getSpawners(SpawnerType.VIRTUAL).stream().filter(spawner -> spawner.sameChunk(chunk.getX(), chunk.getZ())).forEach(Spawner::disable);
    }

    @Override
    protected void onInteract(PlayerInteractEvent event, Player player) {

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            Block block = event.getClickedBlock();
            if (block == null) return;

            if (!Objects.equals(event.getHand(), EquipmentSlot.HAND)) return;

            IStorage storage = this.plugin.getStorage();
            storage.getSpawner(block.getLocation()).ifPresent(spawner -> {

                if (spawner.getType() == SpawnerType.VIRTUAL && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (plugin.getUpgradeManager().applyUpgradeItem(spawner, player, event.getItem())) {
                        event.setCancelled(true);
                        return;
                    }
                }

                this.openVirtualSpawner(spawner, player, event);
            });
        }
    }

    private void openVirtualSpawner(Spawner spawner, Player player, Cancellable event) {
        if (spawner.getType() == SpawnerType.VIRTUAL) {

            event.setCancelled(true);

            if (spawner.getOwner().equals(player.getUniqueId()) || hasPermission(player, Permission.ZSPAWNER_BYPASS)) {
                this.plugin.getManager().openVirtualSpawner(player, spawner, 1);
            }
        }
    }

    private boolean hasSpawnerLimit(Cancellable event, Player player, EntityType entityType, Chunk chunk) {

        IStorage storage = this.plugin.getStorage();

        if (Config.entityLimits.containsKey(entityType)) {

            int entityLimit = Config.entityLimits.get(entityType);
            long amount = storage.countSpawners(chunk.getX(), chunk.getZ(), entityType);
            if (amount >= entityLimit) {
                event.setCancelled(true);
                message(this.plugin, player, Message.LIMIT_ENTITY, "%amount%", entityLimit, "%type%", name(entityType.name()));
                return true;
            }
        } else {

            int entityLimit = Config.globalLimit;
            long amount = storage.countSpawners(chunk.getX(), chunk.getZ());
            if (amount >= entityLimit) {
                event.setCancelled(true);
                message(this.plugin, player, Message.LIMIT_GLOBAL, "%amount%", entityLimit);
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onQuit(PlayerQuitEvent event, Player player) {
        this.plugin.getManager().getPlayerSpawners().remove(player.getUniqueId());
    }
}
