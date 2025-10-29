package fr.maxlego08.spawner;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import fr.maxlego08.spawner.listener.ListenerAdapter;
import org.bukkit.entity.LivingEntity;

public class SpawnerListenerPaper extends ListenerAdapter {

    private final SpawnerPlugin plugin;

    public SpawnerListenerPaper(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onKnockBack(EntityKnockbackByEntityEvent event, LivingEntity entity) {
        if (entity.getPersistentDataContainer().has(this.plugin.getSpawnerKey())) event.setCancelled(true);
    }
}
