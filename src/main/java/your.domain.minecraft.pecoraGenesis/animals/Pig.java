package your.domain.minecraft.pecoraGenesis.animals;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class Pig {

    public static void apply(Player player, int level) {

        switch(level) {

            case 1:
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.HASTE,
                                ThreadLocalRandom.current().nextInt(30, 41) * 20,
                                0
                        ));
                break;

            case 2:
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.HASTE,
                                ThreadLocalRandom.current().nextInt(60, 71) * 20,
                                0
                        ));
                break;

            case 3:
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.HASTE,
                                ThreadLocalRandom.current().nextInt(20, 31) * 20,
                                1
                        ));
                break;
        }
    }
}
