package fr.maxlego08.spawner.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.maxlego08.spawner.storage.Tables;

public class SpawnerMigration extends Migration {

    @Override
    public void up() {
        create(Tables.SPAWNERS, table -> {
            table.uuid("owner").unique().primary();
            table.uuid("spawner_id").unique();
            table.string("location", 255);
            table.string("type", 255);
            table.bigInt("placed_at");
            table.string("entity_type", 255);
            table.string("block_face", 255);
            table.integer("amount");
        });
    }
}
