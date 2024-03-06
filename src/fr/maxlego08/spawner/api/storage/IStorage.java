package fr.maxlego08.spawner.api.storage;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Optional;

public interface IStorage {

    void addSpawner(Spawner spawner);

    long countSpawners(int x, int z);

    long countSpawners(int x, int z, EntityType entityType);

    long countSpawners(OfflinePlayer player, SpawnerType spawnerType);

    Optional<Spawner> getSpawner(Location location);

    List<Spawner> getSpawners(int x, int z);

    List<Spawner> getSpawners(OfflinePlayer offlinePlayer);

    List<Spawner> getSpawners(OfflinePlayer player, SpawnerType spawnerType);

    void load();

    void purge(World world, boolean destroyBlock);

    void removeSpawner(Location location);

    void removeSpawner(Spawner spawner);

    void save();

    void update();
}
