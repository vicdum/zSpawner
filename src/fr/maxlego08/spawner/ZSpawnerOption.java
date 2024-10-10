package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.SpawnerOption;

public class ZSpawnerOption implements SpawnerOption {

    public double distance;
    public double experienceMultiplier;
    public double lootMultiplier;
    public boolean autoKill;
    public boolean autoSell;
    public int maxEntity;
    public int minDelay;
    public int maxDelay;
    public int minSpawn;
    public int maxSpawn;
    public int mobPerMinute;
    public boolean needUpdate;

    public ZSpawnerOption(double distance, double experienceMultiplier, double lootMultiplier, boolean autoKill, boolean autoSell, int maxEntity, int minDelay, int maxDelay, int minSpawn, int maxSpawn, int mobPerMinute) {
        this.distance = distance;
        this.experienceMultiplier = experienceMultiplier;
        this.lootMultiplier = lootMultiplier;
        this.autoKill = autoKill;
        this.autoSell = autoSell;
        this.maxEntity = maxEntity;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.minSpawn = minSpawn;
        this.maxSpawn = maxSpawn;
        this.mobPerMinute = mobPerMinute;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public void setDistance(double distance) {
        this.distance = distance;
        this.needUpdate = true;
    }

    @Override
    public double getExperienceMultiplier() {
        return experienceMultiplier;
    }

    @Override
    public void setExperienceMultiplier(double experienceMultiplier) {
        this.experienceMultiplier = experienceMultiplier;
        this.needUpdate = true;
    }

    @Override
    public double getLootMultiplier() {
        return lootMultiplier;
    }

    @Override
    public void setLootMultiplier(double lootMultiplier) {
        this.lootMultiplier = lootMultiplier;
        this.needUpdate = true;
    }

    @Override
    public boolean enableAutoKill() {
        return autoKill;
    }

    @Override
    public boolean enableAutoSell() {
        return autoSell;
    }

    @Override
    public int getMaxEntity() {
        return maxEntity;
    }

    @Override
    public void setMaxEntity(int maxEntity) {
        this.maxEntity = maxEntity;
        this.needUpdate = true;
    }

    @Override
    public int getMinDelay() {
        return minDelay;
    }

    @Override
    public void setMinDelay(int minDelay) {
        this.minDelay = minDelay;
        this.needUpdate = true;
    }

    @Override
    public int getMaxDelay() {
        return maxDelay;
    }

    @Override
    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
        this.needUpdate = true;
    }

    @Override
    public int getMinSpawn() {
        return minSpawn;
    }

    @Override
    public void setMinSpawn(int minSpawn) {
        this.minSpawn = minSpawn;
        this.needUpdate = true;
    }

    @Override
    public int getMaxSpawn() {
        return maxSpawn;
    }

    @Override
    public void setMaxSpawn(int maxSpawn) {
        this.maxSpawn = maxSpawn;
        this.needUpdate = true;
    }

    @Override
    public int getMobPerMinute() {
        return this.mobPerMinute;
    }

    @Override
    public void setMobPerMinute(int mobPerMinute) {
        this.mobPerMinute = mobPerMinute;
        this.needUpdate = true;
    }

    @Override
    public void setAutoKill(boolean autoKill) {
        this.autoKill = autoKill;
        this.needUpdate = true;
    }

    @Override
    public void setAutoSell(boolean autoSell) {
        this.autoSell = autoSell;
        this.needUpdate = true;
    }

    @Override
    public boolean needUpdate() {
        return this.needUpdate;
    }

    @Override
    public void update() {
        this.needUpdate = false;
    }

    @Override
    public String toString() {
        return "ZSpawnerOption{" + "distance=" + distance + ", experienceMultiplier=" + experienceMultiplier + ", lootMultiplier=" + lootMultiplier + ", autoKill=" + autoKill + ", autoSell=" + autoSell + ", maxEntity=" + maxEntity + ", minDelay=" + minDelay + ", maxDelay=" + maxDelay + ", minSpawn=" + minSpawn + ", maxSpawn=" + maxSpawn + ", mobPerMinute=" + mobPerMinute + ", needUpdate=" + needUpdate + '}';
    }

    @Override
    public ZSpawnerOption cloneOption() {
        return new ZSpawnerOption(this.distance, this.experienceMultiplier, this.lootMultiplier, this.autoKill, this.autoSell, this.maxEntity, this.minDelay, this.maxDelay, this.minSpawn, this.maxSpawn, this.mobPerMinute);
    }
}
