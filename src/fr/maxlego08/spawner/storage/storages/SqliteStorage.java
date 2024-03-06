package fr.maxlego08.spawner.storage.storages;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.ZSpawner;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.zcore.ZPlugin;
import fr.maxlego08.spawner.zcore.logger.Logger;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SqliteStorage extends ZUtils implements IStorage {

    private final String spawnerTableName;
    private final SpawnerPlugin plugin;
    protected Connection connection;
    private List<Spawner> spawners = new ArrayList<>();
    private File databaseFile = null;

    public SqliteStorage(SpawnerPlugin plugin, boolean createFile) {
        this.plugin = plugin;

        FileConfiguration configuration = plugin.getConfig();
        String tablePrefix = configuration.getString("sql.tablePrefix", "zspawner");
        this.spawnerTableName = tablePrefix + "_spawners";

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
        return this.spawners.stream().filter(spawner -> spawner.isPlace() && spawner.getLocation().equals(location)).findFirst();
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
    public long countSpawners(int x, int z) {
        return this.spawners.stream().filter(spawner -> spawner.sameChunk(x, z)).count();
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
            this.spawners = this.getAllSpawners();

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
        ZPlugin.service.execute(() -> this.spawners.stream().filter(Spawner::needUpdate).forEach(this::upsertSpawner));
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

        String createSpawnersTableSQL = "CREATE TABLE IF NOT EXISTS " + this.spawnerTableName + " (" + "owner UUID, " + "spawnerId UUID, " + "location TEXT, " + "type TEXT, " + "placedAt LONG, " + "level TEXT, " + "entityType TEXT, " + "amount INTEGER, " + "PRIMARY KEY (spawnerId), " + "UNIQUE(owner, spawnerId));";

        try (PreparedStatement preparedStatement = this.getConnection().prepareStatement(createSpawnersTableSQL)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.info("Could not create spawners table: " + exception.getMessage(), Logger.LogType.ERROR);
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
        String sql = "INSERT INTO " + this.spawnerTableName + "(owner, spawnerId, location, type, placedAt, level, entityType, amount) " + "VALUES(?,?,?,?,?,?,?,?) " + "ON CONFLICT(owner, spawnerId) DO UPDATE SET " + "location = EXCLUDED.location, " + "type = EXCLUDED.type, " + "placedAt = EXCLUDED.placedAt, " + "level = EXCLUDED.level, " + "entityType = EXCLUDED.entityType, " + "amount = EXCLUDED.amount;";
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
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            Logger.info("Error upserting spawner in SQLite: " + exception.getMessage(), Logger.LogType.ERROR);
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

                Spawner spawner = new ZSpawner(this.plugin, spawnerId, owner, SpawnerType.valueOf(type), EntityType.valueOf(entityType), placedAt, this.plugin.getManager().getSpawnerLevel(level), location != null ? changeStringLocationToLocation(location) : null, amount);
                spawners.add(spawner);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return spawners;
    }


}
