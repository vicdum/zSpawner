package fr.maxlego08.spawner.api;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface SpawnerLocationHistory {
    long getStartTime();

    long getDuration();

    void setDuration(long duration);

    long getEndTime();

    @NotNull UUID getRentalPlayer();

    double getPrice();

    void save();
}
