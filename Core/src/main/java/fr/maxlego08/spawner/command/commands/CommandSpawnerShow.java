package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandSpawnerShow extends VCommand {

    public CommandSpawnerShow(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_SHOW);
        this.addSubCommand("show");
        this.setDescription(Message.DESCRIPTION_SHOW);
        this.addOptionalArg("player");
        this.addOptionalArg("page");
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0, null);
        int page = this.argAsInteger(1, 1);

        plugin.getManager().showSpawners(this.player, offlinePlayer, page);

        return CommandType.SUCCESS;
    }

}
