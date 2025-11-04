package fr.maxlego08.spawner.storage;

import fr.maxlego08.spawner.api.SpawnerLocationHistory;

import java.util.UUID;

public class ZSpawnerLocationHistory implements SpawnerLocationHistory {
    private final long startTime;
    private final long duration;
    private final UUID rentalPlayer;
    private final double price;
    private boolean needUpdate;

    public ZSpawnerLocationHistory(long startTime, long duration, UUID rentalPlayer, double price) {
        this.startTime = startTime;
        this.duration = duration;
        this.rentalPlayer = rentalPlayer;
        this.price = price;
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
    public boolean needUpdate() {
        return this.needUpdate;
    }

    @Override
    public void update() {
        this.needUpdate = false;
    }
}
