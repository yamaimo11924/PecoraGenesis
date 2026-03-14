package your.domain.minecraft.pecoraGenesis.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import your.domain.minecraft.pecoraGenesis.PecoraGenesis;
import your.domain.minecraft.pecoraGenesis.animals.LivestockManager;
import your.domain.minecraft.pecoraGenesis.language.LanguageManager;
import your.domain.minecraft.pecoraGenesis.trait.GeneTrait;
import your.domain.minecraft.pecoraGenesis.trait.Trait;
import your.domain.minecraft.pecoraGenesis.gene.Gene;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DropListener implements Listener {

    private final LivestockManager livestockManager;

    private final NamespacedKey foodKey =
            new NamespacedKey(PecoraGenesis.getInstance(), "food_level");
    private final NamespacedKey statusKey =
            new NamespacedKey(PecoraGenesis.getInstance(), "status_level");
    private final NamespacedKey geneKey =
            new NamespacedKey(PecoraGenesis.getInstance(), "gene_code");

    LanguageManager lang = PecoraGenesis.getInstance().getLanguage();

    public DropListener(LivestockManager livestockManager) {
        this.livestockManager = livestockManager;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (!livestockManager.isLivestock(entity)) return; // 家畜以外は処理しない

        UUID uuid = entity.getUniqueId();

        // 死亡を記録
        PecoraGenesis.getInstance().getGeneDatabase().markDead(uuid);

        // 家畜死亡ログ
        PecoraGenesis.getInstance().getLogger().info(
                "[PecoraGenesis] UUID=" + uuid + ", type=" + entity.getType()
        );

        // Gene をロード
        Gene geneObj = livestockManager.getGene(entity);

        String geneCode = geneObj.toString();
        Trait trait = GeneTrait.getTrait(geneCode);


        double dropMultiplier = switch (trait.getDrop()) {
            case 1 -> 1.2;
            case 2 -> 1.5;
            case 3 -> 2.0;
            default -> 1.0;
        };

        for (int i = 0; i < event.getDrops().size(); i++) {
            ItemStack item = event.getDrops().get(i);
            Material type = item.getType();

           // 対象の肉だけ
            if (type != Material.BEEF && type != Material.PORKCHOP &&
                    type != Material.MUTTON && type != Material.RABBIT &&
                    type != Material.COOKED_BEEF && type != Material.COOKED_PORKCHOP &&
                    type != Material.COOKED_MUTTON && type != Material.COOKED_RABBIT) continue;

            int newAmount = (int) Math.ceil(item.getAmount() * dropMultiplier);
            item.setAmount(newAmount);

            // 既存アイテムに Lore を付与
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {

                List<Component> lore = new ArrayList<>();

                lore.add(Component.text("Gene: " + geneCode).color(NamedTextColor.GOLD));

                if (trait.getFood() > 0)
                    lore.add(Component.text("[FOOD]" + trait.getFood()).color(NamedTextColor.GRAY));

                if (trait.getStatus() > 0)
                    lore.add(Component.text("[STATUS]" + trait.getStatus()).color(NamedTextColor.GRAY));

                meta.lore(lore);

                // ★ PDC追加
                meta.getPersistentDataContainer().set(
                        foodKey,
                        PersistentDataType.INTEGER,
                        trait.getFood()
                );

                meta.getPersistentDataContainer().set(
                        statusKey,
                        PersistentDataType.INTEGER,
                        trait.getStatus()
                );

                meta.getPersistentDataContainer().set(
                        geneKey,
                        PersistentDataType.STRING,
                        geneCode
                );

                item.setItemMeta(meta);
                PecoraGenesis.getInstance().getLogger().info(
                        lang.get("drop-lore-set")
                                .replace("{lore}", lore.toString())
                );
            }


            PecoraGenesis.getInstance().getLogger().info(
                    lang.get("drop-item-update")
                            .replace("{type}", type.toString())
                            .replace("{amount}", String.valueOf(newAmount))
            );
        }
    }
}
