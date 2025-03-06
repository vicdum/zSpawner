package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.placeholder.LocalPlaceholder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class SpawnerPlaceholders {

    private final SpawnerPlugin plugin;

    public SpawnerPlaceholders(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {

        SpawnerManager manager = this.plugin.getManager();
        IStorage storage = this.plugin.getStorage();
        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();

        placeholder.register("sort_name", (player, args) -> manager.getPlayerSort(player).getName());
        placeholder.register("sort", (player, args) -> manager.getPlayerSort(player).name());
        placeholder.register("gui_spawners", (player, args) -> String.valueOf(storage.countSpawners(player, SpawnerType.GUI)));
        placeholder.register("virtual_spawners", (player, args) -> String.valueOf(storage.countSpawners(player, SpawnerType.VIRTUAL)));
        placeholder.register("classic_spawners", (player, args) -> String.valueOf(storage.countSpawners(player, SpawnerType.CLASSIC)));
        placeholder.register("material_", (player, args) -> {
            try {
                return this.plugin.getManager().getEntitiesMaterials().getOrDefault(EntityType.valueOf(args.toUpperCase()), Material.BARRIER.name());
            } catch (Exception exception) {
                return "BARRIER";
            }
        });
    }

}
