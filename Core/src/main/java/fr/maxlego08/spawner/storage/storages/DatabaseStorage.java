package fr.maxlego08.spawner.storage.storages;

import fr.maxlego08.sarah.*;
import fr.maxlego08.sarah.database.DatabaseType;
import fr.maxlego08.sarah.database.Schema;
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
import fr.maxlego08.spawner.dto.ItemDTO;
import fr.maxlego08.spawner.dto.OptionDTO;
import fr.maxlego08.spawner.dto.SpawnerDTO;
import fr.maxlego08.spawner.migrations.ItemMigration;
import fr.maxlego08.spawner.migrations.OptionMigration;
import fr.maxlego08.spawner.migrations.SpawnerMigration;
import fr.maxlego08.spawner.storage.Tables;
import fr.maxlego08.spawner.zcore.utils.ElapsedTime;
import fr.maxlego08.spawner.zcore.utils.GlobalDatabaseConfiguration;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import fr.maxlego08.spawner.zcore.utils.compatibility.FoliaCompatibilityManager;
import fr.maxlego08.spawner.zcore.utils.nms.Base64ItemStack;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DatabaseStorage extends ZUtils implements IStorage {

    private final SpawnerPlugin plugin;
    private final FoliaCompatibilityManager foliaManager;
    private RequestHelper requestHelper;
    private List<Spawner> spawners = new ArrayList<>();

    public DatabaseStorage(SpawnerPlugin plugin, FoliaCompatibilityManager foliaManager) {
        this.plugin = plugin;
        this.foliaManager = foliaManager;
    }

    @Override
    public Optional<Spawner> getSpawner(Location location) {
        return this.spawners.stream().filter(spawner -> spawner.isPlace() && spawner.getCuboid().contains(location)).findFirst();
    }

    @Override
    public Optional<Spawner> getSpawner(UUID uniqueId) {
        return this.spawners.stream().filter(e -> e.getSpawnerId().equals(uniqueId)).findFirst();
    }

    @Override
    public Optional<Spawner> getSpawner(SpawnerType spawnerType, Location... locations) {
        return this.spawners.stream().filter(spawner -> {
            if (spawner.getType() != spawnerType || !spawner.isPlace()) return false;
            for (Location location : locations) {
                if (spawner.getCuboid().contains(location)) {
                    return true;
                }
            }
            return false;
        }).findFirst();
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
        this.foliaManager.runAsync(()->this.plugin.getStorage().addSpawner(spawner));
    }

    @Override
    public void removeSpawner(Location location) {
        getSpawner(location).ifPresent(this::removeSpawner);
    }

    @Override
    public void removeSpawner(Spawner spawner) {
        this.spawners.remove(spawner);
        this.foliaManager.runAsync(() -> this.deleteSpawner(spawner));
    }

    @Override
    public void load() {
        this.foliaManager.runAsync(() -> {

            this.spawners.clear();

            FileConfiguration configuration = plugin.getConfig();
            DatabaseType databaseType = DatabaseType.valueOf(configuration.getString("storage", "SQLITE"));

            DatabaseConnection databaseConnection = getDatabaseConnection(configuration, databaseType);

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

            this.foliaManager.runNextTick(() -> this.spawners.forEach(Spawner::load));
        });
    }

    private @NotNull DatabaseConnection getDatabaseConnection(FileConfiguration configuration, DatabaseType storageType) {
        GlobalDatabaseConfiguration globalDatabaseConfiguration = new GlobalDatabaseConfiguration(configuration);
        String tablePrefix = globalDatabaseConfiguration.getTablePrefix();
        String host = globalDatabaseConfiguration.getHost();
        int port = globalDatabaseConfiguration.getPort();
        String user = globalDatabaseConfiguration.getUser();
        String password = globalDatabaseConfiguration.getPassword();
        String database = globalDatabaseConfiguration.getDatabase();
        boolean debug = globalDatabaseConfiguration.isDebug();

        DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(tablePrefix, user, password, port, host, database, debug, storageType);
        DatabaseConnection databaseConnection = storageType == DatabaseType.SQLITE ? new SqliteConnection(databaseConfiguration, plugin.getDataFolder()) : new HikariDatabaseConnection(databaseConfiguration);
        databaseConnection.connect();

        if (!databaseConnection.isValid()) {
            throw new RuntimeException("Impossible to connect to database!");
        }
        return databaseConnection;
    }

    @Override
    public void save() {
        this.spawners.forEach(Spawner::disable);
        this.update(false);
    }

    @Override
    public void purge(World world, boolean destroyBlock) {
        List<Spawner> spawnersToRemove = this.spawners.stream()
                .filter(spawner -> {
                    Location location = spawner.getLocation();
                    return location != null && location.getWorld() != null && location.getWorld().equals(world);
                })
                .collect(Collectors.toList());

        spawnersToRemove.forEach(spawner -> {
            spawner.disable();
            if (destroyBlock && spawner.getLocation() != null) {
                spawner.getLocation().getBlock().setType(org.bukkit.Material.AIR);
            }
            this.removeSpawner(spawner);
        });
    }

    @Override
    public void update(boolean async) {
        Runnable runnable = () -> {

            List<Schema> schemasItems = new ArrayList<>();
            List<Schema> schemas = new ArrayList<>();
            List<Schema> schemasOptions = new ArrayList<>();

            this.spawners.forEach(spawner -> {
                if (spawner.needUpdate()) {
                    spawner.update();
                    schemas.add(SchemaBuilder.upsert(Tables.SPAWNERS, toSchema(spawner)));
                }
                SpawnerOption spawnerOption = spawner.getOption();
                if (spawnerOption.needUpdate()) {
                    spawnerOption.update();
                    schemasOptions.add(SchemaBuilder.upsert(Tables.OPTIONS, toSchema(spawner.getSpawnerId(), spawnerOption)));
                }

                for (SpawnerItem spawnerItem : spawner.getItems()) {
                    if (spawnerItem.needUpdate()) {
                        spawnerItem.update();
                        schemasItems.add(SchemaBuilder.upsert(Tables.ITEMS, table -> {
                            table.uuid("unique_id", spawnerItem.getUniqueId()).primary();
                            table.uuid("spawner_id", spawner.getSpawnerId()).primary();
                            table.string("item_stack", Base64ItemStack.encode(spawnerItem.getItemStack()));
                            table.bigInt("amount", spawnerItem.getAmount());
                        }));
                    }
                }
            });

            if (!schemas.isEmpty()) this.requestHelper.upsertMultiple(schemas);
            if (!schemasItems.isEmpty()) this.requestHelper.upsertMultiple(schemasItems);
            if (!schemasOptions.isEmpty()) this.requestHelper.upsertMultiple(schemasOptions);
        };

        if (async) this.foliaManager.runAsync(runnable);
        else this.foliaManager.runNextTick(runnable);
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
        this.foliaManager.runAsync(() -> this.deleteSpawnerItem(spawner.getSpawnerId(), Base64ItemStack.encode(spawnerItem.getItemStack())));
    }

    @Override
    public List<Spawner> getSpawners() {
        return spawners;
    }

    public void deleteSpawner(Spawner spawner) {
        this.requestHelper.delete(Tables.SPAWNERS, table -> table.where("spawner_id", spawner.getSpawnerId()));
    }

    public void upsertSpawner(Spawner spawner) {
        spawner.update();
        this.requestHelper.upsert(Tables.SPAWNERS, toSchema(spawner));
    }

    private Consumer<Schema> toSchema(Spawner spawner) {
        return table -> {
            table.uuid("owner", spawner.getOwner()).primary();
            table.uuid("spawner_id", spawner.getSpawnerId()).primary();
            table.string("location", spawner.getLocation() == null ? null : changeLocationToString(spawner.getLocation()));
            table.string("type", spawner.getType().name());
            table.bigInt("placed_at", spawner.getPlacedAt());
            table.string("entity_type", spawner.getEntityType().name());
            table.string("block_face", spawner.getBlockFace().name());
            table.bigInt("amount", spawner.getAmount());
        };
    }

    public List<Spawner> getAllSpawners() {
        var spawners = this.requestHelper.selectAll(Tables.SPAWNERS, SpawnerDTO.class);
        var options = this.requestHelper.selectAll(Tables.OPTIONS, OptionDTO.class);
        var items = this.requestHelper.selectAll(Tables.ITEMS, ItemDTO.class);

        return spawners.stream().map(spawnerDTO -> {

            Spawner spawner = new ZSpawner(this.plugin, spawnerDTO.spawner_id(), spawnerDTO.owner(), spawnerDTO.type(), spawnerDTO.entity_type(), spawnerDTO.placed_at(), spawnerDTO.location() != null ? changeStringLocationToLocation(spawnerDTO.location()) : null, spawnerDTO.amount(), spawnerDTO.block_face());

            spawner.setItems(items.stream().filter(itemDTO -> itemDTO.spawner_id().equals(spawnerDTO.spawner_id())).map(itemDTO -> new ZSpawnerItem(itemDTO.unique_id(), itemDTO.item_stack(), itemDTO.amount())).collect(Collectors.toList()));

            options.stream().filter(optionDTO -> optionDTO.spawner_id().equals(spawnerDTO.spawner_id())).findFirst().ifPresent(optionDTO -> spawner.setOption(toOption(optionDTO)));

            return spawner;
        }).collect(Collectors.toList());
    }

    public void deleteSpawnerItem(UUID spawnerId, String itemStack) {
        this.requestHelper.delete(Tables.ITEMS, table -> table.where("spawner_id", spawnerId).where("item_stack", itemStack));
    }

    public void upsertSpawnerOption(UUID spawnerId, SpawnerOption option) {
        option.update();
        this.requestHelper.upsert(Tables.OPTIONS, toSchema(spawnerId, option));
    }

    private Consumer<Schema> toSchema(UUID spawnerId, SpawnerOption option) {
        return table -> {
            table.uuid("spawner_id", spawnerId).primary();
            table.decimal("distance", option.getDistance());
            table.decimal("experience_multiplier", option.getExperienceMultiplier());
            table.decimal("loot_multiplier", option.getLootMultiplier());
            table.bool("auto_kill", option.enableAutoKill());
            table.bool("auto_sell", option.enableAutoSell());
            table.bigInt("max_entity", option.getMaxEntity());
            table.bigInt("min_delay", option.getMinDelay());
            table.bigInt("max_delay", option.getMaxDelay());
            table.bigInt("min_spawn", option.getMinSpawn());
            table.bigInt("max_spawn", option.getMaxSpawn());
            table.bigInt("mob_per_minute", option.getMobPerMinute());
            table.bigInt("remaining", option.getRemainingEntity());
        };
    }

    @Override
    public Optional<SpawnerOption> getOption(UUID uuid) {
        var options = this.requestHelper.select(Tables.OPTIONS, OptionDTO.class, table -> table.where("spawner_id", uuid));
        return options.isEmpty() ? Optional.empty() : Optional.of(toOption(options.getFirst()));
    }

    private SpawnerOption toOption(OptionDTO optionDTO) {
        return new ZSpawnerOption(optionDTO.distance(), optionDTO.experience_multiplier(), optionDTO.loot_multiplier(), optionDTO.auto_kill(), optionDTO.auto_sell(), optionDTO.max_entity(), optionDTO.min_delay(), optionDTO.max_delay(), optionDTO.min_spawn(), optionDTO.max_spawn(), optionDTO.mob_per_minute(), optionDTO.drop_loots(), optionDTO.remaining());
    }
}
