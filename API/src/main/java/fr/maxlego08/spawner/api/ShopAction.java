package fr.maxlego08.spawner.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ShopAction {

    /**
     * Returns the price for which the given amount of the given itemStack
     * can be sold to the shop. This price is given in terms of the
     * currency used by the shop.
     *
     * @param player the player who is selling
     * @param itemStack the ItemStack to sell
     * @param amount the amount to sell
     * @return the price for which the given amount can be sold
     */
    double getSellPrice(Player player, ItemStack itemStack, long amount);

    /**
     * Deposits the given amount of the given itemStack to the shop storage.
     *
     * @param player the player who is depositing
     * @param itemStack the ItemStack to deposit
     * @param amount the amount to deposit
     * @return true if the deposit was successful, false if there was a problem
     */
    boolean deposit(@NotNull Player player, @NotNull ItemStack itemStack, long amount);

}
