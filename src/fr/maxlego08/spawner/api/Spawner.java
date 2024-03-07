package fr.maxlego08.spawner.api;

import fr.maxlego08.spawner.zcore.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;
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
}
