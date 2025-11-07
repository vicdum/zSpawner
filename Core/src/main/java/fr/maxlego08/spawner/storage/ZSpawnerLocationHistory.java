package fr.maxlego08.spawner.storage;

import fr.maxlego08.spawner.api.SpawnerLocationHistory;
import fr.maxlego08.spawner.api.storage.StorageManager;

import java.util.UUID;

public class ZSpawnerLocationHistory extends Updatable implements SpawnerLocationHistory {
    private final StorageManager storageManager;
    private final UUID spawnerId;

    private final long startTime;
    private long duration;
    private final UUID rentalPlayer;
    private final double price;

    public ZSpawnerLocationHistory(StorageManager storageManager,UUID spawnerId,long startTime, long duration, UUID rentalPlayer, double price) {
        this.storageManager = storageManager;
        this.spawnerId = spawnerId;
        this.startTime = startTime;
        this.duration = duration;
        this.rentalPlayer = rentalPlayer;
        this.price = price;
        this.save();
    }


    @Override
    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @Override
    public void setDuration(long duration) {
        this.duration = duration;
        this.canUpdate();
    }

    @Override
    public long getEndTime() {
        return this.startTime + this.duration;
    }

    @Override
    public UUID getRentalPlayer() {
        return this.rentalPlayer;
    }

    @Override
    public double getPrice() {
        return this.price;
    }


    @Override
    public void save() {
        this.storageManager.upsertLocationHistory(this, this.spawnerId);
    }
}
