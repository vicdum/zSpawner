package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;
import org.bukkit.entity.Player;

public class CommandSpawner extends VCommand {

    public CommandSpawner(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_USE);
        this.addSubCommand(new CommandSpawnerReload(plugin));
        this.addSubCommand(new CommandSpawnerGive(plugin));
        this.addSubCommand(new CommandSpawnerHelp(plugin));
        this.addSubCommand(new CommandSpawnerAdd(plugin));
        this.addSubCommand(new CommandSpawnerOption(plugin));
        this.addSubCommand(new CommandSpawnerRemove(plugin));
        this.addSubCommand(new CommandSpawnerShow(plugin));
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        if (this.sender instanceof Player) plugin.getManager().openSpawner(this.player, 1);
        else syntaxMessage();

        return CommandType.SUCCESS;
    }

}
