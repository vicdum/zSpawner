package fr.maxlego08.spawner.api.item;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.enums.SpawnerOptionSetter;

public interface UpgradeItem {

    SpawnerOptionSetter getOption();

    MenuItemStack getItemStack();

    Object getValue();

    Object getMaxValue();

    boolean canApply(Spawner spawner);

    boolean apply(Spawner spawner);
}
