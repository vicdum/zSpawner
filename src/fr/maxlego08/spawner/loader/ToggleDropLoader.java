package fr.maxlego08.spawner.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.buttons.virtual.ToggleDropButton;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ToggleDropLoader implements ButtonLoader {

    private final SpawnerPlugin plugin;

    public ToggleDropLoader(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Class<? extends Button> getButton() {
        return ToggleDropButton.class;
    }

    @Override
    public String getName() {
        return "zspawner_toggle_drop";
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {

        var enable = configuration.getString(path + "enable");
        var disable = configuration.getString(path + "disable");

        return new ToggleDropButton(this.plugin, enable, disable);
    }
}
