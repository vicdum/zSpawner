package fr.maxlego08.spawner.item;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.enums.SpawnerOptionSetter;
import fr.maxlego08.spawner.api.item.UpgradeItem;

public class ZUpgradeItem implements UpgradeItem {

    private final SpawnerOptionSetter spawnerOptionSetter;
    private final Object value;
    private final Object maxValue;
    private final MenuItemStack itemStack;

    public ZUpgradeItem(SpawnerOptionSetter spawnerOptionSetter, Object value, Object maxValue, MenuItemStack itemStack) {
        this.spawnerOptionSetter = spawnerOptionSetter;
        this.value = value;
        this.maxValue = maxValue;
        this.itemStack = itemStack;
    }

    public SpawnerOptionSetter getSpawnerOptionSetter() {
        return spawnerOptionSetter;
    }

    @Override
    public Object getMaxValue() {
        return maxValue;
    }

    @Override
    public MenuItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public SpawnerOptionSetter getOption() {
        return this.spawnerOptionSetter;
    }

    @Override
    public Object getValue() {
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
}
