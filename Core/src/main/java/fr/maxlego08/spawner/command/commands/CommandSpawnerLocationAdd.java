package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerManager;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerLocationHistory;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.Optional;

public class CommandSpawnerLocationAdd extends VCommand {
    private final SpawnerManager spawnerManager;

    public CommandSpawnerLocationAdd(SpawnerPlugin plugin) {
        super(plugin);
        this.spawnerManager = plugin.getManager();
        this.setPermission(Permission.ZSPAWNER_LOCATION_ADMIN);
        this.addSubCommand("add");
        this.setDescription(Message.DESCRIPTION_LOCATION_ADD);
        this.addRequireArg("owner");
        this.addRequireArg("spawner", (sender, args) -> getSpawners(args, 2, plugin, SpawnerType.VIRTUAL));
        this.addRequireArg("minutes");
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        OfflinePlayer owner = this.argAsOfflinePlayer(0);
        String spawnerKey = this.argAsString(1);
        int minutes = this.argAsInteger(2);

        if (minutes == 0) {
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

        if (spawner.getLastLocationUser() == null) {
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_NO_RENTER);
            return CommandType.DEFAULT;
        }

        boolean isActive = spawner.getLastLocationStartTime() + spawner.getLastLocationTime() > System.currentTimeMillis();
        if (!isActive) {
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_EXPIRED);
            return CommandType.DEFAULT;
        }

        long additionalTime = minutes * 60000L; // Convert minutes to milliseconds
        long lastLocationTime = spawner.getLastLocationTime() + additionalTime;
        spawner.setLastLocationTime(lastLocationTime);

        SpawnerLocationHistory last = spawner.getLocationHistory().getLast();
        if (last != null) {
            last.setDuration(lastLocationTime);
        }

        String action = minutes > 0 ? "added" : "removed";
        message(this.plugin, this.sender, Message.COMMAND_LOCATION_TIME_MODIFIED,
                "%spawnerKey%", spawnerKey,
                "%owner%", owner.getName(),
                "%action%", action,
                "%minutes%", Math.abs(minutes));

        return CommandType.SUCCESS;
    }
}

