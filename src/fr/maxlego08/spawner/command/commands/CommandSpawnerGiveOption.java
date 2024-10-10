package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandSpawnerGiveOption extends VCommand {

    public CommandSpawnerGiveOption(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_GIVE_OPTION);
        this.addSubCommand("giveoption", "go");
        this.setDescription(Message.DESCRIPTION_GIVE_OPTION);
        this.addRequireArg("player");
        this.addRequireArg("option", (a, b) -> new ArrayList<>(plugin.getUpgradeManager().getUpgradeItems().keySet()));
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String upgradeName = this.argAsString(1);

        plugin.getUpgradeManager().give(sender, player, upgradeName);

        return CommandType.SUCCESS;
    }

}
