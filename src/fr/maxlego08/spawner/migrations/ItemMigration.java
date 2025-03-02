package fr.maxlego08.spawner.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.maxlego08.spawner.storage.Tables;

public class ItemMigration extends Migration {
    @Override
    public void up() {
        create(Tables.ITEMS,table -> {
            table.uuid("spawnerId").foreignKey("%prefix%spawners").primary();
            table.string("itemStack", 255).primary();
            table.bigInt("amount");
        });
    }
}
