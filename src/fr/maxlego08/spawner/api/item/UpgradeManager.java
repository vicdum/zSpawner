package fr.maxlego08.spawner.api.item;

import fr.maxlego08.spawner.api.Spawner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

public interface UpgradeManager {

    void loadItems();

    Map<String, UpgradeItem> getUpgradeItems();

    Optional<UpgradeItem> getUpgrade(String name);

    void give(CommandSender sender, Player player, String upgradeName);

    boolean applyUpgradeItem(Spawner spawner, Player player, ItemStack item);
}
