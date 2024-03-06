package fr.maxlego08.spawner.api.utils;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.enums.Sort;

public class PlayerSpawner {

    private Sort typeShort = Sort.PLACE;
    private long placingCooldown = 0;
    private Spawner placingSpawner;

    public Sort getTypeShort() {
        return typeShort;
    }

    public void setTypeShort(Sort typeShort) {
        this.typeShort = typeShort;
    }

    public long getPlacingCooldown() {
        return placingCooldown;
    }

    public void setPlacingCooldown(long placingCooldown) {
        this.placingCooldown = placingCooldown;
    }

    public Spawner getPlacingSpawner() {
        return placingSpawner;
    }

    public boolean isPlacingSpawner() {
        return this.placingCooldown >= System.currentTimeMillis();
    }

    public void setPlacingSpawner(Spawner placingSpawner) {
        this.placingSpawner = placingSpawner;
        this.placingCooldown = System.currentTimeMillis() + (1000 * 60);
    }

    public void placeSpawner() {
        this.placingSpawner = null;
        this.placingCooldown = 0;
    }

    public void toggleSort() {
        this.typeShort = this.typeShort.next();
    }
}
