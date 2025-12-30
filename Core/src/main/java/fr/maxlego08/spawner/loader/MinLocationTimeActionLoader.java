package fr.maxlego08.spawner.loader;

import fr.maxlego08.menu.api.loader.ActionLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.actions.MinLocationTimeAction;

import java.io.File;

public class MinLocationTimeActionLoader extends ActionLoader {
    private final SpawnerPlugin plugin;

    public MinLocationTimeActionLoader(SpawnerPlugin plugin) {
        super("zspawner_min_location_time");
        this.plugin = plugin;
    }

    @Override
    public Action load(String path, TypedMapAccessor accessor, File file) {
        int amount = accessor.getInt("amount", 0);
        return new MinLocationTimeAction(this.plugin, amount);
    }
}
