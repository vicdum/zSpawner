package fr.maxlego08.spawner.shop;

import fr.maxlego08.spawner.api.ShopAction;
import fr.maxlego08.spawner.zcore.utils.ZUtils;
import fr.maxlego08.zshop.api.ShopManager;
import fr.maxlego08.zshop.api.buttons.ItemButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class ZShopAction extends ZUtils implements ShopAction {

    private final Plugin plugin;
    private ShopManager shopManager;

    public ZShopAction(Plugin plugin) {
        this.plugin = plugin;
    }

    private Optional<ItemButton> getItemButton(Player player, ItemStack itemStack, long amount) {
        try {

            ShopManager shopManager = this.getShopManager();
            return shopManager.getItemButton(player, itemStack);

        } catch (Exception exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public double getSellPrice(Player player, ItemStack itemStack, long amount) {
        var optional = getItemButton(player, itemStack, amount);
        if (optional.isEmpty()) return 0.0;

        var itemButton = optional.get();
        return itemButton.getSellPrice(player, (int) amount);
    }

    @Override
    public boolean deposit(Player player, ItemStack itemStack, long amount) {
        var optional = getItemButton(player, itemStack, amount);
        if (optional.isEmpty()) return false;

        var itemButton = optional.get();
        var sellPrice = itemButton.getSellPrice(player, (int) amount);

        String message = this.plugin.getConfig().getString("deposit-reason", "Sale of x%amount% %item% for %price% (Spawner)");
        itemButton.getEconomy().depositMoney(player, sellPrice, getMessage(message, "%amount%", format(amount), "%item%", getItemName(itemStack), "%price%", itemButton.getSellPriceFormat(player, (int) amount)));

        return true;
    }

    public ShopManager getShopManager() {
        if (this.shopManager != null) return this.shopManager;
        return this.shopManager = this.plugin.getServer().getServicesManager().getRegistration(ShopManager.class).getProvider();
    }

}
