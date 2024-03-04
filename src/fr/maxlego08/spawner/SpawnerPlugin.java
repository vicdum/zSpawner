package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.api.storage.SpawnerStorage;
import fr.maxlego08.spawner.command.commands.CommandSpawner;
import fr.maxlego08.spawner.placeholder.LocalPlaceholder;
import fr.maxlego08.spawner.save.MessageLoader;
import fr.maxlego08.spawner.storage.StorageManager;
import fr.maxlego08.spawner.zcore.ZPlugin;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class SpawnerPlugin extends ZPlugin {

    private final SpawnerManager manager = new SpawnerManager(this);
    private SpawnerStorage spawnerStorage;

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zspawner");

        this.preEnable();

        this.saveDefaultConfig();

        this.registerCommand("zspawner", new CommandSpawner(this), "spawner", "sp");

        // this.addSave(Config.getInstance());
        this.addSave(new MessageLoader(this));

        this.spawnerStorage = new StorageManager(this);
        this.addSave(this.spawnerStorage);

        this.addListener(new SpawnerListener(this));

        this.loadFiles();

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

    public SpawnerManager getManager() {
        return manager;
    }

    public SpawnerStorage getSpawnerStorage() {
        return this.spawnerStorage;
    }

    public IStorage getStorage() {
        return this.spawnerStorage.getStorage();
    }
}
