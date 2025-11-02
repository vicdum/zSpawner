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

public class CommandSpawnerLocationClear extends VCommand {

    public CommandSpawnerLocationClear(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_LOCATION_ADMIN);
        this.addSubCommand("clear");
        this.setDescription(Message.DESCRIPTION_LOCATION_CLEAR);
        this.addRequireArg("owner");
        this.addRequireArg("spawner", (sender, args) -> getSpawners(args, 2, plugin, SpawnerType.VIRTUAL));
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        OfflinePlayer owner = this.argAsOfflinePlayer(0);
        String spawnerKey = this.argAsString(1);

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

        // Clear the rental
        spawner.setLastLocationUser(null);
        spawner.setLastLocationStartTime(0);
        spawner.setLastLocationTime(0);

        message(this.plugin, this.sender, Message.COMMAND_LOCATION_CLEARED,
                "%spawnerKey%", spawnerKey,
                "%owner%", owner.getName());

        return CommandType.SUCCESS;
    }
}

