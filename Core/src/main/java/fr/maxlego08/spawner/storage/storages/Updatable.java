package fr.maxlego08.spawner.storage.storages;

import fr.maxlego08.spawner.zcore.utils.ZUtils;

public abstract class Updatable extends ZUtils {
    private int numberOfUpdates = 0;

    public void canUpdate(){
        this.numberOfUpdates++;
        if (this.numberOfUpdates % 5 == 0){
            this.save();
        }
    }

    public abstract void save();

}
