package fr.maxlego08.spawner.api;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.UUID;

public interface Spawner {

    UUID getOwner();

    UUID getSpawnerId();

    Location getLocation();

    SpawnerType getType();

    long getPlacedAt();

    SpawnerLevel getLevel();

    EntityType getEntityType();

    boolean isPlace();

    boolean needUpdate();

    boolean sameChunk(int x, int z);

    void update();

    void place(Location location);

    int getAmount();

    void setAmount(int amount);

    void updateSpawner();

    void load();

    void disable();
}
