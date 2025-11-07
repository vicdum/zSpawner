package fr.maxlego08.spawner.buttons.virtual;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.button.PaginateButton;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.spawner.SpawnerPlugin;
import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.api.SpawnerLocationHistory;
import fr.maxlego08.spawner.api.utils.PlayerSpawner;
import fr.maxlego08.spawner.zcore.enums.Message;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocationHistoryButton extends PaginateButton {
    private final SpawnerPlugin plugin;

    public LocationHistoryButton(Plugin plugin) {
        this.plugin = (SpawnerPlugin) plugin;


    }

    @Override
    public void onRender(Player player, InventoryEngine inventory) {
        Spawner virtualSpawner = this.plugin.getManager().getPlayerSpawners().computeIfAbsent(player.getUniqueId(), uuid -> new PlayerSpawner()).getVirtualSpawner();

        if (virtualSpawner == null) {
            return;
        }
        List<SpawnerLocationHistory> locationHistories = virtualSpawner.getLocationHistory();

        if (locationHistories.isEmpty()) {
            Button elseButton = this.getElseButton();
            if (elseButton != null) {
                ItemStack customItemStack = elseButton.getCustomItemStack(player);
                inventory.addItem(this.getSlot(), customItemStack);
            }
            return;
        }

        this.paginate(locationHistories, inventory, (slot, locationHistorie)->{
            Placeholders placeholders = new Placeholders();

            placeholders.register("location_user_uuid", String.valueOf(locationHistorie.getRentalPlayer()));
            OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(locationHistorie.getRentalPlayer());
            String playerName = offlinePlayer.getName() != null ? offlinePlayer.getName() : "Unknown";
            placeholders.register("location_user", playerName);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String formattedDate = dateFormat.format(new Date(locationHistorie.getStartTime()));
            placeholders.register("location_date", formattedDate);

            long durationMs = locationHistorie.getDuration();
            long durationMinutes = TimeUnit.MILLISECONDS.toMinutes(durationMs);
            long durationHours = TimeUnit.MILLISECONDS.toHours(durationMs);
            long durationDays = TimeUnit.MILLISECONDS.toDays(durationMs);

            String formattedDuration;
            if (durationDays > 0) {
                formattedDuration = String.format("%dj %dh %dmin",
                    durationDays,
                    durationHours % 24,
                    durationMinutes % 60);
            } else if (durationHours > 0) {
                formattedDuration = String.format("%dh %dmin",
                    durationHours,
                    durationMinutes % 60);
            } else {
                formattedDuration = String.format("%d minutes", durationMinutes);
            }

            placeholders.register("location_duration", formattedDuration);
            placeholders.register("location_duration_minutes", String.valueOf(durationMinutes));

            String formattedEndTime = dateFormat.format(new Date(locationHistorie.getEndTime()));
            placeholders.register("location_end_date", formattedEndTime);

            long currentTime = System.currentTimeMillis();
            boolean isActive = locationHistorie.getEndTime() > currentTime;
            placeholders.register("location_status", isActive ? Message.SPAWNER_LOCATION_STATUS_ACTIVE.getMessage() : Message.SPAWNER_LOCATION_STATUS_EXPIRED.getMessage());
            placeholders.register("location_status_raw", isActive ? Message.SPAWNER_LOCATION_STATUS_RAW_ACTIVE.getMessage() :  Message.SPAWNER_LOCATION_STATUS_RAW_EXPIRED.getMessage());

            // Price
            placeholders.register("location_price", String.format("%.2f", locationHistorie.getPrice()));

            ItemStack itemStack = this.getItemStack().build(player, false, placeholders);
            if (itemStack.getType() == Material.PLAYER_HEAD){
                SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                skullMeta.setPlayerProfile(offlinePlayer.getPlayerProfile().getTextures().isEmpty() ? offlinePlayer.getPlayerProfile().update().join() : offlinePlayer.getPlayerProfile());
                itemStack.setItemMeta(skullMeta);
            }
            inventory.addItem(slot, itemStack);
        });
    }


    @Override
    public int getPaginationSize(Player player) {
        Spawner virtualSpawner = this.plugin.getManager().getPlayerSpawners().computeIfAbsent(player.getUniqueId(), uuid -> new PlayerSpawner()).getVirtualSpawner();
        return virtualSpawner != null ? virtualSpawner.getLocationHistory().size() : 0;
    }
}
