package fr.maxlego08.spawner.item;

import fr.maxlego08.menu.api.MenuItemStack;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.enums.SpawnerOptionSetter;
import fr.maxlego08.spawner.api.item.UpgradeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ZUpgradeItem implements UpgradeItem {

    private final String displayName;
    private final SpawnerOptionSetter spawnerOptionSetter;
    private final Object value;
    private final Object maxValue;
    private final MenuItemStack itemStack;

    public ZUpgradeItem(@NotNull String displayName, @NotNull SpawnerOptionSetter spawnerOptionSetter, @Nullable Object value, @Nullable Object maxValue, @Nullable MenuItemStack itemStack) {
        this.displayName = displayName;
        this.spawnerOptionSetter = spawnerOptionSetter;
        this.value = value;
        this.maxValue = maxValue;
        this.itemStack = itemStack;
    }

    @Override
    public @Nullable Object getMaxValue() {
        return maxValue;
    }

    @Override
    public @Nullable MenuItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public @NotNull SpawnerOptionSetter getOption() {
        return this.spawnerOptionSetter;
    }

    @Override
    public @Nullable Object getValue() {
        return this.value;
    }

    @Override
    public boolean canApply(Spawner spawner) {
        return this.spawnerOptionSetter.canApply(spawner.getOption(), this.value, this.maxValue);
    }

    @Override
    public boolean apply(Spawner spawner) {
        this.spawnerOptionSetter.apply(spawner.getOption(), this.value);
        return true;
    }

    @Override
    public @NotNull String getDisplayName() {
        return displayName;
    }
}
