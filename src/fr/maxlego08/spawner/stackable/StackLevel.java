package fr.maxlego08.spawner.stackable;

public class StackLevel {
    private final int stackAmount;
    private final int delay;
    private final int minSpawnDelay;
    private final int maxSpawnDelay;
    private final int spawnCount;
    private final int maxNearbyEntities;
    private final int requiredPlayerRange;
    private final int spawnRange;

    public StackLevel(int stackAmount, int delay, int minSpawnDelay, int maxSpawnDelay, int spawnCount, int maxNearbyEntities, int requiredPlayerRange, int spawnRange) {
        this.stackAmount = stackAmount;
        this.delay = delay;
        this.minSpawnDelay = minSpawnDelay;
        this.maxSpawnDelay = maxSpawnDelay;
        this.spawnCount = spawnCount;
        this.maxNearbyEntities = maxNearbyEntities;
        this.requiredPlayerRange = requiredPlayerRange;
        this.spawnRange = spawnRange;
    }

    public int getStackAmount() {
        return stackAmount;
    }

    public int getDelay() {
        return delay;
    }

    public int getMinSpawnDelay() {
        return minSpawnDelay;
    }

    public int getMaxSpawnDelay() {
        return maxSpawnDelay;
    }

    public int getSpawnCount() {
        return spawnCount;
    }

    public int getMaxNearbyEntities() {
        return maxNearbyEntities;
    }

    public int getRequiredPlayerRange() {
        return requiredPlayerRange;
    }

    public int getSpawnRange() {
        return spawnRange;
    }
}
