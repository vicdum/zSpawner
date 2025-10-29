package fr.maxlego08.spawner.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PlayerGive {

    /**
     * Gives an item to the player, if the player's inventory is full then the
     * item will drop to the ground
     *
     * @param player    the player to give the item to
     * @param itemStack the item to give
     */
    void give(Player player, ItemStack itemStack);

}
