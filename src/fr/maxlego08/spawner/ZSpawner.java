package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerItem;
import fr.maxlego08.spawner.api.SpawnerLevel;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.save.Config;
import fr.maxlego08.spawner.stackable.StackableManager;
import fr.maxlego08.spawner.zcore.utils.Cuboid;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ZSpawner extends ZUtils implements Spawner {

    private final SpawnerPlugin plugin;
    private final UUID uniqueId;
    private final UUID ownerId;
    private final SpawnerType spawnerType;
    private final EntityType entityType;
    private final BlockFace blockFace;
    private final SpawnerLevel spawnerLevel;
    private final List<Entity> deadEntities = new ArrayList<>();
    private long placedAt;
    private Location location;
    private boolean needUpdate;
    private int amount;
    private ArmorStand stackArmorstand;
    private LivingEntity livingEntity;
    private long lastSpawnAt;
    private Cuboid cuboid;
    private List<SpawnerItem> items = new ArrayList<>();

    public ZSpawner(SpawnerPlugin plugin, UUID uniqueId, UUID ownerId, SpawnerType spawnerType, EntityType entityType, long placedAt, SpawnerLevel spawnerLevel, Location location, int amount, BlockFace blockFace) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
        this.ownerId = ownerId;
        this.spawnerType = spawnerType;
        this.entityType = entityType;
        this.placedAt = placedAt;
        this.spawnerLevel = spawnerLevel;
        this.location = location;
        this.amount = amount;
        this.blockFace = blockFace;
    }

    public ZSpawner(SpawnerPlugin plugin, UUID ownerId, SpawnerType spawnerType, EntityType entityType, BlockFace blockFace) {
        this.plugin = plugin;
        this.ownerId = ownerId;
        this.uniqueId = UUID.randomUUID();
        this.spawnerType = spawnerType;
        this.entityType = entityType;
        this.amount = 1;
        this.location = null;
        this.placedAt = 0;
        this.blockFace = blockFace;
        this.spawnerLevel = plugin.getManager().getSpawnerLevel("default");
    }

    @Override
    public UUID getOwner() {
        return this.ownerId;
    }

    @Override
    public UUID getSpawnerId() {
        return this.uniqueId;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public SpawnerType getType() {
        return this.spawnerType;
    }

    @Override
    public long getPlacedAt() {
        return this.placedAt;
    }

    @Override
    public SpawnerLevel getLevel() {
        return this.spawnerLevel;
    }

    @Override
    public EntityType getEntityType() {
        return this.entityType;
    }

    @Override
    public boolean isPlace() {
        return this.location != null;
    }

    @Override
    public boolean needUpdate() {
        return this.needUpdate;
    }

    @Override
    public boolean sameChunk(int x, int z) {
        if (!this.isPlace()) return false;
        Chunk chunk = this.location.getChunk();
        return chunk.getX() == x || chunk.getZ() == z;
    }

    @Override
    public void update() {
        this.needUpdate = false;
    }

    @Override
    public void place(Location location) {

        this.placedAt = System.currentTimeMillis();
        this.location = location;
        Block block = location.getBlock();

        if (this.spawnerType == SpawnerType.VIRTUAL) {

            block.setType(Config.virtualMaterial, true);
            spawnEntity();
        } else {

            block.setType(Material.SPAWNER, true);

            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            spawner.setSpawnedType(this.entityType);
            spawner.update(true);
        }

        this.needUpdate = true;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
        this.needUpdate = true;

        if (amount <= 1) disable();
        else this.spawnHologram();

        this.updateHologram();
    }

    @Override
    public void updateSpawner() {

        if (!isPlace()) return;

        Block block = this.location.getBlock();

        if (this.spawnerType == SpawnerType.VIRTUAL) {

            this.spawnEntity();
        } else {

            CreatureSpawner spawner = (CreatureSpawner) block.getState();

            StackableManager stackableManager = this.plugin.getStackableManager();
            if (stackableManager.isEnable()) {
                stackableManager.updateSpawner(spawner, this.amount);
                this.spawnHologram();
            }

            spawner.update(true);
        }
    }

    @Override
    public void load() {

        if (!isPlace()) return;

        Block block = this.location.getBlock();

        if (this.spawnerType == SpawnerType.VIRTUAL) {

            spawnEntity();
            if (block.getType() != Config.virtualMaterial) {
                block.setType(Config.virtualMaterial);
            }
        } else {
            spawnHologram();
            if (block.getType() != Material.SPAWNER) {
                block.setType(Material.SPAWNER, true);
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                spawner.setSpawnedType(this.entityType);
                spawner.update(true);
                this.updateSpawner();
            }
        }
    }

    private Location getSpawnedEntityLocation() {
        Location location = this.location.clone().add(0.5, 1, 0.5);
        if (this.blockFace.equals(BlockFace.SOUTH)) location.setYaw(180.f);
        if (this.blockFace.equals(BlockFace.WEST)) location.setYaw(-90.f);
        if (this.blockFace.equals(BlockFace.EAST)) location.setYaw(90.f);
        return location;
    }

    private void spawnEntity() {

        if (this.livingEntity != null) {
            this.updateEntity();
            return;
        }

        Location location = getSpawnedEntityLocation();

        World world = location.getWorld();
        this.livingEntity = (LivingEntity) world.spawnEntity(location, this.entityType);
        this.livingEntity.setAI(false);
        this.livingEntity.setCollidable(false);
        this.livingEntity.setCustomNameVisible(true);
        this.livingEntity.setVisualFire(false);
        this.livingEntity.setMetadata("zspawner", new FixedMetadataValue(this.plugin, true));

        if (this.livingEntity instanceof Ageable) {
            Ageable ageable = (Ageable) this.livingEntity;
            ageable.setAdult();
        }

        if (this.livingEntity instanceof ZombieVillager) {
            this.livingEntity.remove();
            this.livingEntity = null;
            this.spawnEntity();
            return;
        }

        this.updateEntity();
    }

    private void spawnHologram() {

        if (this.stackArmorstand != null) {
            this.updateHologram();
            return;
        }

        StackableManager stackableManager = this.plugin.getStackableManager();
        if (this.amount > 1 && stackableManager.isEnable()) {
            Location spawnLocation = this.location.clone().add(0.5, 1.0, 0.5);
            this.stackArmorstand = location.getWorld().spawn(spawnLocation, ArmorStand.class, armorStand -> {
                armorStand.setInvisible(true);
                armorStand.setGravity(false);
                armorStand.setMarker(true);
                armorStand.setCustomNameVisible(true);
                armorStand.setCustomName("");
            });
            this.updateHologram();
        }
    }

    private void updateHologram() {
        StackableManager stackableManager = this.plugin.getStackableManager();
        if (this.stackArmorstand == null || !stackableManager.isEnable()) return;

        if (this.stackArmorstand != null) {
            this.stackArmorstand.setCustomName(color(getMessage(stackableManager.getHologram(), "%amount%", this.amount, "%entity%", name(this.entityType.name()))));
        }
    }

    private void updateEntity() {
        if (this.livingEntity != null) {
            this.livingEntity.setCustomName(color(getMessage(Config.virtualName, "%amount%", this.amount)));
        }
    }

    @Override
    public void disable() {
        if (this.stackArmorstand != null) stackArmorstand.remove();
        if (this.livingEntity != null) livingEntity.remove();
    }

    @Override
    public void breakBlock() {
        if (!this.isPlace()) return;

        this.location.getBlock().setType(Material.AIR);
        this.location = null;
        this.cuboid = null;
        this.placedAt = 0;

        if (this.stackArmorstand != null) stackArmorstand.remove();
        if (this.livingEntity != null) livingEntity.remove();

        this.needUpdate = true;
    }

    @Override
    public int comparePlace() {
        return isPlace() ? 1 : 0;
    }

    @Override
    public int compareNotPlace() {
        return isPlace() ? 0 : 1;
    }

    @Override
    public LivingEntity getLivingEntity() {
        return this.livingEntity;
    }

    @Override
    public List<Entity> getDeadEntities() {
        return this.deadEntities;
    }

    @Override
    public void entityDeath() {

        this.amount -= 1;
        this.needUpdate = true;
        this.updateEntity();

        if (this.livingEntity != null && this.livingEntity.isValid() && ((this.livingEntity.getLocation().getBlockX() != this.location.getBlockX() || this.livingEntity.getLocation().getBlockZ() != this.location.getBlockZ()))) {
            this.livingEntity.teleport(getSpawnedEntityLocation());
        }
    }

    @Override
    public void addItems(List<ItemStack> itemStacks) {

        itemStacks.forEach(itemStack -> {
            Optional<SpawnerItem> optional = getSpawnerItem(itemStack);
            if (optional.isPresent()) {
                SpawnerItem spawnerItem = optional.get();
                spawnerItem.addAmount(itemStack.getAmount());
            } else {
                SpawnerItem spawnerItem = new ZSpawnerItem(itemStack, itemStack.getAmount());
                this.items.add(spawnerItem);
            }
        });

        this.needUpdate = true;
    }

    @Override
    public boolean isChunkLoaded() {
        World world = this.location.getWorld();
        return world.isChunkLoaded(this.location.getChunk());
    }

    @Override
    public double getDistance() {
        // ToDo
        return 16.0;
    }

    @Override
    public void tick() {

        if (this.livingEntity == null || !this.livingEntity.isValid() || this.livingEntity.isDead()) {
            this.spawnEntity();
        }

        // ToDo, utiliser le système de niveau pour ne pas avoir les valeurs en static

        if (System.currentTimeMillis() > this.lastSpawnAt && this.amount < 10000) {

            long ms = ThreadLocalRandom.current().nextLong(5000, 10000); // Entre 5 et 10 secondes
            this.lastSpawnAt = System.currentTimeMillis() + ms;

            this.amount += getNumberBetween(1, 3);
            this.needUpdate = true;
            this.updateEntity();
        }
    }

    @Override
    public BlockFace getBlockFace() {
        return this.blockFace;
    }

    @Override
    public Cuboid getCuboid() {
        if (this.cuboid != null) return this.cuboid;
        Location maxLocation = this.spawnerType == SpawnerType.VIRTUAL ? this.location.clone().add(0, this.livingEntity == null ? 2 : Math.ceil(this.livingEntity.getHeight()), 0) : this.location.clone();
        return this.cuboid = new Cuboid(this.location.clone(), maxLocation);
    }

    @Override
    public List<SpawnerItem> getItems() {
        return this.items;
    }

    @Override
    public void setItems(List<SpawnerItem> items) {
        this.items = items;
    }

    @Override
    public Optional<SpawnerItem> getSpawnerItem(ItemStack itemStack) {
        return this.items.stream().filter(spawnerItem -> spawnerItem.isSimilar(itemStack)).findFirst();
    }
}
