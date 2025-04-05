package fr.maxlego08.spawner.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.maxlego08.spawner.storage.Tables;

public class ItemMigration extends Migration {
    @Override
    public void up() {
        create(Tables.ITEMS, table -> {
            table.uuid("unique_id").primary();
            table.uuid("spawner_id").primary();
            table.text("item_stack");
            table.bigInt("amount");
        });
    }
}
