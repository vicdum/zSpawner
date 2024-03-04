package fr.maxlego08.spawner;

import fr.maxlego08.spawner.api.SpawnerLevel;

public class ZSpawnerLevel implements SpawnerLevel {


    private final String name;
    private final int level;

    public ZSpawnerLevel(String name, int level) {
        this.name = name;
        this.level = level;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
