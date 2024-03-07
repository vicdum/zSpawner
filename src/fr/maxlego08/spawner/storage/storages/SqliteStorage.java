package fr.maxlego08.spawner.storage.storages;

import fr.maxlego08.menu.zcore.utils.nms.ItemStackUtils;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.ZSpawner;
import fr.maxlego08.spawner.ZSpawnerItem;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerItem;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.zcore.ZPlugin;
import fr.maxlego08.spawner.zcore.logger.Logger;
import fr.maxlego08.spawner.zcore.utils.ElapsedTime;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SqliteStorage extends ZUtils implements IStorage {

    private final String spawnerTableName;
    private final String spawnerItemTableName;
    private final SpawnerPlugin plugin;
    protected Connection connection;
    private List<Spawner> spawners = new ArrayList<>();
    private File databaseFile = null;

    public SqliteStorage(SpawnerPlugin plugin, boolean createFile) {
        this.plugin = plugin;

        FileConfiguration configuration = plugin.getConfig();
        String tablePrefix = configuration.getString("sql.tablePrefix", "zspawner");
        this.spawnerTableName = tablePrefix + "_spawners";
        this.spawnerItemTableName = tablePrefix + "_items";

        if (createFile) {
            this.databaseFile = new File(plugin.getDataFolder(), "database.db");
            if (!databaseFile.exists()) {
                try {
                    databaseFile.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

    }

    @Override
    public Optional<Spawner> getSpawner(Location location) {
        return this.spawners.stream().filter(spawner -> spawner.isPlace() && spawner.getCuboid().contains(location)).findFirst();
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

            this.create();
            this.createSpawnerItemsTable();
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
        this.update();
        this.disconnect();
    }

    @Override
    public void purge(World world, boolean destroyBlock) {
        // ToDo
    }

    @Override
    public void update() {
        ZPlugin.service.execute(() -> this.spawners.stream().filter(Spawner::needUpdate).forEach(spawner -> {
            this.upsertSpawner(spawner);
            spawner.getItems().stream().filter(SpawnerItem::needUpdate).forEach(spawnerItem -> this.upsertSpawnerItem(spawner.getSpawnerId(), ItemStackUtils.serializeItemStack(spawnerItem.getItemStack()), spawnerItem.getAmount()));
        }));
    }

    @Override
    public List<Spawner> getSpawners(SpawnerType spawnerType) {
        return this.spawners.stream().filter(spawner -> spawner.getType() == spawnerType).collect(Collectors.toList());
    }

    @Override
    public long countSpawners(OfflinePlayer player, SpawnerType spawnerType) {
        return getSpawners(player).stream().filter(spawner -> spawner.getType() == spawnerType).count();
    }

    public void disconnect() {
        try {
            if (isConnected()) connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed() && connection.isValid(1);
    }

    public void connection() {
        try {
            if (!isConnected()) {
                connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Connection getConnection() {
        this.connection();
        return connection;
    }

    public void create() {

        String createSpawnersTableSQL = "CREATE TABLE IF NOT EXISTS " + this.spawnerTableName + " (" + "owner UUID, " + "spawnerId UUID, " + "location TEXT, " + "type TEXT, " + "placedAt LONG, " + "level TEXT, " + "entityType TEXT, " + "blockFace TEXT, " + "amount INTEGER, " + "PRIMARY KEY (spawnerId), " + "UNIQUE(owner, spawnerId));";

        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(createSpawnersTableSQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.info("Could not create spawners table: " + exception.getMessage(), Logger.LogType.ERROR);
        }
    }

    public void createSpawnerItemsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + this.spawnerItemTableName + " (" + "spawnerId UUID NOT NULL, " + "itemStack TEXT NOT NULL, " + "amount LONG NOT NULL, " + "PRIMARY KEY (spawnerId, itemStack), " + "FOREIGN KEY (spawnerId) REFERENCES " + this.spawnerTableName + "(spawnerId) ON DELETE CASCADE);";
        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.info("Could not create " + this.spawnerItemTableName + " table: " + exception.getMessage(), Logger.LogType.ERROR);
        }
    }


    public void deleteSpawner(Spawner spawner) {
        String sql = "DELETE FROM " + this.spawnerTableName + " WHERE spawnerId = ?";
        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, spawner.getSpawnerId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.info("Error deleting spawner from SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
        }
    }

    public void upsertSpawner(Spawner spawner) {
        spawner.update();
        String sql = "INSERT INTO " + this.spawnerTableName + "(owner, spawnerId, location, type, placedAt, level, entityType, amount, blockFace) " + "VALUES(?,?,?,?,?,?,?,?,?) " + "ON CONFLICT(owner, spawnerId) DO UPDATE SET " + "location = EXCLUDED.location, " + "type = EXCLUDED.type, " + "placedAt = EXCLUDED.placedAt, " + "level = EXCLUDED.level, " + "entityType = EXCLUDED.entityType, " + "blockFace = EXCLUDED.blockFace, " + "amount = EXCLUDED.amount;";
        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, spawner.getOwner().toString());
            preparedStatement.setString(2, spawner.getSpawnerId().toString());

            if (spawner.getLocation() == null) {
                preparedStatement.setNull(3, java.sql.Types.VARCHAR);
            } else {
                preparedStatement.setString(3, changeLocationToString(spawner.getLocation()));
            }
            preparedStatement.setString(4, spawner.getType().toString());
            preparedStatement.setLong(5, spawner.getPlacedAt());
            preparedStatement.setString(6, spawner.getLevel().getName());
            preparedStatement.setString(7, spawner.getEntityType().name());
            preparedStatement.setInt(8, spawner.getAmount());
            preparedStatement.setString(9, spawner.getBlockFace().name());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.info("Error upserting spawner in SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
        }
    }

    public void upsertSpawnerItem(UUID spawnerId, String itemStack, long amount) {
        String sql = "INSERT INTO " + this.spawnerItemTableName + " (spawnerId, itemStack, amount) VALUES (?, ?, ?) " + "ON CONFLICT(spawnerId, itemStack) DO UPDATE SET " + "amount = EXCLUDED.amount;";
        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, spawnerId.toString());
            preparedStatement.setString(2, itemStack);
            preparedStatement.setLong(3, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.info("Error upserting spawner item in SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
        }
    }


    public List<Spawner> getAllSpawners() {
        List<Spawner> spawners = new ArrayList<>();
        String sql = "SELECT * FROM " + this.spawnerTableName;
        try (Statement statement = this.getConnection().createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {

                UUID owner = UUID.fromString(resultSet.getString("owner"));
                UUID spawnerId = UUID.fromString(resultSet.getString("spawnerId"));
                String location = resultSet.getString("location");
                String type = resultSet.getString("type");
                long placedAt = resultSet.getLong("placedAt");
                String level = resultSet.getString("level");
                String entityType = resultSet.getString("entityType");
                int amount = resultSet.getInt("amount");
                String blockFace = resultSet.getString("blockFace");

                Spawner spawner = new ZSpawner(this.plugin, spawnerId, owner, SpawnerType.valueOf(type), EntityType.valueOf(entityType), placedAt, this.plugin.getManager().getSpawnerLevel(level), location != null ? changeStringLocationToLocation(location) : null, amount, BlockFace.valueOf(blockFace));
                spawners.add(spawner);

                spawner.setItems(getSpawnerItems(spawnerId));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return spawners;
    }

    public void deleteSpawnerItems(UUID spawnerId) {
        String sql = "DELETE FROM " + this.spawnerItemTableName + " WHERE spawnerId = ?";
        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, spawnerId.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.info("Error deleting spawner items from SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
        }
    }

    public List<SpawnerItem> getSpawnerItems(UUID spawnerId) {
        List<SpawnerItem> items = new ArrayList<>();
        String sql = "SELECT itemStack, amount FROM " + this.spawnerItemTableName + " WHERE spawnerId = ?";
        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, spawnerId.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                items.add(new ZSpawnerItem(resultSet.getString("itemStack"), resultSet.getLong("amount")));
            }
        } catch (SQLException exception) {
            Logger.info("Error retrieving spawner items from SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
        }
        return items;
    }

    public void deleteSpawnerItem(UUID spawnerId, String itemStack) {
        String sql = "DELETE FROM " + this.spawnerItemTableName + " WHERE spawnerId = ? AND itemStack = ?";
        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, spawnerId.toString());
            preparedStatement.setString(2, itemStack);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.info("Error deleting specific spawner item from SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
        }
    }


}
