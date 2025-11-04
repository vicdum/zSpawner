package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import fr.maxlego08.spawner.placeholder.LocalPlaceholder;
import fr.maxlego08.spawner.storage.storages.interfaces.ServerProfile;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class SpawnerPlaceholders {

    private final SpawnerPlugin plugin;
    private final ServerProfile serverProfile;

    public SpawnerPlaceholders(SpawnerPlugin plugin) {
        this.plugin = plugin;
        this.serverProfile = plugin.getServerDataManager().getOrCreate();
    }

    public void register() {

        SpawnerManager manager = this.plugin.getManager();
        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();

        placeholder.register("sort_name", (player, args) -> manager.getPlayerSort(player).getName());
        placeholder.register("sort", (player, args) -> manager.getPlayerSort(player).name());

        placeholder.register("gui_spawners", (player, args) -> String.valueOf(this.serverProfile.getSpawners(player.getUniqueId(), SpawnerType.GUI).size()));
        placeholder.register("virtual_spawners", (player, args) -> String.valueOf(this.serverProfile.getSpawners(player.getUniqueId(), SpawnerType.VIRTUAL).size()));
        placeholder.register("classic_spawners", (player, args) -> String.valueOf(this.serverProfile.getSpawners(player.getUniqueId(), SpawnerType.CLASSIC).size()));
        placeholder.register("material_", (player, args) -> {
            try {
                return this.plugin.getManager().getEntitiesMaterials().getOrDefault(EntityType.valueOf(args.toUpperCase()), Material.BARRIER.name());
            } catch (Exception exception) {
                return "BARRIER";
            }
        });

        placeholder.register("is_drop_loot", ((player, args) -> {
            PlayerSpawner playerSpawner = this.plugin.getManager().getPlayerSpawners().get(player.getUniqueId());
            Spawner spawner = playerSpawner == null ? null : playerSpawner.getVirtualSpawner() == null ? null : playerSpawner.getVirtualSpawner();
            if (spawner == null) return "false";
            return String.valueOf(spawner.getOption().dropLoots());
        }));
    }

}
