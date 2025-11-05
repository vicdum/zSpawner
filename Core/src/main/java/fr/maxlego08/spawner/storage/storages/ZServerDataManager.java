package fr.maxlego08.spawner.storage.storages;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.ZSpawner;
import fr.maxlego08.spawner.ZSpawnerItem;
import fr.maxlego08.spawner.ZSpawnerOption;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerItem;
import fr.maxlego08.spawner.api.SpawnerOption;
import fr.maxlego08.spawner.dto.SpawnerDTO;
import fr.maxlego08.spawner.storage.storages.interfaces.ServerDataManager;
import fr.maxlego08.spawner.storage.storages.interfaces.ServerProfile;
import fr.maxlego08.spawner.storage.storages.interfaces.StorageManager;
import fr.maxlego08.spawner.zcore.utils.ZUtils;

import java.util.*;

public class ZServerDataManager extends ZUtils implements ServerDataManager {
    private final SpawnerPlugin plugin;
    private ServerProfile profile;

    public ZServerDataManager(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Optional<ServerProfile> getServerProfile() {
        return Optional.ofNullable(this.profile);
    }

    @Override
    public ServerProfile getOrCreate() {
        return this.profile == null ? this.profile = new ZServerProfile(this.plugin.getStorageManager()) : this.profile;
    }

    @Override
    public void clearAll() {

    }

    @Override
    public void loadServerData() {
        StorageManager storageManager = this.plugin.getStorageManager();
        var spawners = storageManager.loadSpawners();
        var options = storageManager.loadOptions();
        Map<UUID, SpawnerOption> spawnerOptions = new HashMap<>();
        for (var optionDTO : options) {
            spawnerOptions.put(optionDTO.spawner_id(), new ZSpawnerOption(storageManager,optionDTO.spawner_id(),optionDTO.distance(), optionDTO.experience_multiplier(), optionDTO.loot_multiplier(), optionDTO.auto_kill(), optionDTO.auto_sell(), optionDTO.max_entity(), optionDTO.min_delay(), optionDTO.max_delay(), optionDTO.min_spawn(), optionDTO.max_spawn(), optionDTO.mob_per_minute(), optionDTO.drop_loots(), optionDTO.location_enabled(), optionDTO.remaining(), optionDTO.min_location_time(), optionDTO.max_location_time(), optionDTO.location_price()));
        }
        var items = storageManager.loadItems();
        Map<UUID, List<SpawnerItem>> itemsBySpawnerId = new HashMap<>();
        for (var item : items) {
            itemsBySpawnerId.computeIfAbsent(item.spawner_id(), k -> new ArrayList<>()).add(new ZSpawnerItem(item.unique_id(),item.item_stack(),item.amount(), storageManager, item.spawner_id()));
        }
        ServerProfile serverProfile = this.getOrCreate();
        for (SpawnerDTO spawnerDTO : spawners) {
            Spawner spawner = new ZSpawner(this.plugin, spawnerDTO.spawner_id(), spawnerDTO.owner(), spawnerDTO.type(),spawnerDTO.entity_type(), spawnerDTO.placed_at(), changeStringLocationToLocation(spawnerDTO.location()),spawnerDTO.amount(),spawnerDTO.block_face(),spawnerDTO.last_location_user(),spawnerDTO.last_location_time());
            spawner.setLastLocationStartTime(spawnerDTO.last_location_time());

            List<SpawnerItem> spawnerItems = itemsBySpawnerId.getOrDefault(spawnerDTO.spawner_id(), Collections.emptyList());
            spawner.setItems(spawnerItems);
            SpawnerOption spawnerOption = spawnerOptions.get(spawnerDTO.spawner_id());
            if (spawnerOption != null) {
                spawner.setOption(spawnerOption);
            }
            serverProfile.loadSpawner(spawner);
        }
    }
}
