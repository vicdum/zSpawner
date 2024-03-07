package fr.maxlego08.spawner.api;

import org.bukkit.Material;

import java.util.List;

public interface SpawnerLevel {

    int getLevel();

    String getName();
    String getDisplayName();
    List<Material> getBlacklistMaterials();
    List<Material> getWhitelistMaterials();
    double getDistance();
    double getExperienceMultiplier();
    double getLootMultiplier();
    boolean isAutoKill();
    int getMaxEntity();
    int getMinDelay();
    int getMaxDelay();
    int getMinSpawn();
    int getMaxSpawn();
    int getMobPerMinute();

}
