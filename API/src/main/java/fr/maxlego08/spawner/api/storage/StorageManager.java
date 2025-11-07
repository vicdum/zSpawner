package fr.maxlego08.spawner.api.storage;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerItem;
import fr.maxlego08.spawner.api.SpawnerLocationHistory;
import fr.maxlego08.spawner.api.SpawnerOption;
import fr.maxlego08.spawner.api.dto.ItemDTO;
import fr.maxlego08.spawner.api.dto.OptionDTO;
import fr.maxlego08.spawner.api.dto.SpawnerDTO;
import fr.maxlego08.spawner.api.dto.SpawnerLocationHistoryDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageManager {
    void loadDatabase();

    boolean isEnable();

    List<SpawnerDTO> loadSpawners();

    List<OptionDTO> loadOptions();

    List<ItemDTO> loadItems();

    List<SpawnerLocationHistoryDTO> loadLocationHistories();

    void upsertSpawner(Spawner spawner);

    void upsertOption(SpawnerOption spawnerOption, UUID spawnerId);

    void upsertItem(SpawnerItem spawnerItem, UUID spawnerId);

    void upsertLocationHistory(SpawnerLocationHistory spawnerLocationHistory, UUID spawnerId);

    Optional<SpawnerOption> getOption(UUID spawnerId);

    void deleteSpawner(Spawner spawner);

    void deleteOption(SpawnerOption spawnerOption, UUID spawnerId);

    void deleteItem(SpawnerItem spawnerItem, UUID spawnerId);

    void saveAllNow();
}
