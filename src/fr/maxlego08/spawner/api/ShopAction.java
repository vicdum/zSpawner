package fr.maxlego08.spawner.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ShopAction {

    double getSellPrice(Player player, ItemStack itemStack, long amount);

    boolean deposit(Player player, ItemStack itemStack, long amount);

}
