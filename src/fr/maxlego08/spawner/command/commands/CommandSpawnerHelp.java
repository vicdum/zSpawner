package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;

public class CommandSpawnerHelp extends VCommand {

	public CommandSpawnerHelp(SpawnerPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.ZSPAWNER_HELP);
		this.addSubCommand("help");
		this.setDescription(Message.DESCRIPTION_HELP);
	}

	@Override
	protected CommandType perform(SpawnerPlugin plugin) {
		this.parent.syntaxMessage();
		return CommandType.SUCCESS;
	}

}
