package your.domain.minecraft.pecoraGenesis.gene;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import your.domain.minecraft.pecoraGenesis.PecoraGenesis;

public class GeneSerializer {

    private static final NamespacedKey GENE_KEY =
            new NamespacedKey(PecoraGenesis.getInstance(), "gene");
    /*
     遺伝子保存
     */
    public static void saveGene(LivingEntity entity, Gene gene) {

        if (entity == null || gene == null) return;

        PersistentDataContainer pdc = entity.getPersistentDataContainer();

        pdc.set(GENE_KEY, PersistentDataType.STRING, gene.toString());
    }
    /*
     遺伝子取得
     */
    public static Gene loadGene(LivingEntity entity) {

        if (entity == null) return Gene.defaultGene();

        PersistentDataContainer pdc = entity.getPersistentDataContainer();

        String geneStr = pdc.get(GENE_KEY, PersistentDataType.STRING);

        if (geneStr == null) {
            return Gene.defaultGene();
        }

        return Gene.fromString(geneStr);
    }
    /*
     遺伝子が存在するか
     */
    public static boolean hasGene(LivingEntity entity) {

        PersistentDataContainer pdc = entity.getPersistentDataContainer();

        return pdc.has(GENE_KEY, PersistentDataType.STRING);
    }
    /*
     遺伝子削除
     */
    public static void removeGene(LivingEntity entity) {

        PersistentDataContainer pdc = entity.getPersistentDataContainer();

        pdc.remove(GENE_KEY);
    }
}
