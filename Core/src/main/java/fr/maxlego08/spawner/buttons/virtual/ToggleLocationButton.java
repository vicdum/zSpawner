package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.save.Config;

public class ToggleLocationButton extends AbstractToggleButton {

    public ToggleLocationButton(SpawnerPlugin plugin, String enableText, String disableText) {
        super(plugin, enableText, disableText);
    }

    @Override
    protected void toggleOption(Spawner spawner) {
        if (Config.enableSpawnerLocation){
            if (spawner.getLastLocationUser() != null) {
                boolean isLocationActive = spawner.getLastLocationStartTime() + spawner.getLastLocationTime() > System.currentTimeMillis();

                if (isLocationActive) {
                    return;
                }
            }

            spawner.getOption().setLocationEnabled(!spawner.getOption().isLocationEnabled());
        }
    }

    @Override
    protected boolean getCurrentState(Spawner spawner) {
        return !spawner.getOption().isLocationEnabled();
    }
}
