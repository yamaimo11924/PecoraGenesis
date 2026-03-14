package your.domain.minecraft.pecoraGenesis.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import your.domain.minecraft.pecoraGenesis.animals.*;
import org.bukkit.NamespacedKey;
import your.domain.minecraft.pecoraGenesis.PecoraGenesis;

import java.util.Set;

public class PlayerEatListener implements Listener {

    private final NamespacedKey foodKey =
            new NamespacedKey(PecoraGenesis.getInstance(), "food_level");

    private final NamespacedKey statusKey =
            new NamespacedKey(PecoraGenesis.getInstance(), "status_level");
    private final NamespacedKey geneKey =
            new NamespacedKey(PecoraGenesis.getInstance(), "gene_code");

    private final Set<Material> cookedMeats = Set.of(
            Material.COOKED_PORKCHOP, Material.COOKED_BEEF, Material.COOKED_MUTTON, Material.COOKED_RABBIT
    );

    private final Set<Material> rawMeats = Set.of(
            Material.PORKCHOP, Material.BEEF, Material.MUTTON, Material.RABBIT
    );

    private final LivestockManager livestockManager;

    public PlayerEatListener(LivestockManager livestockManager) {
        this.livestockManager = livestockManager;
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return;

        if (!meta.getPersistentDataContainer().has(foodKey, PersistentDataType.INTEGER)
                && !meta.getPersistentDataContainer().has(statusKey, PersistentDataType.INTEGER))
            return;

        int foodLevel = meta.getPersistentDataContainer().getOrDefault(
                foodKey,
                PersistentDataType.INTEGER,
                0
        );

        int statusLevel = meta.getPersistentDataContainer().getOrDefault(
                statusKey,
                PersistentDataType.INTEGER,
                0
        );

        // 食糧バフ
        switch (item.getType()) {
            case COOKED_RABBIT, COOKED_MUTTON, COOKED_PORKCHOP, COOKED_BEEF -> {
                switch (foodLevel) {
                    case 1 -> {
                        player.setFoodLevel(Math.min(player.getFoodLevel() + 1, 20));
                        if (Math.random() < 0.4) player.setSaturation(Math.min(player.getSaturation() + 1.0f, 20.0f));
                    }
                    case 2 -> {
                        player.setFoodLevel(Math.min(player.getFoodLevel() + 2, 20));
                        if (Math.random() < 0.3) player.setSaturation(Math.min(player.getSaturation() + 2.0f, 20.0f));
                    }
                }
            }
        }

        // ステータスバフ
        if (statusLevel > 0) {
            switch (item.getType()) {
                case COOKED_RABBIT -> Rabbit.apply(player, statusLevel);
                case COOKED_MUTTON -> Sheep.apply(player, statusLevel);
                case COOKED_PORKCHOP -> Pig.apply(player, statusLevel);
                case COOKED_BEEF -> Cow.apply(player, statusLevel);
            }
        }
    }
}
