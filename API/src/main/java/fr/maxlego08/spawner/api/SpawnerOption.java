package fr.maxlego08.spawner.api;

import org.jetbrains.annotations.NotNull;

public interface SpawnerOption {

    double getDistance();

    void setDistance(double distance);

    double getExperienceMultiplier();

    void setExperienceMultiplier(double experienceMultiplier);

    double getLootMultiplier();

    void setLootMultiplier(double lootMultiplier);

    boolean enableAutoKill();

    boolean enableAutoSell();

    int getMaxEntity();

    void setMaxEntity(int maxEntity);

    int getMinDelay();

    void setMinDelay(int minDelay);

    int getMaxDelay();

    void setMaxDelay(int maxDelay);

    int getMinSpawn();

    void setMinSpawn(int minSpawn);

    int getMaxSpawn();

    void setMaxSpawn(int maxSpawn);

    int getMobPerMinute();

    void setMobPerMinute(int mobPerMinute);

    void setAutoKill(boolean autoKill);

    void setAutoSell(boolean autoSell);

    boolean dropLoots();

    void setDropLoots(boolean dropLoots);

    int getRemainingEntity();

    boolean isLocationEnabled();

    void setLocationEnabled(boolean locationEnabled);

    void setRemainingEntity(int remainingEntity);

    void removeRemainingEntity(int addedEntities);

    @NotNull SpawnerOption cloneOption();

    long getMinLocationTime();

    void setMinLocationTime(long minLocationTime);

    void addMinLocationTime(long timeToAdd);

    long getMaxLocationTime();

    void setMaxLocationTime(long maxLocationTime);

    void addMaxLocationTime(long timeToAdd);

    double getLocationPrice();

    void setLocationPrice(double locationPrice);

    void addLocationPrice(double priceToAdd);

    void save();
}
