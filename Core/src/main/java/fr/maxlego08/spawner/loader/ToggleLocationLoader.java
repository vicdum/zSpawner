package fr.maxlego08.spawner.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.buttons.virtual.ToggleLocationButton;

public class ToggleLocationLoader extends AbstractSpawnOptionLoader {

    public ToggleLocationLoader(SpawnerPlugin plugin) {
        super(plugin, "zspawner_toggle_location");
    }

    @Override
    protected Button createButton(String enableText, String disableText) {
        return new ToggleLocationButton(this.plugin, enableText, disableText);
    }
}
