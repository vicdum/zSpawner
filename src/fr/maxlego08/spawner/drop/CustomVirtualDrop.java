package fr.maxlego08.spawner.drop;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.menu.api.utils.Placeholders;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a custom drop configuration for a virtual spawner entity.
 */
public class CustomVirtualDrop {

    private final MenuItemStack menuItemStack;
    private final double chance;
    private final int min;
    private final int max;

    public CustomVirtualDrop(MenuItemStack menuItemStack, double chance, int min, int max) {
        this.menuItemStack = menuItemStack;
        this.chance = chance;
        this.min = min;
        this.max = max;
    }

    /**
     * Generates an {@link ItemStack} based on the drop configuration.
     *
     * @param player     The player used for placeholder parsing. Can be {@code null}.
     * @param entityType The entity type associated with the drop.
     * @return An {@link Optional} containing the generated {@link ItemStack} when the drop succeeds.
     */
    public Optional<ItemStack> generate(Player player, EntityType entityType) {

        double finalChance = Math.max(0.0, this.chance);
        if (finalChance <= 0.0) {
            return Optional.empty();
        }

        if (ThreadLocalRandom.current().nextDouble(100.0) >= finalChance) {
            return Optional.empty();
        }

        Placeholders placeholders = new Placeholders();
        if (entityType != null) {
            placeholders.register("entity", entityType.name());
            placeholders.register("translation", entityType.translationKey());
        }

        ItemStack itemStack = this.menuItemStack.build(player, false, placeholders);
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

    public MenuItemStack getMenuItemStack() {
        return menuItemStack;
    }

    public double getChance() {
        return chance;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}

