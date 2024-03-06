package fr.maxlego08.spawner.api.storage;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IStorage {

    Optional<Spawner> getSpawner(Location location);

    List<Spawner> getSpawners(int x, int z);

    List<Spawner> getSpawners(OfflinePlayer offlinePlayer);

    long countSpawners(int x, int z);
    void addSpawner(Spawner spawner);

    void removeSpawner(Location location);

    void removeSpawner(Spawner spawner);

    void load();

    void save();

    void purge(World world, boolean destroyBlock);

    void update();

    long countSpawners(OfflinePlayer player, SpawnerType spawnerType);

    List<Spawner> getSpawners(OfflinePlayer player, SpawnerType spawnerType);
}
