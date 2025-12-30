package fr.maxlego08.spawner.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.buttons.virtual.ToggleDropButton;

public class ToggleDropLoader extends AbstractSpawnOptionLoader {

    public ToggleDropLoader(SpawnerPlugin plugin) {
        super(plugin, "zspawner_toggle_drop");
    }

    @Override
    protected Button createButton(String enableText, String disableText) {
        return new ToggleDropButton(this.plugin, enableText, disableText);
    }
}
