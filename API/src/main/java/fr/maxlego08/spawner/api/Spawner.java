package fr.maxlego08.spawner.api;

import fr.maxlego08.spawner.api.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Spawner {

    @NotNull UUID getOwner();

    UUID getSpawnerId();

    Location getLocation();

    SpawnerType getType();

    long getPlacedAt();

    SpawnerOption getOption();

    void setOption(SpawnerOption spawnerOption);

    void addLocationHistory(SpawnerLocationHistory spawnerLocationHistory);

    void setLocationHistory(List<SpawnerLocationHistory> spawnerLocationHistory);

    List<SpawnerLocationHistory> getLocationHistory();

    EntityType getEntityType();

    boolean isPlace();

    boolean sameChunk(int x, int z);

    void place(Location location);

    int getAmount();

    void setAmount(int amount);

    void updateSpawner();

    void load();

    void disable();

    void breakBlock();

    int comparePlace();

    int compareNotPlace();

    LivingEntity getLivingEntity();

    List<Entity> getDeadEntities();

    void entityDeath();

    void addItems(List<ItemStack> itemStacks);

    boolean isChunkLoaded();

    double getDistance();

    void tick();

    BlockFace getBlockFace();

    Cuboid getCuboid();

    List<SpawnerItem> getItems();

    void setItems(List<SpawnerItem> items);

    Optional<SpawnerItem> getSpawnerItem(ItemStack itemStack);

    void removeItem(SpawnerItem spawnerItem);

    void autoKill();

    String getSpawnerKey();

    Location getSpawnedEntityLocation();

    @Nullable UUID getLastLocationUser();

    void setLastLocationUser(@Nullable UUID uuid);

    long getLastLocationTime();

    void setLastLocationTime(long time);

    long getLastLocationStartTime();

    void setLastLocationStartTime(long time);

    void save();
}
