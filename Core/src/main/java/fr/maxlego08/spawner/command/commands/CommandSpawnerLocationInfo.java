package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CommandSpawnerLocationInfo extends VCommand {

    public CommandSpawnerLocationInfo(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_LOCATION_ADMIN);
        this.addSubCommand("info");
        this.setDescription(Message.DESCRIPTION_LOCATION_INFO);
        this.addRequireArg("owner");
        this.addRequireArg("spawner", (sender, args) -> getSpawners(args, 2, plugin, SpawnerType.VIRTUAL));
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        OfflinePlayer owner = this.argAsOfflinePlayer(0);
        String spawnerKey = this.argAsString(1);

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

        message(this.plugin, this.sender, Message.COMMAND_LOCATION_INFO_HEADER,
                "%spawnerKey%", spawnerKey,
                "%owner%", owner.getName());

        message(this.plugin, this.sender, Message.COMMAND_LOCATION_INFO_ENABLED,
                "%enabled%", spawner.getOption().isLocationEnabled() ? "Yes" : "No");

        if (spawner.getLastLocationUser() != null) {
            OfflinePlayer renter = Bukkit.getOfflinePlayer(spawner.getLastLocationUser());
            long currentTime = System.currentTimeMillis();
            long startTime = spawner.getLastLocationStartTime();
            long duration = spawner.getLastLocationTime();
            long endTime = startTime + duration;
            boolean isActive = endTime > currentTime;

            message(this.plugin, this.sender, Message.COMMAND_LOCATION_INFO_RENTER,
                    "%renter%", renter.getName());
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_INFO_STATUS,
                    "%status%", isActive ? "&aActive" : "&cExpired");

            if (isActive) {
                long remainingMs = endTime - currentTime;
                long remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(remainingMs);
                long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(remainingMs) % 60;

                message(this.plugin, this.sender, Message.COMMAND_LOCATION_INFO_REMAINING,
                        "%time%", String.format("%d min %d sec", remainingMinutes, remainingSeconds));
            }
        } else {
            message(this.plugin, this.sender, Message.COMMAND_LOCATION_INFO_NO_RENTER);
        }

        message(this.plugin, this.sender, Message.COMMAND_LOCATION_INFO_CONFIG,
                "%min%", spawner.getOption().getMinLocationTime(),
                "%max%", spawner.getOption().getMaxLocationTime(),
                "%price%", spawner.getOption().getLocationPrice());

        return CommandType.SUCCESS;
    }
}

