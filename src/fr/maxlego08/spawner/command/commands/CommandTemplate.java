package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;

public class CommandTemplate extends VCommand {

	public CommandTemplate(SpawnerPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.EXAMPLE_PERMISSION);
		this.addSubCommand(new CommandTemplateReload(plugin));
	}

	@Override
	protected CommandType perform(SpawnerPlugin plugin) {
		syntaxMessage();
		return CommandType.SUCCESS;
	}

}
