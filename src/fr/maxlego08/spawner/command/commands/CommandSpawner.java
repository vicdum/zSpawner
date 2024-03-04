package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;

public class CommandSpawner extends VCommand {

	public CommandSpawner(SpawnerPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.ZSPAWNER_USE);
		this.addSubCommand(new CommandSpawnerReload(plugin));
	}

	@Override
	protected CommandType perform(SpawnerPlugin plugin) {
		syntaxMessage();
		return CommandType.SUCCESS;
	}

}
