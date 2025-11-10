package fr.maxlego08.spawner.migrations;

import fr.maxlego08.sarah.SchemaBuilder;
import fr.maxlego08.sarah.database.Migration;
import fr.maxlego08.spawner.zcore.enums.Tables;

public class SpawnerMigration extends Migration {

    @Override
    public void up() {
        String spawnersTableName = Tables.SPAWNERS.getTableName();
        this.createOrAlter(spawnersTableName, table -> {
            table.uuid("owner").primary();
            table.uuid("spawner_id").unique().primary();
            table.string("location", 255);
            table.string("type", 255);
            table.bigInt("placed_at");
            table.string("entity_type", 255);
            table.string("block_face", 255);
            table.integer("amount");
        });
        SchemaBuilder.alter(this, spawnersTableName, table -> table.uuid("last_location_user").nullable());
        SchemaBuilder.alter(this, spawnersTableName, table -> table.bigInt("last_location_time").defaultValue(0));
        SchemaBuilder.alter(this, spawnersTableName, table -> table.bigInt("last_location_start_time").defaultValue(0));
    }
}
