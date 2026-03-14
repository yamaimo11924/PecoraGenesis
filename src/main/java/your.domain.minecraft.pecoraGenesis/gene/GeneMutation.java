package your.domain.minecraft.pecoraGenesis.gene;

import your.domain.minecraft.pecoraGenesis.PecoraGenesis;
import your.domain.minecraft.pecoraGenesis.config.ConfigManager;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GeneMutation {

    /**
     * 子の遺伝子生成
     */
    public static Gene createChildGene(Gene mother, Gene father) {

        Gene baseGene = ThreadLocalRandom.current().nextBoolean() ? mother : father;
        int roll = ThreadLocalRandom.current().nextInt(100) + 1;

        ConfigManager cfg = PecoraGenesis.getInstance().getConfigManager();

        // 通常継承
        if (roll <= cfg.getInheritChance()) {
            return new Gene(baseGene.getLeft(), baseGene.getRight());
        }

        // 遺伝変異
        if (roll <= cfg.getInheritChance() + cfg.getDominanceChance()) {
            return dominanceMutation(baseGene);
        }

        // 順序変異
        return sequenceMutation(baseGene);
    }

    /**
     * 遺伝変異（Dominance）
     */
    private static Gene dominanceMutation(Gene gene) {

        GeneType left = gene.getLeft();
        GeneType right = gene.getRight();

        boolean fixLeft = ThreadLocalRandom.current().nextBoolean();

        Set<GeneType> bitSources = new HashSet<>();
        bitSources.add(left);
        bitSources.add(right);

        List<Gene> candidates = new ArrayList<>();

        if (fixLeft) {
            for (GeneType candidate : bitSources) {
                candidates.add(new Gene(left, candidate));
            }
        } else {
            for (GeneType candidate : bitSources) {
                candidates.add(new Gene(candidate, right));
            }
        }
        /*
        // ===== CT特別ルール追加 =====
        // 親が CG または CC の場合、CTを候補に追加
        if (isCG(gene) || isCC(gene)) {
            candidates.add(new Gene(GeneType.C, GeneType.T));
        }
        */

        return candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
    }

    /**
     * 順序変異（Sequence）
     */
    private static Gene sequenceMutation(Gene gene) {

        GeneType leftNext = nextGene(gene.getLeft());
        GeneType rightNext = nextGene(gene.getRight());

        Gene option1 = new Gene(leftNext, gene.getRight());
        Gene option2 = new Gene(gene.getLeft(), rightNext);

        return ThreadLocalRandom.current().nextBoolean() ? option1 : option2;
    }

    /**
     * A → T → G → C → A の循環
     */
    private static GeneType nextGene(GeneType type) {
        return switch (type) {
            case A -> GeneType.T;
            case T -> GeneType.G;
            case G -> GeneType.C;
            case C -> GeneType.A;
        };
    }

    /*
    private static boolean isCG(Gene gene) {
        return gene.getLeft() == GeneType.C && gene.getRight() == GeneType.G;
    }
    */

    /*
    private static boolean isCC(Gene gene) {
        return gene.getLeft() == GeneType.C && gene.getRight() == GeneType.C;
    }
     */
}
