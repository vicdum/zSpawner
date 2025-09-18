package fr.maxlego08.spawner.drop;

import fr.maxlego08.menu.api.MenuItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a custom drop configuration for a virtual spawner entity.
 */
public record CustomVirtualDrop(MenuItemStack menuItemStack, double chance, int min, int max) {

    /**
     * Generates an {@link ItemStack} based on the drop configuration.
     *
     * @param player     The player used for placeholder parsing. Can be {@code null}.
     * @return An {@link Optional} containing the generated {@link ItemStack} when the drop succeeds.
     */
    public Optional<ItemStack> generate(Player player) {

        double finalChance = Math.max(0.0, this.chance);
        if (finalChance <= 0.0) {
            return Optional.empty();
        }

        if (ThreadLocalRandom.current().nextDouble(100.0) >= finalChance) {
            return Optional.empty();
        }

        ItemStack itemStack = this.menuItemStack.build(player, false);
        if (itemStack == null) {
            return Optional.empty();
        }

        int minAmount = Math.min(this.min, this.max);
        int maxAmount = Math.max(this.min, this.max);

        if (maxAmount <= 0) {
            maxAmount = 1;
        }
        if (minAmount <= 0) {
            minAmount = 1;
        }

        int amount = minAmount;
        if (maxAmount > minAmount) {
            amount = ThreadLocalRandom.current().nextInt(maxAmount - minAmount + 1) + minAmount;
        }

        amount = Math.min(amount, itemStack.getMaxStackSize());
        if (amount <= 0) {
            amount = 1;
        }

        itemStack.setAmount(amount);
        return Optional.of(itemStack);
    }
}

