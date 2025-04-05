package fr.maxlego08.spawner.api.item;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.enums.SpawnerOptionSetter;

public interface UpgradeItem {

    /**
     * Returns the {@link SpawnerOptionSetter} associated to this item.
     *
     * @return The {@link SpawnerOptionSetter} associated to this item.
     */
    SpawnerOptionSetter getOption();

    /**
     * Returns the {@link MenuItemStack} associated to this item.
     *
     * @return The {@link MenuItemStack} associated to this item.
     */
    MenuItemStack getItemStack();

    /**
     * Returns the value associated with this item. This value is used to apply the option to the spawner.
     *
     * @return The value associated with this item.
     */
    Object getValue();

    /**
     * Returns the maximum value of this item. This value is used to check if the current value of the option is
     * already at the maximum value.
     *
     * @return The maximum value of this item.
     */
    Object getMaxValue();

    /**
     * Determines if this upgrade item can be applied to the specified spawner.
     *
     * @param spawner The spawner to check for applicability.
     * @return true if the upgrade can be applied, false otherwise.
     */
    boolean canApply(Spawner spawner);

    /**
     * Applies the upgrade to the specified spawner.
     *
     * @param spawner The spawner to which the upgrade will be applied.
     * @return true if the upgrade was successfully applied, false otherwise.
     */
    boolean apply(Spawner spawner);

    /**
     * Returns the display name of this item.
     *
     * @return The display name of this item.
     */
    String getDisplayName();
}
