package fr.maxlego08.spawner.api;

import java.util.UUID;

public interface SpawnerLocationHistory extends Updatable {
    long getStartTime();

    long getDuration();

    long getEndTime();

    UUID getRentalPlayer();

    double getPrice();
}
