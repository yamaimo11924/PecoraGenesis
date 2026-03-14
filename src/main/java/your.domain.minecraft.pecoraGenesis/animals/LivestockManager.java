package your.domain.minecraft.pecoraGenesis.animals;

import your.domain.minecraft.pecoraGenesis.PecoraGenesis;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import your.domain.minecraft.pecoraGenesis.gene.Gene;
import your.domain.minecraft.pecoraGenesis.gene.GeneSerializer;

public class LivestockManager {

    private final JavaPlugin plugin;

    public LivestockManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /* 対象家畜か判定 */
    public boolean isLivestock(LivingEntity entity) {
        EntityType type = entity.getType();
        return type == EntityType.COW
                || type == EntityType.PIG
                || type == EntityType.SHEEP
                || type == EntityType.RABBIT;
    }

    /* 遺伝子取得 */
    public Gene getGene(LivingEntity entity) {
        if (!GeneSerializer.hasGene(entity)) {
            Gene defaultGene = Gene.defaultGene();
            GeneSerializer.saveGene(entity, defaultGene); // 初期化は AA
            return defaultGene;
        }
        return GeneSerializer.loadGene(entity);
    }

    /* 遺伝子設定 */
    public void setGene(LivingEntity entity, Gene gene) {
        GeneSerializer.saveGene(entity, gene);

        // database.yml にも保存
        PecoraGenesis.getInstance().getGeneDatabase().save(entity, gene);
    }

    /* initializeGene は必要な場合だけ使用 */
    public void initializeGene(LivingEntity entity) {
        if (!isLivestock(entity) && !GeneSerializer.hasGene(entity)) {
            GeneSerializer.saveGene(entity, Gene.defaultGene());
            plugin.getLogger().info(
                    "Initialized gene AA for " +
                            entity.getType() +
                            " UUID=" + entity.getUniqueId()
            );
        }
    }
}
