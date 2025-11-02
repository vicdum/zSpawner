package fr.maxlego08.spawner.loader;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.DefaultButtonValue;
import fr.maxlego08.menu.api.loader.ButtonLoader;
import fr.maxlego08.menu.hooks.currencies.Currencies;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.buttons.virtual.PlayerPurchaseSpawnerLocationButton;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerPurchaseLocationButtonLoader extends ButtonLoader {
    private final SpawnerPlugin plugin;

    public PlayerPurchaseLocationButtonLoader(SpawnerPlugin plugin) {
        super(plugin, "zspawner_player_purchase_location");
        this.plugin = plugin;
    }

    @Override
    public Button load(YamlConfiguration configuration, String path, DefaultButtonValue defaultButtonValue) {
        Currencies currencies = Currencies.valueOf(configuration.getString("currencies", Currencies.VAULT.name()).toUpperCase());
        String economyName = configuration.getString("economy",null);
        return new PlayerPurchaseSpawnerLocationButton(this.plugin,currencies,economyName);
    }
}
