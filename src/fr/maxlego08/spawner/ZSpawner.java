package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerLevel;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.stackable.StackableManager;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class ZSpawner extends ZUtils implements Spawner {

    private final SpawnerPlugin plugin;
    private final UUID uniqueId;
    private final UUID ownerId;
    private final SpawnerType spawnerType;
    private final EntityType entityType;
    private final SpawnerLevel spawnerLevel;
    private long placedAt;
    private Location location;
    private boolean needUpdate;
    private int amount;
    private ArmorStand stackArmorstand;

    public ZSpawner(SpawnerPlugin plugin, UUID uniqueId, UUID ownerId, SpawnerType spawnerType, EntityType entityType, long placedAt, SpawnerLevel spawnerLevel, Location location, int amount) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
        this.ownerId = ownerId;
        this.spawnerType = spawnerType;
        this.entityType = entityType;
        this.placedAt = placedAt;
        this.spawnerLevel = spawnerLevel;
        this.location = location;
        this.amount = amount;
    }

    public ZSpawner(SpawnerPlugin plugin, UUID ownerId, SpawnerType spawnerType, EntityType entityType) {
        this.plugin = plugin;
        this.ownerId = ownerId;
        this.uniqueId = UUID.randomUUID();
        this.spawnerType = spawnerType;
        this.entityType = entityType;
        this.amount = 1;
        this.location = null;
        this.placedAt = 0;
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
        block.setType(Material.SPAWNER, true);

        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        spawner.setSpawnedType(this.entityType);
        spawner.update(true);

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
        CreatureSpawner spawner = (CreatureSpawner) block.getState();

        StackableManager stackableManager = this.plugin.getStackableManager();
        if (stackableManager.isEnable()) {
            stackableManager.updateSpawner(spawner, this.amount);
            this.spawnHologram();
        }

        spawner.update(true);
    }

    @Override
    public void load() {

        if (!isPlace()) return;

        spawnHologram();
        Block block = this.location.getBlock();
        if (block.getType() != Material.SPAWNER) {
            block.setType(Material.SPAWNER, true);
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            spawner.setSpawnedType(this.entityType);
            spawner.update(true);
            this.updateSpawner();
        }
    }

    public void spawnHologram() {

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

    public void updateHologram() {
        StackableManager stackableManager = this.plugin.getStackableManager();
        if (this.stackArmorstand == null || !stackableManager.isEnable()) return;

        this.stackArmorstand.setCustomName(color(getMessage(stackableManager.getHologram(), "%amount%", this.amount, "%entity%", name(this.entityType.name()))));
    }

    @Override
    public void disable() {
        if (this.stackArmorstand != null) stackArmorstand.remove();
    }

    @Override
    public void breakBlock() {
        if (!this.isPlace()) return;

        this.location.getBlock().setType(Material.AIR);
        this.location = null;
        this.placedAt = 0;

        if (this.stackArmorstand != null) stackArmorstand.remove();

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
}
