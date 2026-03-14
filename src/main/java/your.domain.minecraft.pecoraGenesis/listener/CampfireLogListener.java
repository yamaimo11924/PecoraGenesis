package your.domain.minecraft.pecoraGenesis.listener;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Campfire;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import your.domain.minecraft.pecoraGenesis.PecoraGenesis;
import your.domain.minecraft.pecoraGenesis.animals.LivestockManager;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class CampfireLogListener implements Listener {

    private final NamespacedKey foodKey =
            new NamespacedKey(PecoraGenesis.getInstance(), "food_level");
    private final NamespacedKey statusKey =
            new NamespacedKey(PecoraGenesis.getInstance(), "status_level");
    private final NamespacedKey geneKey =
            new NamespacedKey(PecoraGenesis.getInstance(), "gene_code");

    private boolean hasGene(ItemStack item) {

        if (item == null || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        return pdc.has(geneKey);
    }

    private final LivestockManager livestockManager;

    public CampfireLogListener(LivestockManager livestockManager) {
        this.livestockManager = livestockManager;
    }

    private static class CampfireSlot {

        private final UUID world;
        private final int x;
        private final int y;
        private final int z;
        private final int slot;

        public CampfireSlot(Block block, int slot) {
            this.world = block.getWorld().getUID();
            this.x = block.getX();
            this.y = block.getY();
            this.z = block.getZ();
            this.slot = slot;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CampfireSlot that)) return false;

            return x == that.x &&
                    y == that.y &&
                    z == that.z &&
                    slot == that.slot &&
                    world.equals(that.world);
        }

        @Override
        public int hashCode() {
            return Objects.hash(world, x, y, z, slot);
        }
    }

    // 焚き火で焼けるアイテム
    private static final Set<Material> CAMPFIRE_COOKABLE = Set.of(
            Material.BEEF,
            Material.CHICKEN,
            Material.RABBIT,
            Material.PORKCHOP,
            Material.MUTTON,
            Material.COD,
            Material.SALMON,
            Material.POTATO,
            Material.KELP
    );

    // slot追跡
    private final Map<CampfireSlot, ItemStack> cookingItems = new HashMap<>();

    // ===============================
    // アイテム投入
    // ===============================

    @EventHandler
    public void onCampfireInsert(PlayerInteractEvent event) {

        if (event.getHand() != EquipmentSlot.HAND) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        if (block.getType() != Material.CAMPFIRE) return;

        ItemStack item = event.getItem();
        if (item == null) return;

        if (!CAMPFIRE_COOKABLE.contains(item.getType())) return;

        Campfire campfire = (Campfire) block.getState();

        for (int i = 0; i < 4; i++) {

            ItemStack slot = campfire.getItem(i);

            if (slot == null || slot.getType().isAir()) {

                cookingItems.put(new CampfireSlot(block, i), item.clone());
                return;
            }
        }

        // 満杯なら置かせない
        event.setCancelled(true);
    }

    // ===============================
    // 焼き上がり
    // ===============================

    @EventHandler
    public void onCook(BlockCookEvent event) {

        if (!(event.getBlock().getState() instanceof Campfire)) return;

        Block block = event.getBlock();

        ItemStack result = event.getResult();
        if (result == null) return;

        for (int i = 0; i < 4; i++) {

            CampfireSlot k = new CampfireSlot(block, i);
            ItemStack stored = cookingItems.get(k);

            if (stored == null) continue;

            if (!stored.hasItemMeta()) {
                cookingItems.remove(k);
                continue;
            }

            ItemMeta sourceMeta = stored.getItemMeta();
            ItemMeta resultMeta = result.getItemMeta();

            if (resultMeta == null) return;

            // DisplayName
            if (sourceMeta.hasDisplayName()) {
                resultMeta.displayName(sourceMeta.displayName());
            }

            // Lore
            if (sourceMeta.hasLore()) {
                resultMeta.lore(sourceMeta.lore());
            }

            PersistentDataContainer s = sourceMeta.getPersistentDataContainer();
            PersistentDataContainer r = resultMeta.getPersistentDataContainer();

            if (s.has(foodKey, PersistentDataType.INTEGER)) {
                Integer value = s.get(foodKey, PersistentDataType.INTEGER);
                if (value != null)
                    r.set(foodKey, PersistentDataType.INTEGER, value);
            }

            if (s.has(statusKey, PersistentDataType.INTEGER)) {
                Integer value = s.get(statusKey, PersistentDataType.INTEGER);
                if (value != null)
                    r.set(statusKey, PersistentDataType.INTEGER, value);
            }

            if (s.has(geneKey, PersistentDataType.STRING)) {
                String gene = s.get(geneKey, PersistentDataType.STRING);
                if (gene != null)
                    r.set(geneKey, PersistentDataType.STRING, gene);
            }

            result.setItemMeta(resultMeta);
            event.setResult(result);

            cookingItems.remove(k);
            break;
        }
    }

    // ===============================
    // ドロップ（焚き火破壊）
    // ===============================

    @EventHandler
    public void onDrop(BlockDropItemEvent event) {

        if (event.getBlock().getType() != Material.CAMPFIRE) return;

        Block block = event.getBlock();

        for (int i = 0; i < 4; i++) {
            cookingItems.remove(new CampfireSlot(block, i));
        }
    }
}
