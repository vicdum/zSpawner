package fr.maxlego08.spawner.listener;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Slime;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("deprecation")
public abstract class ListenerAdapter extends ZUtils {

    protected void onConnect(PlayerJoinEvent event, Player player) {
    }

    protected void onQuit(PlayerQuitEvent event, Player player) {
    }

    protected void onMove(PlayerMoveEvent event, Player player) {
    }

    protected void onInventoryClick(InventoryClickEvent event, Player player) {
    }

    protected void onInventoryClose(InventoryCloseEvent event, Player player) {
    }

    protected void onInventoryDrag(InventoryDragEvent event, Player player) {
    }

    protected void onBlockBreak(BlockBreakEvent event, Player player) {
    }

    protected void onBlockPlace(BlockPlaceEvent event, Player player) {
    }

    protected void onEntityDeath(EntityDeathEvent event, Entity entity) {
    }

    protected void onInteract(PlayerInteractEvent event, Player player) {
    }

    protected void onPlayerTalk(AsyncPlayerChatEvent event, String message) {
    }

    protected void onCraftItem(CraftItemEvent event) {
    }

    protected void onCommand(PlayerCommandPreprocessEvent event, Player player, String message) {
    }

    protected void onGamemodeChange(PlayerGameModeChangeEvent event, Player player) {
    }

    protected void onDrop(PlayerDropItemEvent event, Player player) {
    }

    protected void onPickUp(PlayerPickupItemEvent event, Player player) {
    }

    protected void onMobSpawn(CreatureSpawnEvent event) {
    }

    protected void onDamageByEntity(EntityDamageByEntityEvent event, DamageCause cause, double damage, LivingEntity damager, LivingEntity entity) {
    }

    protected void onPlayerDamagaByPlayer(EntityDamageByEntityEvent event, DamageCause cause, double damage, Player damager, Player entity) {
    }

    protected void onPlayerDamagaByArrow(EntityDamageByEntityEvent event, DamageCause cause, double damage, Projectile damager, Player entity) {
    }

    protected void onItemisOnGround(PlayerDropItemEvent event, Player player, Item item, Location location) {
    }

    protected void onItemMove(PlayerDropItemEvent event, Player player, Item item, Location location, Block block) {
    }

    protected void onPlayerWalk(PlayerMoveEvent event, Player player, int i) {
    }

    protected void onBlockExplode(List<Block> blocks) {

    }

    protected void onPower(CreeperPowerEvent event, CreeperPowerEvent.PowerCause cause, Creeper entity, LightningStrike lightning) {

    }

    protected void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event, Player player, Entity rightClicked) {
    }

    protected void onCombust(EntityCombustEvent event, Entity entity) {

    }

    protected void onEntityDamage(EntityDamageEvent event, LivingEntity entity, DamageCause cause, double finalDamage) {

    }

    protected void onEntityDamageByPlayer(EntityDamageByEntityEvent event, Player damager, LivingEntity entity) {

    }

    protected void onPistonExtend(BlockPistonExtendEvent event, Block block, List<Block> blocks) {

    }

    protected void onPistonRetract(BlockPistonRetractEvent event, Block block, List<Block> blocks) {

    }

    protected void onChunkLoad(ChunkLoadEvent event, Chunk chunk, World world) {

    }

    protected void onChunkUnLoad(ChunkUnloadEvent event, Chunk chunk, World world) {

    }

    protected void onTeleport(EntityTeleportEvent event, Entity entity) {

    }

    public void onKnockBack(EntityKnockbackByEntityEvent event, LivingEntity entity) {

    }

    protected void onSlimeSplit(SlimeSplitEvent event, Slime entity) {

    }

    public void onEntityDrop(EntityDropItemEvent event, Entity entity, Item itemDrop) {

    }
}
