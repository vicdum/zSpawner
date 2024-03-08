package fr.maxlego08.spawner.api;

public interface SpawnerOption extends Updatable{

    double getDistance();

    double getExperienceMultiplier();

    double getLootMultiplier();

    boolean enableAutoKill();

    boolean enableAutoSell();

    int getMaxEntity();

    int getMinDelay();

    int getMaxDelay();

    int getMinSpawn();

    int getMaxSpawn();

    int getMobPerMinute();

    void setDistance(double distance);

    void setExperienceMultiplier(double experienceMultiplier);

    void setLootMultiplier(double lootMultiplier);

    void setAutoKill(boolean autoKill);

    void setAutoSell(boolean autoSell);

    void setMaxEntity(int maxEntity);

    void setMinDelay(int minDelay);

    void setMaxDelay(int maxDelay);

    void setMinSpawn(int minSpawn);

    void setMaxSpawn(int maxSpawn);

    void setMobPerMinute(int mobPerMinute);
}
