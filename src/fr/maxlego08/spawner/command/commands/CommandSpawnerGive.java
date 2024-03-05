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

public class CommandSpawnerGive extends VCommand {

    public CommandSpawnerGive(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_GIVE);
        this.addSubCommand("give");
        this.setDescription(Message.DESCRIPTION_GIVE);
        this.addRequireArg("type", (a, b) -> Arrays.stream(SpawnerType.values()).map(e -> e.name().toLowerCase()).collect(Collectors.toList()));
        this.addRequireArg("entity", (a, b) -> Arrays.stream(EntityType.values()).filter(EntityType::isAlive).map(e -> e.name().toLowerCase()).collect(Collectors.toList()));
        this.addRequireArg("player");
        this.addOptionalArg("silent", (a, b) -> Arrays.asList("true", "false"));
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        SpawnerType spawnerType = SpawnerType.valueOf(this.argAsString(0).toUpperCase());
        EntityType entityType = this.argAsEntityType(1);
        Player player = this.argAsPlayer(2);
        boolean silent = this.argAsBoolean(3, false);

        plugin.getManager().giveSpawner(this.sender, player, spawnerType, entityType, silent);

        return CommandType.SUCCESS;
    }

}
