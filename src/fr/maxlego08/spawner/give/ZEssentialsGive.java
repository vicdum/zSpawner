package fr.maxlego08.spawner.give;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.spawner.api.PlayerGive;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ZEssentialsGive implements PlayerGive {
    @Override
    public void give(Player player, ItemStack itemStack) {
        EssentialsPlugin essentialsPlugin = (EssentialsPlugin) Bukkit.getPluginManager().getPlugin("zEssentials");
        if (essentialsPlugin == null) {
            System.err.println("Impossible to find zEssentials plugin");
            return;
        }
        essentialsPlugin.give(player, itemStack);
    }
}
