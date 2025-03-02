package fr.maxlego08.spawner;

import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.spawner.api.PlayerGive;
import fr.maxlego08.spawner.api.ShopAction;
import fr.maxlego08.spawner.api.item.UpgradeManager;
import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.api.storage.SpawnerStorage;
import fr.maxlego08.spawner.command.commands.CommandSpawner;
import fr.maxlego08.spawner.give.DefaultGive;
import fr.maxlego08.spawner.give.ZEssentialsGive;
import fr.maxlego08.spawner.item.ZUpgradeManager;
import fr.maxlego08.spawner.placeholder.LocalPlaceholder;
import fr.maxlego08.spawner.save.Config;
import fr.maxlego08.spawner.save.MessageLoader;
import fr.maxlego08.spawner.shop.ZShopAction;
import fr.maxlego08.spawner.stackable.StackableManager;
import fr.maxlego08.spawner.storage.StorageManager;
import fr.maxlego08.spawner.zcore.ZPlugin;
import fr.maxlego08.spawner.zcore.utils.plugins.Plugins;
import org.bukkit.Bukkit;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class SpawnerPlugin extends ZPlugin {

    private final SpawnerManager manager = new SpawnerManager(this);
    private final StackableManager stackableManager = new StackableManager(this);
    private final SpawnerPlaceholders spawnerPlaceholders = new SpawnerPlaceholders(this);
    private final UpgradeManager upgradeManager = new ZUpgradeManager(this);
    private SpawnerStorage spawnerStorage;
    private InventoryManager inventoryManager;
    private ButtonManager buttonManager;
    private ShopAction shopAction;
    private PlayerGive playerGive = new DefaultGive();

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zspawner");

        this.preEnable();

        this.saveDefaultConfig();

        this.registerCommand("zspawner", new CommandSpawner(this), "spawner", "sp", "spawners");

        this.inventoryManager = getProvider(InventoryManager.class);
        this.buttonManager = getProvider(ButtonManager.class);

        this.addSave(this.manager);
        this.addSave(new MessageLoader(this));
        this.addSave(this.stackableManager);

        this.spawnerStorage = new StorageManager(this);
        this.addSave(this.spawnerStorage);

        this.addListener(new SpawnerListener(this));
        this.addListener(new SpawnerListenerPaper());

        Config.getInstance().load(this);
        this.manager.loadButtons();
        this.upgradeManager.loadItems();
        this.loadFiles();

        this.spawnerPlaceholders.register();

        Bukkit.getScheduler().runTaskTimer(this, this.manager, 20, 20);

        if (this.isEnable(Plugins.ZSHOP)) {
            getLogger().info("Use zShop");
            this.shopAction = new ZShopAction(this);
        }

        if (this.isEnable(Plugins.ZESSENTIALS)) {
            getLogger().info("Use zEssentials");
            this.playerGive = new ZEssentialsGive();
        }

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

    @Override
    public void reloadFiles() {
        super.reloadFiles();
        this.upgradeManager.loadItems();
    }

    public SpawnerManager getManager() {
        return manager;
    }

    public SpawnerStorage getSpawnerStorage() {
        return this.spawnerStorage;
    }

    public IStorage getStorage() {
        return this.spawnerStorage.getStorage();
    }

    public StackableManager getStackableManager() {
        return stackableManager;
    }

    public ButtonManager getButtonManager() {
        return buttonManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public UpgradeManager getUpgradeManager() {
        return upgradeManager;
    }

    public ShopAction getShopAction() {
        return shopAction;
    }

    public PlayerGive getPlayerGive() {
        return playerGive;
    }
}
