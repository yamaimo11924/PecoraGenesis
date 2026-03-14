package your.domain.minecraft.pecoraGenesis.trait;

import java.util.HashMap;
import java.util.Map;

public class GeneTrait {

    private static final Map<String, Trait> TRAITS = new HashMap<>();

    static {

        // 野生型
        TRAITS.put("AA", new Trait(0,0,0,0));

        // 半野生型
        TRAITS.put("TA", new Trait(0,1,1,0));
        TRAITS.put("AT", new Trait(0,1,2,0));

        // 中間型
        TRAITS.put("TT", new Trait(0,2,0,0));
        TRAITS.put("GA", new Trait(0,0,1,0));
        TRAITS.put("AG", new Trait(1,0,2,0));
        // 半家畜型
        TRAITS.put("GG", new Trait(3,0,0,0));
        TRAITS.put("CA", new Trait(0,0,1,1));
        TRAITS.put("GT", new Trait(2,1,0,0));
        TRAITS.put("TG", new Trait(1,2,0,0));
        TRAITS.put("AC", new Trait(0,0,2,1));
        // 完全家畜型
        TRAITS.put("TC", new Trait(0,2,0,1));
        TRAITS.put("CT", new Trait(0,1,0,2));
        TRAITS.put("CG", new Trait(1,0,0,2));
        TRAITS.put("CC", new Trait(0,0,0,3));
        TRAITS.put("GC", new Trait(2,0,0,1));
    }

    public static Trait getTrait(String gene) {
        return TRAITS.getOrDefault(gene, new Trait(0,0,0,0));
    }
}
