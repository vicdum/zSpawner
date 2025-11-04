package fr.maxlego08.spawner.storage.storages.interfaces;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerItem;
import fr.maxlego08.spawner.api.SpawnerOption;
import fr.maxlego08.spawner.dto.ItemDTO;
import fr.maxlego08.spawner.dto.OptionDTO;
import fr.maxlego08.spawner.dto.SpawnerDTO;

import java.util.List;
import java.util.UUID;

public interface StorageManager {
    void loadDatabase();

    boolean isEnable();

    List<SpawnerDTO> loadSpawners();

    List<OptionDTO> loadOptions();

    List<ItemDTO> loadItems();

    void upsertSpawner(Spawner spawner);

    void upsertOption(SpawnerOption spawnerOption, UUID spawnerId);

    void upsertItem(SpawnerItem spawnerItem, UUID spawnerId);

    void deleteSpawner(Spawner spawner);

    void deleteOption(SpawnerOption spawnerOption, UUID spawnerId);

    void deleteItem(SpawnerItem spawnerItem, UUID spawnerId);
}
