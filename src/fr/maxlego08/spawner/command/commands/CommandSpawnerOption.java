package fr.maxlego08.spawner.command.commands;

import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.api.enums.SpawnerOptionSetter;
import fr.maxlego08.spawner.command.VCommand;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.enums.Permission;
import fr.maxlego08.spawner.zcore.utils.commands.CommandType;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommandSpawnerOption extends VCommand {

    public CommandSpawnerOption(SpawnerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSPAWNER_OPTION);
        this.addSubCommand("option");
        this.setDescription(Message.DESCRIPTION_GIVE);

        this.addRequireArg("player");
        this.addRequireArg("spawner", (sender, args) -> getSpawners(args, 1, plugin, SpawnerType.VIRTUAL));
        this.addRequireArg("option", (sender, args) -> Arrays.stream(SpawnerOptionSetter.values()).map(e -> e.name().toLowerCase()).collect(Collectors.toList()));
        this.addRequireArg("value", (sender, args) -> {
            try {
                SpawnerOptionSetter spawnerOptionSetter = SpawnerOptionSetter.valueOf(args[3].toUpperCase());
                if (spawnerOptionSetter.getType() == Boolean.class) {
                    return Arrays.asList("true", "false");
                } else {
                    switch (spawnerOptionSetter) {
                        case LOOT_MULTIPLIER:
                        case EXPERIENCE_MULTIPLIER:
                            return IntStream.rangeClosed(10, 30).mapToObj(i -> String.format("%d,%d", i / 10, i % 10)).collect(Collectors.toList());
                        case MIN_DELAY:
                        case MAX_DELAY:
                        case MAX_ENTITY:
                            return IntStream.rangeClosed(1, 30).mapToObj(i -> String.valueOf(i * 1000)).collect(Collectors.toList());
                        case DISTANCE:
                            return Arrays.asList("1", "2", "4", "6", "8", "10", "12", "14", "16", "18", "20");
                        case MAX_SPAWN:
                        case MIN_SPAWN:
                            return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
                        case MOB_PER_MINUTE:
                        default:
                            return Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
                    }
                }
            } catch (Exception exception) {
                return new ArrayList<>();
            }
        });
    }

    @Override
    protected CommandType perform(SpawnerPlugin plugin) {

        OfflinePlayer offlinePlayer = this.argAsOfflinePlayer(0);
        String spawnerKey = this.argAsString(1);
        SpawnerOptionSetter spawnerOptionSetter = SpawnerOptionSetter.valueOf(this.argAsString(2).toUpperCase());
        String value = this.argAsString(3);

        Optional<Spawner> optional = plugin.getStorage().getSpawners(offlinePlayer).stream().filter(e -> e.getSpawnerKey().equals(spawnerKey)).findFirst();
        if (!optional.isPresent()) {
            message(this.plugin, this.sender, Message.COMMAND_SPAWNER_NOT_FOUND, "%spawnerKey%", spawnerKey);
            return CommandType.DEFAULT;
        }

        Spawner spawner = optional.get();
        if (spawner.getType() != SpawnerType.VIRTUAL) {
            message(this.plugin, this.sender, Message.COMMAND_SPAWNER_TYPE);
            return CommandType.DEFAULT;
        }

        if (!spawnerOptionSetter.apply(spawner.getOption(), value)) {
            return CommandType.SYNTAX_ERROR;
        }

        message(this.plugin, this.sender, Message.COMMAND_OPTION_SUCCESS, "%spawnerKey%", spawnerKey, "%name%", name(spawnerOptionSetter.name()), "%value%", value, "%player%", player.getName());

        return CommandType.SUCCESS;
    }

}
