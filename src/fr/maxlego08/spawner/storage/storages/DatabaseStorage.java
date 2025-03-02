package fr.maxlego08.spawner.storage.storages;

import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
import fr.maxlego08.sarah.DatabaseConfiguration;
import fr.maxlego08.sarah.DatabaseConnection;
import fr.maxlego08.sarah.HikariDatabaseConnection;
import fr.maxlego08.sarah.MigrationManager;
import fr.maxlego08.sarah.RequestHelper;
import fr.maxlego08.sarah.SqliteConnection;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.maxlego08.sarah.logger.JULogger;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.ZSpawner;
import fr.maxlego08.spawner.ZSpawnerItem;
import fr.maxlego08.spawner.ZSpawnerOption;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerItem;
import fr.maxlego08.spawner.api.SpawnerOption;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.api.storage.StorageType;
import fr.maxlego08.spawner.dto.ItemDTO;
import fr.maxlego08.spawner.dto.OptionDTO;
import fr.maxlego08.spawner.dto.SpawnerDTO;
import fr.maxlego08.spawner.migrations.ItemMigration;
import fr.maxlego08.spawner.migrations.OptionMigration;
import fr.maxlego08.spawner.migrations.SpawnerMigration;
import fr.maxlego08.spawner.storage.Tables;
import fr.maxlego08.spawner.zcore.ZPlugin;
import fr.maxlego08.spawner.zcore.logger.Logger;
import fr.maxlego08.spawner.zcore.utils.ElapsedTime;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class DatabaseStorage extends ZUtils implements IStorage {

    private final SpawnerPlugin plugin;
    private RequestHelper requestHelper;
    private List<Spawner> spawners = new ArrayList<>();

    public DatabaseStorage(SpawnerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Optional<Spawner> getSpawner(Location location) {
        return this.spawners.stream().filter(spawner -> spawner.isPlace() && spawner.getCuboid().contains(location)).findFirst();
    }

    @Override
    public Optional<Spawner> getSpawner(Location location, SpawnerType spawnerType) {
        return this.spawners.stream().filter(spawner -> spawner.getType() == spawnerType && spawner.isPlace() && spawner.getCuboid().contains(location)).findFirst();
    }

    @Override
    public Optional<Spawner> getSpawnerByEntity(LivingEntity entity) {
        return this.spawners.stream().filter(spawner -> spawner.getLivingEntity() != null && spawner.getLivingEntity() == entity).findFirst();
    }

    @Override
    public Optional<Spawner> getSpawnerByDeadEntity(Entity entity) {
        return this.spawners.stream().filter(spawner -> spawner.getDeadEntities().contains(entity)).findFirst();
    }

    @Override
    public List<Spawner> getSpawners(int x, int z) {
        return this.spawners.stream().filter(spawner -> spawner.sameChunk(x, z)).collect(Collectors.toList());
    }

    @Override
    public List<Spawner> getSpawners(OfflinePlayer offlinePlayer) {
        return this.spawners.stream().filter(spawner -> spawner.getOwner().equals(offlinePlayer.getUniqueId())).collect(Collectors.toList());
    }

    @Override
    public List<Spawner> getSpawners(OfflinePlayer offlinePlayer, SpawnerType spawnerType) {
        return this.spawners.stream().filter(spawner -> spawner.getOwner().equals(offlinePlayer.getUniqueId()) && spawnerType == spawner.getType()).collect(Collectors.toList());
    }

    @Override
    public long countSpawners(int x, int z) {
        return this.spawners.stream().filter(spawner -> spawner.sameChunk(x, z)).count();
    }

    @Override
    public long countSpawners(int x, int z, EntityType entityType) {
        return this.spawners.stream().filter(spawner -> spawner.sameChunk(x, z) && entityType == spawner.getEntityType()).count();
    }

    @Override
    public void addSpawner(Spawner spawner) {
        this.spawners.add(spawner);
        ZPlugin.service.execute(() -> this.upsertSpawner(spawner));
    }

    @Override
    public void removeSpawner(Location location) {
        getSpawner(location).ifPresent(this::removeSpawner);
    }

    @Override
    public void removeSpawner(Spawner spawner) {
        this.spawners.remove(spawner);
        ZPlugin.service.execute(() -> this.deleteSpawner(spawner));
    }

    @Override
    public void load() {
        ZPlugin.service.execute(() -> {

            this.spawners.clear();

            FileConfiguration configuration = plugin.getConfig();
            String tablePrefix = configuration.getString("sql.tablePrefix", "zspawner_");
            StorageType storageType = StorageType.valueOf(configuration.getString("storage", "SQLITE"));

            String user = configuration.getString("sql.user");
            String password = configuration.getString("sql.password");
            String database = configuration.getString("sql.database");
            String host = configuration.getString("sql.host");
            int port = configuration.getInt("sql.port");
            boolean debug = configuration.getBoolean("sql.debug");

            DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(tablePrefix, user, password, port, host, database, debug, storageType == StorageType.SQLITE ? DatabaseType.SQLITE : DatabaseType.MYSQL);
            DatabaseConnection databaseConnection = storageType == StorageType.SQLITE ? new SqliteConnection(databaseConfiguration, plugin.getDataFolder()) : new HikariDatabaseConnection(databaseConfiguration);
            databaseConnection.connect();

            if (!databaseConnection.isValid()) {
                throw new RuntimeException("Impossible to connect to database!");
            }

            this.requestHelper = new RequestHelper(databaseConnection, JULogger.from(plugin.getLogger()));

            MigrationManager.setMigrationTableName("zspawner_migrations");
            MigrationManager.registerMigration(new SpawnerMigration());
            MigrationManager.registerMigration(new ItemMigration());
            MigrationManager.registerMigration(new OptionMigration());
            MigrationManager.execute(databaseConnection, JULogger.from(plugin.getLogger()));

            ElapsedTime elapsedTime = new ElapsedTime("Select spawners");
            elapsedTime.start();
            this.spawners = this.getAllSpawners();
            elapsedTime.end();

            Bukkit.getScheduler().runTask(this.plugin, () -> this.spawners.forEach(Spawner::load));
        });
    }

    @Override
    public void save() {
        this.spawners.forEach(Spawner::disable);
        this.update(false);
    }

    @Override
    public void purge(World world, boolean destroyBlock) {
        // ToDo
    }

    @Override
    public void update(boolean async) {
        Runnable runnable = () -> this.spawners.forEach(spawner -> {
            if (spawner.needUpdate()) this.upsertSpawner(spawner);
            SpawnerOption spawnerOption = spawner.getOption();
            if (spawnerOption.needUpdate()) this.upsertSpawnerOption(spawner.getSpawnerId(), spawnerOption);
            spawner.getItems().stream().filter(SpawnerItem::needUpdate).forEach(spawnerItem -> this.upsertSpawnerItem(spawner.getSpawnerId(), spawnerItem));
        });

        if (async) ZPlugin.service.execute(runnable);
        else runnable.run();
    }

    @Override
    public List<Spawner> getSpawners(SpawnerType spawnerType) {
        return this.spawners.stream().filter(spawner -> spawner.getType() == spawnerType).collect(Collectors.toList());
    }

    @Override
    public long countSpawners(OfflinePlayer player, SpawnerType spawnerType) {
        return getSpawners(player).stream().filter(spawner -> spawner.getType() == spawnerType).count();
    }

    @Override
    public void deleteSpawnerItem(Spawner spawner, SpawnerItem spawnerItem) {
        ZPlugin.service.execute(() -> this.deleteSpawnerItem(spawner.getSpawnerId(), ItemStackUtils.serializeItemStack(spawnerItem.getItemStack())));
    }

    @Override
    public List<Spawner> getSpawners() {
        return spawners;
    }

    public void deleteSpawner(Spawner spawner) {
        this.requestHelper.delete(Tables.SPAWNERS, table -> table.where("spawnerId", spawner.getSpawnerId()));
    }

    public void upsertSpawner(Spawner spawner) {
        spawner.update();
        this.requestHelper.upsert(Tables.SPAWNERS, table -> {
            table.uuid("owner", spawner.getOwner()).primary();
            table.uuid("spawnerId", spawner.getSpawnerId()).primary();
            table.string("location", spawner.getLocation() == null ? null : changeLocationToString(spawner.getLocation()));
            table.string("type", spawner.getType().name());
            table.bigInt("placedAt", spawner.getPlacedAt());
            table.string("entityType", spawner.getEntityType().name());
            table.string("blockFace", spawner.getBlockFace().name());
            table.bigInt("amount", spawner.getAmount());
        });
    }

    public void upsertSpawnerItem(UUID spawnerId, SpawnerItem spawnerItem) {
        spawnerItem.update();
        this.requestHelper.upsert(Tables.ITEMS, table -> {
            table.uuid("spawnerId", spawnerId).primary();
            table.string("itemStack", ItemStackUtils.serializeItemStack(spawnerItem.getItemStack())).primary();
            table.bigInt("amount", spawnerItem.getAmount());
        });
    }


    public List<Spawner> getAllSpawners() {
        var spawners = this.requestHelper.selectAll(Tables.SPAWNERS, SpawnerDTO.class);
        var options = this.requestHelper.selectAll(Tables.OPTIONS, OptionDTO.class);
        var items = this.requestHelper.selectAll(Tables.ITEMS, ItemDTO.class);

        return spawners.stream().map(spawnerDTO -> {

            Spawner spawner = new ZSpawner(this.plugin, spawnerDTO.spawnerId(), spawnerDTO.spawnerId(), spawnerDTO.type(), spawnerDTO.entityType(), spawnerDTO.placedAt(), spawnerDTO.location() != null ? changeStringLocationToLocation(spawnerDTO.location()) : null, spawnerDTO.amount(), spawnerDTO.blockFace());

            spawner.setItems(items.stream().filter(itemDTO -> itemDTO.spawnerId().equals(spawnerDTO.spawnerId())).map(itemDTO -> new ZSpawnerItem(itemDTO.itemStack(), itemDTO.amount())).collect(Collectors.toList()));

            options.stream().filter(optionDTO -> optionDTO.spawnerId().equals(spawnerDTO.spawnerId())).findFirst().ifPresent(optionDTO -> {
                SpawnerOption option = new ZSpawnerOption(optionDTO.distance(), optionDTO.experienceMultiplier(), optionDTO.lootMultiplier(), optionDTO.autoKill(), optionDTO.autoSell(), optionDTO.maxEntity(), optionDTO.minDelay(), optionDTO.maxDelay(), optionDTO.minSpawn(), optionDTO.maxSpawn(), optionDTO.mobPerMinute());
                spawner.setOption(option);
            });

            return spawner;
        }).collect(Collectors.toList());
    }

    public void deleteSpawnerItem(UUID spawnerId, String itemStack) {
        this.requestHelper.delete(Tables.ITEMS, table -> table.where("spawnerId", spawnerId).where("itemStack", itemStack));
    }

    public void upsertSpawnerOption(UUID spawnerId, SpawnerOption option) {
        option.update();
        this.requestHelper.upsert(Tables.OPTIONS, table -> {
            table.uuid("spawnerId", spawnerId).primary();
            table.decimal("distance", option.getDistance());
            table.decimal("experienceMultiplier", option.getExperienceMultiplier());
            table.decimal("lootMultiplier", option.getLootMultiplier());
            table.bool("autoKill", option.enableAutoKill());
            table.bool("autoSell", option.enableAutoSell());
            table.bigInt("maxEntity", option.getMaxEntity());
            table.bigInt("minDelay", option.getMinDelay());
            table.bigInt("maxDelay", option.getMaxDelay());
            table.bigInt("minSpawn", option.getMinSpawn());
            table.bigInt("maxSpawn", option.getMaxSpawn());
            table.bigInt("mobPerMinute", option.getMobPerMinute());
        });
    }


}
