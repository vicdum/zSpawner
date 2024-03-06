package fr.maxlego08.spawner;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.menu.button.loader.NoneLoader;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerLevel;
import fr.maxlego08.spawner.api.SpawnerType;
import fr.maxlego08.spawner.api.enums.Sort;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import fr.maxlego08.spawner.api.utils.SpawnerResult;
import fr.maxlego08.spawner.buttons.gui.SpawnersButton;
import fr.maxlego08.spawner.zcore.enums.Message;
import fr.maxlego08.spawner.zcore.utils.storage.Persist;
import fr.maxlego08.spawner.zcore.utils.storage.Savable;
import fr.maxlego08.spawner.zcore.utils.yaml.YamlUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SpawnerManager extends YamlUtils implements Savable {

    private final SpawnerPlugin plugin;
    private final NamespacedKey spawnerTypeKey;
    private final NamespacedKey spawnerEntityKey;
    private final Map<SpawnerType, MenuItemStack> spawnerTypeItemStacks = new HashMap<>();
    private final Map<UUID, PlayerSpawner> playerSpawners = new HashMap<>();
    private Map<EntityType, String> entitiesMaterials = new HashMap<>();
    private List<Material> blacklistMaterials = new ArrayList<>();

    public SpawnerManager(SpawnerPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        this.spawnerTypeKey = new NamespacedKey(plugin, "type");
        this.spawnerEntityKey = new NamespacedKey(plugin, "entity");
    }

    public SpawnerLevel getSpawnerLevel(String levelName) {
        return new ZSpawnerLevel("TMP", 1);
    }

    @Override
    public void save(Persist persist) {

    }

    @Override
    public void load(Persist persist) {

        this.spawnerTypeItemStacks.clear();
        File file = new File(this.plugin.getDataFolder(), "config.yml");
        if (!file.exists()) this.plugin.saveDefaultConfig();

        Loader<MenuItemStack> loader = new MenuItemStackLoader(this.plugin.getInventoryManager());
        YamlConfiguration configuration = (YamlConfiguration) this.plugin.getConfig();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("items");
        if (configurationSection != null) {
            configurationSection.getKeys(false).forEach(type -> {
                try {
                    SpawnerType spawnerType = SpawnerType.valueOf(type);
                    MenuItemStack menuItemStack = loader.load(configuration, "items." + type + ".", file);
                    this.spawnerTypeItemStacks.put(spawnerType, menuItemStack);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }

        this.entitiesMaterials = loadEntityMaterials();
        this.blacklistMaterials = loadBlacklist();

        this.loadInventories();
    }

    public void openSpawner(Player player) {
        InventoryManager inventoryManager = this.plugin.getInventoryManager();
        inventoryManager.openInventory(player, this.plugin, "spawners");
    }

    public void loadInventories() {
        InventoryManager inventoryManager = this.plugin.getInventoryManager();
        inventoryManager.deleteInventories(this.plugin);

        try {
            inventoryManager.loadInventoryOrSaveResource(this.plugin, "inventories/gui/spawners.yml");
        } catch (InventoryException exception) {
            exception.printStackTrace();
        }
    }

    public void loadButtons() {

        ButtonManager buttonManager = this.plugin.getButtonManager();
        buttonManager.register(new NoneLoader(this.plugin, SpawnersButton.class, "zspawner_spawners"));

    }

    public ItemStack getSpawnerItemStack(Player player, SpawnerType spawnerType, EntityType entityType) {

        MenuItemStack menuItemStack = this.spawnerTypeItemStacks.get(spawnerType);
        Placeholders placeholders = new Placeholders();
        placeholders.register("type", name(entityType.name()));

        ItemStack itemStack = menuItemStack.build(player, false, placeholders);
        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(this.spawnerTypeKey, PersistentDataType.STRING, spawnerType.name());
        persistentDataContainer.set(this.spawnerEntityKey, PersistentDataType.STRING, entityType.name());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void giveSpawner(CommandSender sender, Player target, SpawnerType spawnerType, EntityType entityType, boolean silent) {

        ItemStack itemStack = getSpawnerItemStack(target, spawnerType, entityType);
        give(target, itemStack);

        message(this.plugin, sender, Message.GIVE_SENDER, "%target%", target.getName(), "%type%", name(spawnerType.name()), "%entity%", name(entityType.name()));
        if (!silent) {
            message(this.plugin, target, Message.GIVE_PLAYER, "%type%", name(spawnerType.name()), "%entity%", name(entityType.name()));
        }
    }

    public Optional<SpawnerResult> getSpawnerResult(ItemStack itemStack) {

        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (persistentDataContainer.has(this.spawnerEntityKey) && persistentDataContainer.has(this.spawnerTypeKey)) {

            SpawnerType spawnerType = SpawnerType.valueOf(persistentDataContainer.get(this.spawnerTypeKey, PersistentDataType.STRING));
            EntityType entityType = EntityType.valueOf(persistentDataContainer.get(this.spawnerEntityKey, PersistentDataType.STRING));
            return Optional.of(new SpawnerResult(spawnerType, entityType));
        }
        return Optional.empty();
    }

    public void addSpawner(CommandSender sender, Player target, EntityType entityType, boolean silent) {

        Spawner spawner = new ZSpawner(this.plugin, target.getUniqueId(), SpawnerType.GUI, entityType);
        this.plugin.getStorage().addSpawner(spawner);

        message(this.plugin, sender, Message.ADD_SENDER, "%target%", target.getName(), "%entity%", name(entityType.name()));
        if (!silent) {
            message(this.plugin, target, Message.ADD_PLAYER, "%entity%", name(entityType.name()));
        }
    }

    public Map<EntityType, String> getEntitiesMaterials() {
        return entitiesMaterials;
    }

    public void breakSpawner(Player player, Spawner spawner) {
        spawner.breakBlock();
        openSpawner(player);
    }

    public void startPlacement(Player player, Spawner spawner) {

        player.closeInventory();
        PlayerSpawner playerSpawner = this.playerSpawners.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerSpawner());
        playerSpawner.setPlacingSpawner(spawner);
        message(this.plugin, player, Message.PLACE_START);
    }

    public Map<UUID, PlayerSpawner> getPlayerSpawners() {
        return playerSpawners;
    }

    public List<Material> getBlacklistMaterials() {
        return blacklistMaterials;
    }

    public Sort getPlayerSort(Player player) {
        PlayerSpawner playerSpawner = this.playerSpawners.get(player.getUniqueId());
        return playerSpawner == null ? Sort.PLACE : playerSpawner.getTypeShort();
    }
}
