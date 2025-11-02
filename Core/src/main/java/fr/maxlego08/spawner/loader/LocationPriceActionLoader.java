package fr.maxlego08.spawner.loader;

import fr.maxlego08.menu.api.loader.ActionLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.actions.SpawnerLocationPriceAction;

import java.io.File;

public class LocationPriceActionLoader extends ActionLoader {
    private final SpawnerPlugin plugin;

    public LocationPriceActionLoader(SpawnerPlugin plugin) {
        super("zspawner_location_price");
        this.plugin = plugin;
    }

    @Override
    public Action load(String path, TypedMapAccessor accessor, File file) {
        double amount = accessor.getDouble("amount", 0.0);
        return new SpawnerLocationPriceAction(this.plugin, amount);
    }
}

