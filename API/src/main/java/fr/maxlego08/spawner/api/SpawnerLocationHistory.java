package fr.maxlego08.spawner.api;

import java.util.UUID;

public interface SpawnerLocationHistory {
    long getStartTime();

    long getDuration();

    void setDuration(long duration);

    long getEndTime();

    UUID getRentalPlayer();

    double getPrice();

    void save();
}
