package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.SpawnerLevel;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ZSpawnerLevel implements SpawnerLevel {

    public static SpawnerLevel DEFAULT_LEVEL = new ZSpawnerLevel(0, "TMP", "TMP", new ArrayList<>(), new ArrayList<>(), 6.0, 1.0, 1.0, false, 1000, 10000, 15000, 1, 2, 0);

    public final int level;

    public final String name;
    public final String displayName;
    public final List<Material> blacklistMaterials;
    public final List<Material> whitelistMaterials;
    public final double distance;
    public final double experienceMultiplier;
    public final double lootMultiplier;
    public final boolean autoKill;
    public final int maxEntity;
    public final int minDelay;
    public final int maxDelay;
    public final int minSpawn;
    public final int maxSpawn;
    public final int mobPerMinute;

    public ZSpawnerLevel(int level, String name, String displayName, List<Material> blacklistMaterials, List<Material> whitelistMaterials, double distance, double experienceMultiplier, double lootMultiplier, boolean autoKill, int maxEntity, int minDelay, int maxDelay, int minSpawn, int maxSpawn, int mobPerMinute) {
        this.level = level;
        this.name = name;
        this.displayName = displayName;
        this.blacklistMaterials = blacklistMaterials;
        this.whitelistMaterials = whitelistMaterials;
        this.distance = distance;
        this.experienceMultiplier = experienceMultiplier;
        this.lootMultiplier = lootMultiplier;
        this.autoKill = autoKill;
        this.maxEntity = maxEntity;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.minSpawn = minSpawn;
        this.maxSpawn = maxSpawn;
        this.mobPerMinute = mobPerMinute;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public List<Material> getBlacklistMaterials() {
        return blacklistMaterials;
    }

    @Override
    public List<Material> getWhitelistMaterials() {
        return whitelistMaterials;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public double getExperienceMultiplier() {
        return experienceMultiplier;
    }

    @Override
    public double getLootMultiplier() {
        return lootMultiplier;
    }

    @Override
    public boolean isAutoKill() {
        return autoKill;
    }

    @Override
    public int getMaxEntity() {
        return maxEntity;
    }

    @Override
    public int getMinDelay() {
        return minDelay;
    }

    @Override
    public int getMaxDelay() {
        return maxDelay;
    }

    @Override
    public int getMinSpawn() {
        return minSpawn;
    }

    @Override
    public int getMaxSpawn() {
        return maxSpawn;
    }

    @Override
    public int getMobPerMinute() {
        return this.mobPerMinute;
    }

    @Override
    public String toString() {
        return "ZSpawnerLevel{" +
                "level=" + level +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", blacklistMaterials=" + blacklistMaterials +
                ", whitelistMaterials=" + whitelistMaterials +
                ", distance=" + distance +
                ", experienceMultiplier=" + experienceMultiplier +
                ", lootMultiplier=" + lootMultiplier +
                ", autoKill=" + autoKill +
                ", maxEntity=" + maxEntity +
                ", minDelay=" + minDelay +
                ", maxDelay=" + maxDelay +
                ", minSpawn=" + minSpawn +
                ", maxSpawn=" + maxSpawn +
                ", mobPerMinute=" + mobPerMinute +
                '}';
    }
}
