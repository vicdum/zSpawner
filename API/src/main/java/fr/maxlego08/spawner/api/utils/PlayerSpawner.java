package fr.maxlego08.spawner.api.utils;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.enums.Sort;
import org.bukkit.OfflinePlayer;

public class PlayerSpawner {

    private Sort typeShort = Sort.PLACE;
    private long placingCooldown = 0;
    private Spawner placingSpawner;
    private Spawner virtualSpawner;
    private OfflinePlayer targetPlayer;
    private int locationTime;

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

    public Spawner getVirtualSpawner() {
        return virtualSpawner;
    }

    public void setVirtualSpawner(Spawner virtualSpawner) {
        this.virtualSpawner = virtualSpawner;
        this.locationTime = (int) virtualSpawner.getOption().getMinLocationTime();
    }

    public OfflinePlayer getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(OfflinePlayer targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public int getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(int locationTime) {
        this.locationTime = locationTime;
    }


}
