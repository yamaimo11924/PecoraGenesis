package your.domain.minecraft.pecoraGenesis.listener;

import your.domain.minecraft.pecoraGenesis.PecoraGenesis;
import your.domain.minecraft.pecoraGenesis.commands.AdminCommand;
import your.domain.minecraft.pecoraGenesis.animals.LivestockManager;
import your.domain.minecraft.pecoraGenesis.gene.Gene;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import your.domain.minecraft.pecoraGenesis.language.LanguageManager;

public class CommandListener implements Listener {

    private final LivestockManager livestockManager;

    LanguageManager lang = PecoraGenesis.getInstance().getLanguage();

    public CommandListener(LivestockManager livestockManager) {
        this.livestockManager = livestockManager;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        // クリックモード OFF なら何もしない
        if (!AdminCommand.isClickMode(player)) return;

        // 対象が家畜じゃなければ何もしない
        if (!(event.getRightClicked() instanceof LivingEntity entity)) return;
        if (!livestockManager.isLivestock(entity)) return;

        // プレイヤーが直前に /pecora admin click で指定した遺伝子を取得
        Gene gene = AdminCommand.getClickGene(player);
        if (gene == null) return; // 指定がなければ何もしない

        // 遺伝子を家畜に適用
        livestockManager.setGene(entity, gene);

        // メッセージ
        player.sendMessage(
                lang.get("gene-applied")
                        .replace("{gene}", gene.toString())
                        .replace("{entity}", entity.getType().toString())
        );

    }
}
