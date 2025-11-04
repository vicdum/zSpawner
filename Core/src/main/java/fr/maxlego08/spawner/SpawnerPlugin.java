package fr.maxlego08.spawner;

import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.utils.MetaUpdater;
import fr.maxlego08.spawner.api.PlayerGive;
import fr.maxlego08.spawner.api.ShopAction;
import fr.maxlego08.spawner.api.item.UpgradeManager;
import fr.maxlego08.spawner.api.storage.IStorage;
import fr.maxlego08.spawner.api.storage.SpawnerStorage;
import fr.maxlego08.spawner.api.team.TeamManager;
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
import fr.maxlego08.spawner.storage.storages.StorageManagerImp;
import fr.maxlego08.spawner.storage.storages.ZServerDataManager;
import fr.maxlego08.spawner.storage.storages.interfaces.ServerDataManager;
import fr.maxlego08.spawner.team.SuperiorTeamManager;
import fr.maxlego08.spawner.zcore.ZPlugin;
import fr.maxlego08.spawner.zcore.utils.compatibility.FoliaCompatibilityManager;
import fr.maxlego08.spawner.zcore.utils.plugins.Metrics;
import fr.maxlego08.spawner.zcore.utils.plugins.Plugins;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class SpawnerPlugin extends ZPlugin {
    private final FoliaCompatibilityManager foliaManager = new FoliaCompatibilityManager(this);

    private final fr.maxlego08.spawner.storage.storages.interfaces.StorageManager storageManager = new StorageManagerImp(this, this.foliaManager);
    private final ServerDataManager serverDataManager = new ZServerDataManager(this);

    private final SpawnerManager manager = new SpawnerManager(this, this.foliaManager);
    private final StackableManager stackableManager = new StackableManager(this);
    private final SpawnerPlaceholders spawnerPlaceholders = new SpawnerPlaceholders(this);
    private final UpgradeManager upgradeManager = new ZUpgradeManager(this);
    private SpawnerStorage spawnerStorage;
    private InventoryManager inventoryManager;
    private ButtonManager buttonManager;
    private ShopAction shopAction;
    private PlayerGive playerGive = new DefaultGive();
    private NamespacedKey spawnerKey;
    private MetaUpdater metaUpdater;
    private final List<TeamManager> teamManagers = new ArrayList<>();

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zspawner");

        this.preEnable();

        this.storageManager.loadDatabase();

        this.spawnerKey = new NamespacedKey(this, "zspawner");

        this.saveDefaultConfig();

        this.registerCommand("zspawner", new CommandSpawner(this), "spawner", "sp", "spawners");

        this.inventoryManager = getProvider(InventoryManager.class);
        this.metaUpdater = this.inventoryManager.getMeta();
        this.buttonManager = getProvider(ButtonManager.class);

        this.addSave(this.manager);
        this.addSave(new MessageLoader(this));
        this.addSave(this.stackableManager);

        this.spawnerStorage = new StorageManager(this, this.foliaManager);
        this.addSave(this.spawnerStorage);

        this.addListener(new SpawnerListener(this, this.foliaManager));
        this.addListener(new SpawnerListenerPaper(this));

        Config.getInstance().load(this);
        this.manager.loadButtons();
        this.upgradeManager.loadItems();
        this.loadFiles();
        this.serverDataManager.loadServerData();

        this.spawnerPlaceholders.register();

        this.foliaManager.runTimer(this.manager, 20, 20);

        new Metrics(this, 5365);

        if (this.isEnable(Plugins.ZSHOP)) {
            getLogger().info("Use zShop");
            this.shopAction = new ZShopAction(this);
        }

        if (this.isEnable(Plugins.ZESSENTIALS)) {
            getLogger().info("Use zEssentials");
            this.playerGive = new ZEssentialsGive();
        }

        if (this.isEnable(Plugins.SUPERIORSKYBLOCK2)) {
            getLogger().info("Use SuperiorSkyBlock2");
            this.registerTeamManager(new SuperiorTeamManager(this));
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

    public NamespacedKey getSpawnerKey() {
        return spawnerKey;
    }

    public fr.maxlego08.spawner.storage.storages.interfaces.StorageManager getStorageManager() {
        return storageManager;
    }

    public ServerDataManager getServerDataManager() {
        return serverDataManager;
    }

    public FoliaCompatibilityManager getFoliaManager() {return this.foliaManager;}

    public MetaUpdater getMetaUpdater() {return this.metaUpdater;}

    public void registerTeamManager(TeamManager teamManager) {
        if (teamManager != null) {
            this.teamManagers.add(teamManager);
        }
    }

    public List<TeamManager> getTeamManagers() {
        return Collections.unmodifiableList(this.teamManagers);
    }

    public boolean hasTeamAccess(UUID ownerId, UUID playerId) {
        if (ownerId == null || playerId == null) {
            return false;
        }
        return this.teamManagers.stream().anyMatch(teamManager -> teamManager.canAccess(ownerId, playerId));
    }
}
