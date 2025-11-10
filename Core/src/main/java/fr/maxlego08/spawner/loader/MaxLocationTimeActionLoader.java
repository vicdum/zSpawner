package fr.maxlego08.spawner.loader;

import fr.maxlego08.menu.api.loader.ActionLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.actions.MaxLocationTimeAction;

import java.io.File;

public class MaxLocationTimeActionLoader extends ActionLoader {
    private final SpawnerPlugin plugin;

    public MaxLocationTimeActionLoader(SpawnerPlugin plugin) {
        super("zspawner_max_location_time");
        this.plugin = plugin;
    }

    @Override
    public Action load(String path, TypedMapAccessor accessor, File file) {
        int amount = accessor.getInt("amount", 0);
        return new MaxLocationTimeAction(this.plugin, amount);
    }
}

