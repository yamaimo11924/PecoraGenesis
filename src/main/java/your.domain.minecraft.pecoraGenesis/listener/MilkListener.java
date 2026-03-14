package your.domain.minecraft.pecoraGenesis.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import your.domain.minecraft.pecoraGenesis.animals.LivestockManager;
import your.domain.minecraft.pecoraGenesis.trait.GeneTrait;
import your.domain.minecraft.pecoraGenesis.trait.Trait;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MilkListener implements Listener {

    private final LivestockManager livestockManager;

    private final Map<UUID, Long> cooldown = new HashMap<>();
    private static final long COOLDOWN = 5 * 60 * 1000; // 5分
    private static final Random random = new Random();

    public MilkListener(LivestockManager livestockManager) {
        this.livestockManager = livestockManager;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onMilk(PlayerInteractEntityEvent event) {

        // -------------------
        // 基本チェック
        // -------------------
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (player.getInventory().getItemInMainHand().getType() != Material.BUCKET) return;
        if (!(entity instanceof Cow || entity instanceof Goat || entity instanceof MushroomCow)) return;
        if (entity instanceof Ageable ageable && !ageable.isAdult()) return;

        UUID id = entity.getUniqueId();
        long now = System.currentTimeMillis();

        // -------------------
        // クールダウン判定
        // -------------------
        if (cooldown.containsKey(id)) {
            long last = cooldown.get(id);

            if (now - last < COOLDOWN) {
                event.setCancelled(true);
                player.sendActionBar(Component.text("§cこの動物はまだ搾れません"));
                return;
            }
        }

        // -------------------
        // 遺伝子取得
        // -------------------
        LivingEntity living = (LivingEntity) entity;
        String gene = String.valueOf(livestockManager.getGene(living));

        Trait trait = GeneTrait.getTrait(gene);
        int drop = trait.getDrop();

        // -------------------
        // クールダウン発生確率
        // -------------------
        double chance = getCooldownChance(drop);

        if (random.nextDouble() <= chance) {
            cooldown.put(id, now);
        }
    }

    /**
     * 遺伝子に応じたクールダウン発生確率
     */
    private double getCooldownChance(int drop) {
        return switch (drop) {
            case 0 -> 1.0;   // 100% クールダウン
            case 1 -> 0.75;  // 75%
            case 2 -> 0.5;   // 50%
            case 3 -> 0.25;  // 25%
            default -> 1.0;
        };
    }
}
