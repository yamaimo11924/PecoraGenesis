package your.domain.minecraft.pecoraGenesis.animals;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class Rabbit {

    public static void apply(Player player, int level) {

        switch(level) {

            case 1:
                int time1 = ThreadLocalRandom.current().nextInt(20, 31);
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.JUMP_BOOST,
                                time1 * 20,
                                0
                        ));
                break;

            case 2:
                int time2 = ThreadLocalRandom.current().nextInt(40, 51);
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.JUMP_BOOST,
                                time2 * 20,
                                0
                        ));
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.SLOW_FALLING,
                                time2 * 20,
                                0
                        ));
                break;

            case 3:
                int time3 = ThreadLocalRandom.current().nextInt(20, 31);
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.JUMP_BOOST,
                                time3 * 20,
                                1
                        ));
                player.addPotionEffect(
                        new PotionEffect(
                                PotionEffectType.SLOW_FALLING,
                                time3 * 20,
                                0
                        ));
                break;
        }
    }

}
