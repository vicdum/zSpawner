package fr.maxlego08.spawner.api.storage;

import fr.maxlego08.spawner.api.Spawner;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Optional;

public interface IStorage {

    Optional<Spawner> getSpawner(Location location);

    List<Spawner> getSpawners(int x, int z);

    long countSpawners(int x, int z);

    void placeSpawner(Location location, Spawner spawner);

    void removeSpawner(Location location);

    void load();

    void save();

    void purge(World world, boolean destroyBlock);

    void update();
}
