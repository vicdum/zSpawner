package fr.maxlego08.spawner;

import fr.maxlego08.spawner.command.commands.CommandTemplate;
import fr.maxlego08.spawner.placeholder.LocalPlaceholder;
import fr.maxlego08.spawner.save.Config;
import fr.maxlego08.spawner.save.MessageLoader;
import fr.maxlego08.spawner.zcore.ZPlugin;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class SpawnerPlugin extends ZPlugin {

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("template");

        this.preEnable();

        this.registerCommand("template", new CommandTemplate(this));

        this.addSave(Config.getInstance());
        this.addSave(new MessageLoader(this));

        this.loadFiles();

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

}
