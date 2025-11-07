package fr.maxlego08.spawner.give;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.spawner.api.PlayerGive;
import fr.maxlego08.spawner.zcore.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ZEssentialsGive implements PlayerGive {
    @Override
    public void give(@NotNull Player player, @NotNull ItemStack itemStack) {
        EssentialsPlugin essentialsPlugin = (EssentialsPlugin) Bukkit.getPluginManager().getPlugin("zEssentials");
        if (essentialsPlugin == null) {
            Logger.info("Impossible to find zEssentials plugin", Logger.LogType.ERROR);
            return;
        }
        essentialsPlugin.give(player, itemStack);
    }
}
