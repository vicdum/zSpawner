package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.Optional;

public class CommandSpawnerRemove extends VCommand {

    public CommandSpawnerRemove(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_REMOVE);
        this.addSubCommand("remove");
        this.setDescription(Message.DESCRIPTION_REMOVE);
        this.addRequireArg("player");
        this.addRequireArg("spawner", (sender, args) -> getSpawners(args, 1, plugin, SpawnerType.GUI));
        this.addOptionalArg("silent", (a, b) -> Arrays.asList("true", "false"));
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        String spawnerKey = this.argAsString(1);
        boolean silent = this.argAsBoolean(2, false);

        Optional<Spawner> optional = plugin.getStorage().getSpawners(offlinePlayer).stream().filter(e -> e.getSpawnerKey().equals(spawnerKey)).findFirst();
        if (!optional.isPresent()) {
            message(this.plugin, this.sender, Message.COMMAND_SPAWNER_NOT_FOUND, "%spawnerKey%", spawnerKey);
            return CommandType.DEFAULT;
        }
        Spawner spawner = optional.get();
        if (spawner.getType() != SpawnerType.GUI) {
            message(this.plugin, this.sender, Message.COMMAND_SPAWNER_TYPE);
            return CommandType.DEFAULT;
        }

        plugin.getManager().removeSpawner(this.sender, player, spawner, silent);

        return CommandType.SUCCESS;
    }

}
