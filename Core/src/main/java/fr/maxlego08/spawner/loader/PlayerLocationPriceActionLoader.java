package fr.maxlego08.spawner.loader;

import fr.maxlego08.menu.api.loader.ActionLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.actions.PlayerLocationPriceAction;

import java.io.File;

public class PlayerLocationPriceActionLoader extends ActionLoader {
    private final SpawnerPlugin plugin;

    public PlayerLocationPriceActionLoader(SpawnerPlugin plugin) {
        super("zspawner_player_location_price");
        this.plugin = plugin;
    }

    @Override
    public Action load(String path, TypedMapAccessor accessor, File file) {
        int amount = accessor.getInt("amount", 0);
        return new PlayerLocationPriceAction(this.plugin, amount);
    }
}
