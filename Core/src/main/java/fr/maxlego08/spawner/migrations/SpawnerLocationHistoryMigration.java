package fr.maxlego08.spawner.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.maxlego08.spawner.storage.Tables;

public class SpawnerLocationHistoryMigration extends Migration {
    @Override
    public void up() {
        this.create(Tables.SPAWNER_LOCATION_HISTORY, table -> {
            table.uuid("spawner_id").primary();
            table.bigInt("timestamp").primary();
            table.bigInt("duration");
            table.uuid("player_id");
            table.decimal("price", 15, 2);
        });
    }
}
