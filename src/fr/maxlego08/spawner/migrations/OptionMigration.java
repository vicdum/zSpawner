package fr.maxlego08.spawner.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.maxlego08.spawner.storage.Tables;

public class OptionMigration extends Migration {
    @Override
    public void up() {
        create(Tables.OPTIONS,table -> {
            table.uuid("spawnerId").foreignKey("%prefix%spawners").primary();
            table.decimal("distance");
            table.decimal("experienceMultiplier");
            table.decimal("lootMultiplier");
            table.bool("autoKill");
            table.bool("autoSell");
            table.integer("maxEntity");
            table.integer("minDelay");
            table.integer("maxDelay");
            table.integer("minSpawn");
            table.integer("maxSpawn");
            table.integer("mobPerMinute");
        });
    }
}
