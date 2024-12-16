package fr.maxlego08.spawner.api;

public interface SpawnerOption extends Updatable {

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

    SpawnerOption cloneOption();
}
