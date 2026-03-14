package your.domain.minecraft.pecoraGenesis.animals;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class Cow {

    public static void apply(Player player, int level) {

        switch(level) {

            case 1:
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.STRENGTH,
                                ThreadLocalRandom.current().nextInt(10, 21) * 20,
                                0
                        ));
                break;

            case 2:
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.STRENGTH,
                                ThreadLocalRandom.current().nextInt(40, 51) * 20,
                                0
                        ));
                break;

            case 3:
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.STRENGTH,
                                ThreadLocalRandom.current().nextInt(5, 11) * 20,
                                1
                        ));
                break;
        }
    }

}
