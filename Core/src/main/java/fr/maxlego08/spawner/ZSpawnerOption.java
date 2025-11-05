package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.SpawnerOption;
import fr.maxlego08.spawner.save.Config;
import fr.maxlego08.spawner.storage.storages.Updatable;
import fr.maxlego08.spawner.storage.storages.interfaces.StorageManager;

import java.util.UUID;

public class ZSpawnerOption extends Updatable implements SpawnerOption {
    private final StorageManager storageManager;

    private final UUID spawnerId;

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

    public ZSpawnerOption(StorageManager storageManager,UUID spawnerId,double distance, double experienceMultiplier, double lootMultiplier, boolean autoKill, boolean autoSell, int maxEntity, int minDelay, int maxDelay, int minSpawn, int maxSpawn, int mobPerMinute, boolean dropLoots,boolean locationEnabled, int remainingEntity, long minLocationTime, long maxLocationTime, double locationPrice) {
        this.storageManager = storageManager;
        this.spawnerId = spawnerId;
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
        this.canUpdate();
    }

    @Override
    public double getExperienceMultiplier() {
        return experienceMultiplier;
    }

    @Override
    public void setExperienceMultiplier(double experienceMultiplier) {
        this.experienceMultiplier = experienceMultiplier;
        this.canUpdate();
    }

    @Override
    public double getLootMultiplier() {
        return lootMultiplier;
    }

    @Override
    public void setLootMultiplier(double lootMultiplier) {
        this.lootMultiplier = lootMultiplier;
        this.canUpdate();
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
        this.canUpdate();
    }

    @Override
    public int getMinDelay() {
        return minDelay;
    }

    @Override
    public void setMinDelay(int minDelay) {
        this.minDelay = minDelay;
        this.canUpdate();
    }

    @Override
    public int getMaxDelay() {
        return maxDelay;
    }

    @Override
    public void setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
        this.canUpdate();
    }

    @Override
    public int getMinSpawn() {
        return minSpawn;
    }

    @Override
    public void setMinSpawn(int minSpawn) {
        this.minSpawn = minSpawn;
        this.canUpdate();
    }

    @Override
    public int getMaxSpawn() {
        return maxSpawn;
    }

    @Override
    public void setMaxSpawn(int maxSpawn) {
        this.maxSpawn = maxSpawn;
        this.canUpdate();
    }

    @Override
    public int getMobPerMinute() {
        return this.mobPerMinute;
    }

    @Override
    public void setMobPerMinute(int mobPerMinute) {
        this.mobPerMinute = mobPerMinute;
        this.canUpdate();
    }

    @Override
    public void setAutoKill(boolean autoKill) {
        this.autoKill = autoKill;
        this.canUpdate();
    }

    @Override
    public void setAutoSell(boolean autoSell) {
        this.autoSell = autoSell;
        this.canUpdate();
    }

    @Override
    public boolean dropLoots() {
        return this.dropLoots;
    }

    @Override
    public void setDropLoots(boolean dropLoots) {
        this.dropLoots = dropLoots;
        this.canUpdate();
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
        this.canUpdate();
    }

    @Override
    public void setRemainingEntity(int remainingEntity) {
        this.remainingEntity = remainingEntity;
        this.canUpdate();
    }

    @Override
    public void removeRemainingEntity(int addedEntities) {
        this.remainingEntity -= addedEntities;
        this.canUpdate();
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
        return new ZSpawnerOption(this.storageManager,this.spawnerId,this.distance, this.experienceMultiplier, this.lootMultiplier, this.autoKill, this.autoSell, this.maxEntity, this.minDelay, this.maxDelay, this.minSpawn, this.maxSpawn, this.mobPerMinute, this.dropLoots, this.locationEnabled, this.remainingEntity, this.minLocationTime, this.maxLocationTime, this.locationPrice);
    }

    @Override
    public long getMinLocationTime() {
        return minLocationTime;
    }

    @Override
    public void setMinLocationTime(long minLocationTime) {
        this.minLocationTime = minLocationTime;
        this.canUpdate();
    }

    @Override
    public void addMinLocationTime(long timeToAdd) {
        this.minLocationTime += timeToAdd;
        this.canUpdate();
        if (this.minLocationTime < 0) this.minLocationTime = 0;
    }

    @Override
    public long getMaxLocationTime() {
        return maxLocationTime;
    }

    @Override
    public void setMaxLocationTime(long maxLocationTime) {
        this.maxLocationTime = maxLocationTime;
        this.canUpdate();
    }

    @Override
    public void addMaxLocationTime(long timeToAdd) {
        this.maxLocationTime += timeToAdd;
        this.canUpdate();
        if (this.maxLocationTime < 0) this.maxLocationTime = 0;
    }

    @Override
    public double getLocationPrice() {
        return locationPrice;
    }

    @Override
    public void setLocationPrice(double locationPrice) {
        this.locationPrice = locationPrice;
        this.canUpdate();
    }

    @Override
    public void addLocationPrice(double priceToAdd) {
        this.locationPrice += priceToAdd;
        this.canUpdate();
        if (this.locationPrice < 0) this.locationPrice = 0;
    }

    @Override
    public void save() {
        if (this.spawnerId == null) return;
        this.storageManager.upsertOption(this,this.spawnerId);
    }
}
