package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.storage.ZSpawnerLocationHistory;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.Optional;

public class CommandSpawnerLocationSet extends VCommand {

    public CommandSpawnerLocationSet(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_LOCATION_ADMIN);
        this.addSubCommand("set");
        this.setDescription(Message.DESCRIPTION_LOCATION_SET);
        this.addRequireArg("owner");
        this.addRequireArg("spawner", (sender, args) -> getSpawners(args, 2, plugin, SpawnerType.VIRTUAL));
        this.addRequireArg("renter");
        this.addRequireArg("minutes");
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        OfflinePlayer owner = this.argAsOfflinePlayer(0);
        String spawnerKey = this.argAsString(1);
        OfflinePlayer renter = this.argAsOfflinePlayer(2);
        int minutes = this.argAsInteger(3);

        if (minutes <= 0) {
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_INVALID_TIME);
            return CommandType.DEFAULT;
        }

        Collection<Spawner> spawners = plugin.getServerDataManager().getOrCreate().getSpawners(owner.getUniqueId());
        Optional<Spawner> optional = Optional.empty();
        for (Spawner spawner : spawners) {
            if (spawner.getSpawnerKey().equals(spawnerKey)) {
                optional = Optional.of(spawner);
                break;
            }
        }

        if (optional.isEmpty()) {
            message(this.plugin, this.sender, Message.COMMAND_SPAWNER_NOT_FOUND, "%spawnerKey%", spawnerKey);
            return CommandType.DEFAULT;
        }

        Spawner spawner = optional.get();
        if (spawner.getType() != SpawnerType.VIRTUAL) {
            message(this.plugin, this.sender, Message.COMMAND_SPAWNER_TYPE);
            return CommandType.DEFAULT;
        }

        if (!spawner.getOption().isLocationEnabled()) {
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_NOT_ENABLED);
            return CommandType.DEFAULT;
        }

        spawner.setLastLocationUser(renter.getUniqueId());
        long startTime = System.currentTimeMillis();
        spawner.setLastLocationStartTime(startTime);
        long durationMs = minutes * 60000L; // Convert minutes to milliseconds
        spawner.setLastLocationTime(durationMs);

        ZSpawnerLocationHistory history = new ZSpawnerLocationHistory(this.plugin.getStorageManager(), spawner.getSpawnerId(), startTime, durationMs, renter.getUniqueId(), 0);
        spawner.addLocationHistory(history);
        history.save();

        message(this.plugin, this.sender, Message.COMMAND_LOCATION_SET_SUCCESS,
                "%spawnerKey%", spawnerKey,
                "%owner%", owner.getName(),
                "%renter%", renter.getName(),
                "%minutes%", minutes);

        return CommandType.SUCCESS;
    }
}
