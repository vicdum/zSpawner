package fr.maxlego08.spawner.zcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class OfflinePlayerCache {
    private static final Map<UUID, OfflinePlayer> cache = new HashMap<>();

    public static OfflinePlayer getOfflinePlayer(UUID uuid) {
        return cache.computeIfAbsent(uuid, Bukkit::getOfflinePlayer);
    }

    public static void clearCache() {
        cache.clear();
    }
}
