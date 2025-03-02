package fr.maxlego08.spawner.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.maxlego08.spawner.storage.Tables;

public class SpawnerMigration extends Migration {

    @Override
    public void up() {
        create(Tables.SPAWNERS, table -> {
            table.uuid("owner").unique().primary();
            table.uuid("spawnerId").unique();
            table.string("location", 255);
            table.string("type", 255);
            table.bigInt("placedAt");
            table.string("entityType", 255);
            table.string("blockFace", 255);
            table.integer("amount");
        });
    }
}
