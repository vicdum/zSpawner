package fr.maxlego08.spawner.zcore.enums;

public enum Tables {
    SPAWNERS("spawners"),
    ITEMS("items"),
    OPTIONS("options"),
    SPAWNER_LOCATION_HISTORY("spawner_location_history");

    private final String tableName;

    Tables(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return "%prefix%" + tableName;
    }
}
