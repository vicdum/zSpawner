package fr.maxlego08.spawner.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.maxlego08.spawner.storage.Tables;

public class OptionMigration extends Migration {
    @Override
    public void up() {
        createOrAlter(Tables.OPTIONS, table -> {
            table.uuid("spawner_id").primary();
            table.decimal("distance");
            table.decimal("experience_multiplier");
            table.decimal("loot_multiplier");
            table.bool("auto_kill");
            table.bool("auto_sell");
            table.integer("max_entity");
            table.integer("min_delay");
            table.integer("max_delay");
            table.integer("min_spawn");
            table.integer("max_spawn");
            table.integer("mob_per_minute");
            table.bool("drop_loots").defaultValue(false);
        });
    }
}
