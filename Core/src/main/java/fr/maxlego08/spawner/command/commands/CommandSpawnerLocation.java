package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;

public class CommandSpawnerLocation extends VCommand {

    public CommandSpawnerLocation(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_LOCATION_ADMIN);
        this.addSubCommand("location", "loc");
        this.setDescription(Message.DESCRIPTION_LOCATION);
        this.addSubCommand(new CommandSpawnerLocationSet(plugin));
        this.addSubCommand(new CommandSpawnerLocationAdd(plugin));
        this.addSubCommand(new CommandSpawnerLocationRemove(plugin));
        this.addSubCommand(new CommandSpawnerLocationInfo(plugin));
        this.addSubCommand(new CommandSpawnerLocationClear(plugin));
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {
        syntaxMessage();
        return CommandType.SUCCESS;
    }
}

