package fr.maxlego08.spawner.item;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.enums.SpawnerOptionSetter;
import fr.maxlego08.spawner.api.item.UpgradeItem;
import fr.maxlego08.spawner.api.item.UpgradeManager;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ZUpgradeManager extends ZUtils implements UpgradeManager {

    private final SpawnerPlugin plugin;
    private final Map<String, UpgradeItem> upgrades = new HashMap<>();
    private final NamespacedKey namespacedKey;

    public ZUpgradeManager(SpawnerPlugin plugin) {
        this.plugin = plugin;
        this.namespacedKey = new NamespacedKey(plugin, "upgrade");
    }

    @Override
    public void loadItems() {
        InventoryManager inventoryManager = this.plugin.getInventoryManager();
        Loader<MenuItemStack> loader = new MenuItemStackLoader(inventoryManager);
        File file = new File(this.plugin.getDataFolder(), "option-items.yml");
        if (!file.exists()) {
            plugin.saveResource("option-items.yml", false);
        }

        this.upgrades.clear();

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection configurationSection = configuration.getConfigurationSection("items");
        if (configurationSection == null) return;

        for (String upgradeName : configurationSection.getKeys(false)) {

            String path = "items." + upgradeName + ".";
            MenuItemStack itemStack = null;
            try {
                itemStack = loader.load(configuration, path + "item.", file);
            } catch (InventoryException exception) {
                exception.printStackTrace();
            }

            SpawnerOptionSetter spawnerOptionSetter = SpawnerOptionSetter.valueOf(configuration.getString(path + "type").toUpperCase());
            Object value = configuration.get(path + "value");
            Object maxValue = configuration.get(path + "max-value");
            String displayName = configuration.getString(path + "display-name", upgradeName);

            this.upgrades.put(upgradeName, new ZUpgradeItem(displayName, spawnerOptionSetter, value, maxValue, itemStack));
        }
    }

    @Override
    public Map<String, UpgradeItem> getUpgradeItems() {
        return this.upgrades;
    }

    @Override
    public Optional<UpgradeItem> getUpgrade(String name) {
        return Optional.of(this.upgrades.get(name));
    }

    @Override
    public void give(CommandSender sender, Player player, String upgradeName) {

        Optional<UpgradeItem> optional = getUpgrade(upgradeName);
        if (optional.isEmpty()) {
            message(plugin, sender, Message.UPGRADE_NOT_FOUND, "%name%", upgradeName);
            return;
        }

        UpgradeItem upgradeItem = optional.get();
        ItemStack itemStack = upgradeItem.getItemStack().build(player, false);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(this.namespacedKey, PersistentDataType.STRING, upgradeName);
        itemStack.setItemMeta(itemMeta);
        this.plugin.getPlayerGive().give(player, itemStack);

        message(plugin, sender, Message.UPGRADE_GIVE, "%name%", upgradeItem.getDisplayName(), "%player%", player.getName());
    }

    @Override
    public boolean applyUpgradeItem(Spawner spawner, Player player, ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return false;

        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        if (!persistentDataContainer.has(this.namespacedKey, PersistentDataType.STRING)) return false;

        String upgradeName = persistentDataContainer.get(this.namespacedKey, PersistentDataType.STRING);
        Optional<UpgradeItem> optional = getUpgrade(upgradeName);
        if (optional.isEmpty()) return false;

        UpgradeItem upgradeItem = optional.get();

        if (!upgradeItem.canApply(spawner)) {
            message(plugin, player, Message.UPGRADE_NOT_APPLICABLE, "%name%", upgradeItem.getDisplayName());
            return false;
        }

        if (!upgradeItem.apply(spawner)) return false;

        removeItemInHand(player, 1);
        Location location = spawner.getLocation().getBlock().getLocation();
        var particleLocation = new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY() + 1, location.getBlockZ() + 0.5);
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, particleLocation, 25, 0.55, 0.55, 0.55);
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1f, 1f);

        message(plugin, player, Message.UPGRADE_APPLY, "%name%", upgradeItem.getDisplayName());

        return true;
    }
}
