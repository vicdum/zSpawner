package fr.maxlego08.spawner.api.storage;

import fr.maxlego08.spawner.api.Spawner;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

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

}
