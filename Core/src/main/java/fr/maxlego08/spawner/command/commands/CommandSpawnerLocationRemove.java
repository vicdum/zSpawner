package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.Optional;

public class CommandSpawnerLocationRemove extends VCommand {

    public CommandSpawnerLocationRemove(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_LOCATION_ADMIN);
        this.addSubCommand("remove", "rem");
        this.setDescription(Message.DESCRIPTION_LOCATION_REMOVE);
        this.addRequireArg("owner");
        this.addRequireArg("spawner", (sender, args) -> getSpawners(args, 2, plugin, SpawnerType.VIRTUAL));
        this.addRequireArg("minutes");
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        OfflinePlayer owner = this.argAsOfflinePlayer(0);
        String spawnerKey = this.argAsString(1);
        int minutes = this.argAsInteger(2);

        if (minutes <= 0) {
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_INVALID_TIME);
            return CommandType.DEFAULT;
        }

        List<Spawner> spawners = plugin.getStorage().getSpawners(owner);
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

        if (spawner.getLastLocationUser() == null) {
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_NO_RENTER);
            return CommandType.DEFAULT;
        }

        // Check if location is still active
        boolean isActive = spawner.getLastLocationStartTime() + spawner.getLastLocationTime() > System.currentTimeMillis();
        if (!isActive) {
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_EXPIRED);
            return CommandType.DEFAULT;
        }

        // Remove time from the current rental
        long removeTime = minutes * 60000L; // Convert minutes to milliseconds
        long newTime = spawner.getLastLocationTime() - removeTime;

        if (newTime <= 0) {
            // If removing more time than remaining, clear the rental
            spawner.setLastLocationUser(null);
            spawner.setLastLocationStartTime(0);
            spawner.setLastLocationTime(0);
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_CLEARED,
                    "%spawnerKey%", spawnerKey,
                    "%owner%", owner.getName());
        } else {
            spawner.setLastLocationTime(newTime);
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_TIME_MODIFIED,
                    "%spawnerKey%", spawnerKey,
                    "%owner%", owner.getName(),
                    "%action%", "removed",
                    "%minutes%", minutes);
        }

        return CommandType.SUCCESS;
    }
}

