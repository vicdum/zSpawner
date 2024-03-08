package fr.maxlego08.spawner;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import fr.maxlego08.spawner.listener.ListenerAdapter;
import org.bukkit.entity.LivingEntity;

public class SpawnerListenerPaper extends ListenerAdapter {

    @Override
    public void onKnockBack(EntityKnockbackByEntityEvent event, LivingEntity entity) {
        if (entity.hasMetadata("zspawner")) event.setCancelled(true);
    }
}
