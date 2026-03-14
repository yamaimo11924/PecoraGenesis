package your.domain.minecraft.pecoraGenesis.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import your.domain.minecraft.pecoraGenesis.PecoraGenesis;
import your.domain.minecraft.pecoraGenesis.animals.LivestockManager;
import your.domain.minecraft.pecoraGenesis.gene.Gene;
import your.domain.minecraft.pecoraGenesis.gene.GeneSerializer;
import your.domain.minecraft.pecoraGenesis.language.LanguageManager;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public class GeneInspectListener implements Listener {

    private final LivestockManager livestockManager;

    public GeneInspectListener(LivestockManager livestockManager) {
        this.livestockManager = livestockManager;
    }

    @EventHandler
    public void onInspect(PlayerInteractEntityEvent event) {

        // オフハンドイベントを無視
        if (event.getHand() != EquipmentSlot.HAND) return;

        LanguageManager lang = PecoraGenesis.getInstance().getLanguage();

        if (!(event.getRightClicked() instanceof LivingEntity entity)) return;
        if (!livestockManager.isLivestock(entity)) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // feed 判定
        List<Material> validFeedItems = PecoraGenesis.getInstance()
                .getConfigManager()
                .getFeedItems(entity.getType());

        boolean isFeed = !GeneSerializer.hasGene(entity) &&
                validFeedItems.contains(item.getType());

        if (isFeed) {
            GeneSerializer.saveGene(entity, Gene.defaultGene());
            player.sendActionBar(
                    Component.text(lang.get("gene-init"), NamedTextColor.GREEN)
            );
        }

        Material inspectItem = PecoraGenesis.getInstance()
                .getConfigManager()
                .getInspectItem();

        // ===== 石のクワで右クリックした場合は遺伝子表示 =====
        if (item.getType() == inspectItem) {

            Gene gene = livestockManager.getGene(entity);

            player.sendActionBar(
                    Component.text(lang.get("gene-label"), NamedTextColor.GREEN)
                            .append(Component.text(gene.toString(), NamedTextColor.AQUA))
            );
        }
    }
}
