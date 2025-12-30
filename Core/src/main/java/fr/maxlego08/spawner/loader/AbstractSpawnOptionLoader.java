package fr.maxlego08.spawner.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.spawner.SpawnerPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class AbstractSpawnOptionLoader extends ButtonLoader {
    protected final SpawnerPlugin plugin;

    public AbstractSpawnOptionLoader(SpawnerPlugin plugin, String buttonName) {
        super(plugin, buttonName);
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        String enableText = configuration.getString(path + "enable");
        String disableText = configuration.getString(path + "disable");
        return createButton(enableText, disableText);
    }

    protected abstract Button createButton(String enableText, String disableText);
}
