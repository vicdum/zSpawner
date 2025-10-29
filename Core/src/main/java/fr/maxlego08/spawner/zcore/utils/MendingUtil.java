package fr.maxlego08.spawner.zcore.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MendingUtil {

    private MendingUtil() {
    }

    /**
     * ----------- Utilitaires communs -----------
     */

    private static boolean isEligibleMending(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable dmg)) return false;
        if (!meta.hasEnchant(Enchantment.MENDING)) return false;
        if (meta.isUnbreakable()) return false;
        return dmg.getDamage() > 0;
    }

    /**
     * Répare un item avec la logique Mending et retourne l'XP restante.
     * Règle vanilla : 1 point d'XP -> 2 points de durabilité.
     */
    public static int repairItemWithMending(ItemStack item, int xpPoints) {
        if (item == null || item.getType().isAir() || xpPoints <= 0) return xpPoints;

        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable dmg)) return xpPoints;
        if (!meta.hasEnchant(Enchantment.MENDING)) return xpPoints;
        if (meta.isUnbreakable()) return xpPoints;

        int damage = dmg.getDamage();
        if (damage <= 0) return xpPoints;

        int maxRepairByXp = xpPoints * 2;         // 1 XP = 2 durabilité
        int actualRepair = Math.min(damage, maxRepairByXp);
        int xpUsed = (actualRepair + 1) / 2;      // arrondi supérieur si impair

        dmg.setDamage(damage - actualRepair);
        item.setItemMeta(dmg);

        return xpPoints - xpUsed;
    }

    private static List<Slot> mainHandAndArmor(PlayerInventory inv) {
        return List.of(
                new Slot(inv::getItemInOffHand, inv::setItemInOffHand),
                new Slot(inv::getItemInMainHand, inv::setItemInMainHand),
                new Slot(inv::getHelmet, inv::setHelmet),
                new Slot(inv::getChestplate, inv::setChestplate),
                new Slot(inv::getLeggings, inv::setLeggings),
                new Slot(inv::getBoots, inv::setBoots)
        );
    }

    /**
     * Consomme tout le budget d'XP pour réparer la main principale ET/OU l’armure.
     * À chaque "tick" de consommation, choisit aléatoirement un item éligible (comme vanilla).
     *
     * @param player   Joueur concerné
     * @param xpPoints Budget total d'XP (points, pas niveaux)
     * @return XP restante si plus rien n’est réparable (sinon 0)
     */
    public static int repairAllMainHandAndArmor(Player player, int xpPoints) {
        if (player == null || xpPoints <= 0) return xpPoints;

        PlayerInventory inv = player.getInventory();
        List<Slot> addressable = mainHandAndArmor(inv);

        // Garde-fou anti-boucle (au pire, on ne devrait jamais l’atteindre)
        int safety = 10000;

        while (xpPoints > 0 && safety-- > 0) {
            // Scanner les items éligibles à ce tour
            List<Candidate> candidates = new ArrayList<>();
            for (Slot s : addressable) {
                ItemStack it = s.getter().get();
                if (isEligibleMending(it)) {
                    candidates.add(new Candidate(s, it));
                }
            }

            if (candidates.isEmpty()) {
                // Plus rien à réparer
                break;
            }

            // Choisir un candidat au hasard (comportement vanilla)
            Candidate chosen = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));

            int before = xpPoints;
            xpPoints = repairItemWithMending(chosen.item, xpPoints);

            // Réécrire l’item dans son slot (sécuritaire)
            chosen.slot.setter().accept(chosen.item);

            // Si aucune XP n’a été consommée, on évite la boucle infinie
            if (xpPoints == before) break;
        }

        return xpPoints; // 0 si tout consommé, >0 si plus rien n'est réparable
    }

    /**
     * Représentation d’un slot adressable (get/set)
     */
    private record Slot(Supplier<ItemStack> getter, Consumer<ItemStack> setter) {
    }

    /** ----------- Méthode demandée : consommer tout l’XP pour réparer ----------- */

    /**
     * Un candidat éligible + l’item lu au moment T (pour éviter des get() multiples)
     */
    private record Candidate(Slot slot, ItemStack item) {
    }
}
