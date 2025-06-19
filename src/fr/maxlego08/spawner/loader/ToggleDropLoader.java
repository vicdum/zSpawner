package fr.maxlego08.spawner.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.buttons.virtual.ToggleDropButton;
import org.bukkit.configuration.file.YamlConfiguration;

public class ToggleDropLoader extends ButtonLoader {

    private final SpawnerPlugin plugin;

    public ToggleDropLoader(SpawnerPlugin plugin) {
        super(plugin, "zspawner_toggle_drop");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {

        var enable = configuration.getString(path + "enable");
        var disable = configuration.getString(path + "disable");

        return new ToggleDropButton(this.plugin, enable, disable);
    }
}
