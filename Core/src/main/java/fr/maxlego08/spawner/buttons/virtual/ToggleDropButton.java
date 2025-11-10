package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;

public class ToggleDropButton extends AbstractToggleButton {

    public ToggleDropButton(SpawnerPlugin plugin, String enableText, String disableText) {
        super(plugin, enableText, disableText);
    }

    @Override
    protected void toggleOption(Spawner spawner) {
        spawner.getOption().setDropLoots(!spawner.getOption().dropLoots());
    }

    @Override
    protected boolean getCurrentState(Spawner spawner) {
        return spawner.getOption().dropLoots();
    }
}
