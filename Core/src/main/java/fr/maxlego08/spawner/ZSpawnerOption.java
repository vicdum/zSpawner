package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.SpawnerOption;
import fr.maxlego08.spawner.save.Config;

public class ZSpawnerOption implements SpawnerOption {

    private double distance;
    private double experienceMultiplier;
    private double lootMultiplier;
    private boolean autoKill;
    private boolean autoSell;
    private int maxEntity;
    private int minDelay;
    private int maxDelay;
    private int minSpawn;
    private int maxSpawn;
    private int mobPerMinute;
    private boolean needUpdate;
    private boolean dropLoots;
    private boolean locationEnabled;
    private int remainingEntity;
    private long minLocationTime;
    private long maxLocationTime;
    private double locationPrice;

    public ZSpawnerOption(double distance, double experienceMultiplier, double lootMultiplier, boolean autoKill, boolean autoSell, int maxEntity, int minDelay, int maxDelay, int minSpawn, int maxSpawn, int mobPerMinute, boolean dropLoots,boolean locationEnabled, int remainingEntity, long minLocationTime, long maxLocationTime, double locationPrice) {
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
        this.dropLoots = dropLoots;
        this.locationEnabled = locationEnabled;
        this.remainingEntity = remainingEntity;
        this.minLocationTime = minLocationTime;
        this.maxLocationTime = maxLocationTime;
        this.locationPrice = locationPrice;
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
    public boolean dropLoots() {
        return this.dropLoots;
    }

    @Override
    public void setDropLoots(boolean dropLoots) {
        this.dropLoots = dropLoots;
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
    public int getRemainingEntity() {
        return remainingEntity;
    }

    @Override
    public boolean isLocationEnabled() {
        return Config.enableSpawnerLocation && this.locationEnabled;
    }

    @Override
    public void setLocationEnabled(boolean locationEnabled) {
        this.locationEnabled = locationEnabled;
        this.needUpdate = true;
    }

    @Override
    public void setRemainingEntity(int remainingEntity) {
        this.remainingEntity = remainingEntity;
        this.needUpdate = true;
    }

    @Override
    public void removeRemainingEntity(int addedEntities) {
        this.remainingEntity -= addedEntities;
        this.needUpdate = true;
    }

    @Override
    public String toString() {
        return "ZSpawnerOption{" +
                "distance=" + distance +
                ", experienceMultiplier=" + experienceMultiplier +
                ", lootMultiplier=" + lootMultiplier +
                ", autoKill=" + autoKill +
                ", autoSell=" + autoSell +
                ", maxEntity=" + maxEntity +
                ", minDelay=" + minDelay +
                ", maxDelay=" + maxDelay +
                ", minSpawn=" + minSpawn +
                ", maxSpawn=" + maxSpawn +
                ", mobPerMinute=" + mobPerMinute +
                ", needUpdate=" + needUpdate +
                ", dropLoots=" + dropLoots +
                ", locationEnabled=" + locationEnabled +
                ", remainingEntity=" + remainingEntity +
                ", minLocationTime=" + minLocationTime +
                ", maxLocationTime=" + maxLocationTime +
                ", locationPrice=" + locationPrice +
                '}';
    }

    @Override
    public ZSpawnerOption cloneOption() {
        return new ZSpawnerOption(this.distance, this.experienceMultiplier, this.lootMultiplier, this.autoKill, this.autoSell, this.maxEntity, this.minDelay, this.maxDelay, this.minSpawn, this.maxSpawn, this.mobPerMinute, this.dropLoots, this.locationEnabled, this.remainingEntity, this.minLocationTime, this.maxLocationTime, this.locationPrice);
    }

    @Override
    public long getMinLocationTime() {
        return minLocationTime;
    }

    @Override
    public void setMinLocationTime(long minLocationTime) {
        this.minLocationTime = minLocationTime;
        this.needUpdate = true;
    }

    @Override
    public void addMinLocationTime(long timeToAdd) {
        this.minLocationTime += timeToAdd;
        this.needUpdate = true;
        if (this.minLocationTime < 0) this.minLocationTime = 0;
    }

    @Override
    public long getMaxLocationTime() {
        return maxLocationTime;
    }

    @Override
    public void setMaxLocationTime(long maxLocationTime) {
        this.maxLocationTime = maxLocationTime;
        this.needUpdate = true;
    }

    @Override
    public void addMaxLocationTime(long timeToAdd) {
        this.maxLocationTime += timeToAdd;
        this.needUpdate = true;
        if (this.maxLocationTime < 0) this.maxLocationTime = 0;
    }

    @Override
    public double getLocationPrice() {
        return locationPrice;
    }

    @Override
    public void setLocationPrice(double locationPrice) {
        this.locationPrice = locationPrice;
        this.needUpdate = true;
    }

    @Override
    public void addLocationPrice(double priceToAdd) {
        this.locationPrice += priceToAdd;
        this.needUpdate = true;
        if (this.locationPrice < 0) this.locationPrice = 0;
    }
}
