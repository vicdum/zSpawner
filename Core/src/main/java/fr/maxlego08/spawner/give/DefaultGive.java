package fr.maxlego08.spawner.give;

import fr.maxlego08.spawner.api.PlayerGive;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DefaultGive extends ZUtils implements PlayerGive {
    @Override
    public void give(Player player, ItemStack itemStack) {
        var items = player.getInventory().addItem(itemStack);
        if (items.isEmpty()) return;

        var world = player.getWorld();
        for (ItemStack value : items.values()) {
            world.dropItem(player.getLocation(), value);
        }
    }
}
