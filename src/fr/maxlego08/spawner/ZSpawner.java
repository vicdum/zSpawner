package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerLevel;
import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public class ZSpawner implements Spawner {

    private final UUID uniqueId;
    private final UUID ownerId;
    private final SpawnerType spawnerType;
    private final EntityType entityType;
    private long placedAt;
    private SpawnerLevel spawnerLevel;
    private Location location;
    private boolean needUpdate;

    public ZSpawner(UUID uniqueId, UUID ownerId, SpawnerType spawnerType, EntityType entityType, long placedAt, SpawnerLevel spawnerLevel, Location location) {
        this.uniqueId = uniqueId;
        this.ownerId = ownerId;
        this.spawnerType = spawnerType;
        this.entityType = entityType;
        this.placedAt = placedAt;
        this.spawnerLevel = spawnerLevel;
        this.location = location;
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
        Chunk chunk = this.location.getChunk();
        return chunk.getX() == x || chunk.getZ() == z;
    }

    @Override
    public void update() {
        this.needUpdate = false;
    }

    @Override
    public void place(Location location) {

        this.location = location;
        Block block = location.getBlock();
        block.setType(Material.SPAWNER, true);

        CreatureSpawner spawner = (CreatureSpawner) block.getState();
        spawner.setSpawnedType(this.entityType);
        spawner.update(true);

        this.needUpdate = true;
    }
}
