package your.domain.minecraft.pecoraGenesis.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Ageable;
import org.bukkit.event.Listener;
import your.domain.minecraft.pecoraGenesis.animals.LivestockManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;
import your.domain.minecraft.pecoraGenesis.gene.Gene;
import your.domain.minecraft.pecoraGenesis.gene.GeneMutation;
import your.domain.minecraft.pecoraGenesis.trait.GeneTrait;
import your.domain.minecraft.pecoraGenesis.trait.Trait;

public class BreedListener implements Listener {

    private final LivestockManager livestockManager;
    public BreedListener(LivestockManager livestockManager) {
        this.livestockManager = livestockManager;
    }

    @EventHandler
    public void onBreed(EntityBreedEvent event) {

        LivingEntity child = event.getEntity();
        LivingEntity mother = event.getMother();
        LivingEntity father = event.getFather();

        if (!livestockManager.isLivestock(child)) return;

        Gene motherGene = livestockManager.getGene(mother);
        Gene fatherGene = livestockManager.getGene(father);

        Trait motherTrait = GeneTrait.getTrait(motherGene.toString());
        Trait fatherTrait = GeneTrait.getTrait(fatherGene.toString());

        Gene childGene = GeneMutation.createChildGene(motherGene, fatherGene);

        // コンソールにログ出力（JavaPlugin を持たなくてもOK）
        Bukkit.getServer().getConsoleSender().sendMessage(
                "§a[PecoraGenesis] Breed: Type=" + child.getType()
                        + ", M1=" + mother.getUniqueId()
                        + ", F2=" + father.getUniqueId()
                        + ", Gene=" + childGene
        );

        int breedLevel = Math.max(motherTrait.getBreed(), fatherTrait.getBreed());
        double twinChance = switch (breedLevel) {
            case 1 -> 0.25;
            case 2 -> 0.5;
            default -> 0.0;
        };


        if (Math.random() < twinChance) {
            // 双子の生成(一匹追加)
            if (event.getEntityType().getEntityClass() != null) {
                LivingEntity twin = (LivingEntity) child.getWorld()
                        .spawn(child.getLocation(), event.getEntityType().getEntityClass());

                // 子供状態にする
                if (twin instanceof Ageable ageable) {
                    ageable.setBaby();
                }

                livestockManager.setGene(twin, childGene); // 双子に遺伝子コピー

                // 双子生成ログ
                Bukkit.getServer().getConsoleSender().sendMessage(
                        "§b[PecoraGenesis] Twin: Type=" + twin.getType()
                                + ", M1=" + mother.getUniqueId()
                                + ", F2=" + father.getUniqueId()
                                + ", Gene=" + childGene
                );
            }
        }

        livestockManager.setGene(child, childGene);

    }
}
