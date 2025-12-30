package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommandSpawnerAdd extends VCommand {

    public CommandSpawnerAdd(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_ADD);
        this.addSubCommand("add");
        this.setDescription(Message.DESCRIPTION_GIVE);
        this.addRequireArg("entity", (a, b) -> Arrays.stream(EntityType.values()).filter(EntityType::isAlive).map(e -> e.name().toLowerCase()).collect(Collectors.toList()));
        this.addRequireArg("player");
        this.addOptionalArg("silent", (a, b) -> Arrays.asList("true", "false"));
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        EntityType entityType = this.argAsEntityType(0);
        Player player = this.argAsPlayer(1);
        boolean silent = this.argAsBoolean(2, false);

        plugin.getManager().addSpawner(this.sender, player, entityType, silent);

        return CommandType.SUCCESS;
    }

}
