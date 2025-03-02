package fr.maxlego08.spawner.api.item;

import fr.maxlego08.spawner.api.Spawner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

public interface UpgradeManager {

    /**
     * Loads all items from the configuration file
     */
    void loadItems();

    /**
     * Returns a map of all upgrade items
     *
     * @return a map of all upgrade items
     */
    Map<String, UpgradeItem> getUpgradeItems();

    /**
     * Returns an optional of an upgrade item
     *
     * @param name the name of the upgrade item
     * @return an optional of an upgrade item
     */
    Optional<UpgradeItem> getUpgrade(String name);

    /**
     * Gives an upgrade item to a player
     *
     * @param sender      the command sender who executes the command
     * @param player      the player who receives the upgrade item
     * @param upgradeName the name of the upgrade item
     */
    void give(CommandSender sender, Player player, String upgradeName);

    /**
     * Applies an upgrade item to a spawner
     *
     * @param spawner   the spawner
     * @param player    the player who applies the upgrade item
     * @param itemStack the item stack that contains the upgrade item
     * @return true if the upgrade item was applied, false otherwise
     */
    boolean applyUpgradeItem(Spawner spawner, Player player, ItemStack itemStack);
}
