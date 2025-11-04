package fr.maxlego08.spawner.storage.storages;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.storage.storages.interfaces.ServerProfile;
import fr.maxlego08.spawner.storage.storages.interfaces.StorageManager;

import java.util.*;

public class ZServerProfile implements ServerProfile {
    private final StorageManager storageManager;
    private final Map<SpawnerType, Map<UUID, Spawner>> spawners = new HashMap<>();

    public ZServerProfile(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public Optional<Spawner> getSpawner(UUID uuid, SpawnerType spawnerType) {
        if (this.spawners.containsKey(spawnerType)) {
            Map<UUID, Spawner> typeMap = this.spawners.get(spawnerType);
            if (typeMap != null && typeMap.containsKey(uuid)) {
                return Optional.of(typeMap.get(uuid));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Spawner> getSpawner(UUID uuid) {
        for (Map<UUID, Spawner> typeMap : this.spawners.values()) {
            if (typeMap.containsKey(uuid)) {
                return Optional.of(typeMap.get(uuid));
            }
        }
        return Optional.empty();
    }

    @Override
    public Collection<Spawner> getSpawners() {
        List<Spawner> allSpawners = new ArrayList<>();
        for (Map<UUID, Spawner> typeMap : this.spawners.values()) {
            allSpawners.addAll(typeMap.values());
        }
        return Collections.unmodifiableList(allSpawners);
    }

    @Override
    public Collection<Spawner> getSpawners(SpawnerType spawnerType) {
        if (this.spawners.containsKey(spawnerType)) {
            Map<UUID, Spawner> typeMap = this.spawners.get(spawnerType);
            if (typeMap != null) {
                return Collections.unmodifiableCollection(typeMap.values());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void addSpawner(Spawner spawner) {
        this.spawners.computeIfAbsent(spawner.getType(), k -> new HashMap<>()).put(spawner.getSpawnerId(), spawner);
        this.storageManager.upsertSpawner(spawner);
    }

    @Override
    public void deleteSpawner(Spawner spawner) {
        if (this.spawners.containsKey(spawner.getType())) {
            Map<UUID, Spawner> typeMap = this.spawners.get(spawner.getType());
            if (typeMap != null) {
                typeMap.remove(spawner.getSpawnerId());
            }
        }
        this.storageManager.deleteSpawner(spawner);
    }

    @Override
    public void loadSpawner(Spawner spawner) {
        this.spawners.computeIfAbsent(spawner.getType(), k -> new HashMap<>()).put(spawner.getSpawnerId(), spawner);
    }
}
