package your.domain.minecraft.pecoraGenesis.commands;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import your.domain.minecraft.pecoraGenesis.PecoraGenesis;
import your.domain.minecraft.pecoraGenesis.gene.Gene;
import your.domain.minecraft.pecoraGenesis.language.LanguageManager;

import java.util.*;

public class AdminCommand {

    private static final Map<UUID, Boolean> clickModeMap = new HashMap<>();
    private static final Map<UUID, Gene> clickGeneMap = new HashMap<>();

    LanguageManager lang = PecoraGenesis.getInstance().getLanguage();

    // クリックモードを判定する
    public static boolean isClickMode(Player player) {
        return clickModeMap.getOrDefault(player.getUniqueId(), false);
    }

    // プレイヤーのクリック遺伝子を取得
    public static Gene getClickGene(Player player) {
        return clickGeneMap.get(player.getUniqueId());
    }

    // /pecora admin click 実行時にセット
    public static void setClickGene(Player player, Gene gene) {
        clickGeneMap.put(player.getUniqueId(), gene);
    }

    // /pecora admin clickmode 実行時にモードをセット
    public static void setClickMode(Player player, boolean mode) {
        clickModeMap.put(player.getUniqueId(), mode);
    }


    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(lang.get("no-permission"));
            return;
        }

        // /pecora a だけ実行した場合
        if (args.length == 0) {
            sender.sendMessage(lang.get("general-help")); // 一般用メッセージ
            // OP または pecora.admin を持つ場合に管理者用メッセージを追加
            if (player.hasPermission("pecora.admin") || player.isOp()) {
                sender.sendMessage(lang.get("admin-help"));
            }
            return;
        }

        // 標準のサブコマンド処理
        switch (args[0].toLowerCase()) {
            case "spawn", "clickmode", "click" -> {
                if (!player.hasPermission("pecora.admin") && !player.isOp()) {
                    player.sendMessage(lang.get("no-permission"));
                    return;
                }

                switch (args[0].toLowerCase()) {
                    case "spawn" -> runSpawn(player, Arrays.copyOfRange(args, 1, args.length));
                    case "clickmode" -> runClickMode(player, Arrays.copyOfRange(args, 1, args.length));
                    case "click" -> runClick(player, Arrays.copyOfRange(args, 1, args.length));
                }
            }
            default -> sender.sendMessage(lang.get("unknown-subcommand").replace("{arg}", args[0]));
        }
    }

    private void runSpawn(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(lang.get("admin-spawn-usage"));
            return;
        }

        String typeStr = args[0].toLowerCase();
        EntityType type;
        switch (typeStr) {
            case "cow" -> type = EntityType.COW;
            case "pig" -> type = EntityType.PIG;
            case "sheep" -> type = EntityType.SHEEP;
            case "rabbit" -> type = EntityType.RABBIT;
            default -> {
                player.sendMessage(lang.get("unknown-entity").replace("{arg}", args[0]));
                return;
            }
        }

        // 遺伝子を作成
        Gene geneObj = (args.length > 1) ? Gene.fromString(args[1].toUpperCase()) : Gene.fromString("AA");

        // 生物をスポーン
        LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), type);

        // 遺伝子を PDC に保存
        entity.getPersistentDataContainer().set(
                new NamespacedKey("pecora", "gene"),
                PersistentDataType.STRING,
                geneObj.toString()
        );

        // LivestockManager にも確実にセット
        PecoraGenesis.getInstance().getLivestockManager().setGene(entity, geneObj);

        // メッセージ表示
        player.sendMessage(lang.get("spawn-success")
                .replace("{type}", typeStr)
                .replace("{gene}", geneObj.toString()));
    }

    private void runClickMode(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(lang.get("admin-clickmode-usage"));
            return;
        }

        String mode = args[0].toLowerCase();
        if (mode.equals("on")) {
            setClickMode(player, true);
            player.sendMessage(lang.get("clickmode-on"));
        } else if (mode.equals("off")) {
            setClickMode(player, false);
            player.sendMessage(lang.get("clickmode-off"));
        } else {
            player.sendMessage(lang.get("unknown-mode").replace("{arg}", args[0]));
        }
    }

    private void runClick(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(lang.get("admin-click-usage"));
            return;
        }

        Gene gene = Gene.fromString(args[0].toUpperCase());
        setClickGene(player, gene);
        player.sendMessage(lang.get("click-gene-set").replace("{gene}", gene.toString()));
    }

    @Nullable
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return new ArrayList<>();

        if (args.length == 1) {
            List<String> subs = new ArrayList<>();
            // 権限のあるプレイヤーだけ admin サブコマンドを追加
            if (player.hasPermission("pecora.admin") || player.isOp()) {
                subs.addAll(Arrays.asList("spawn", "clickmode", "click"));
            }
            List<String> filtered = new ArrayList<>();
            for (String s : subs) if (s.startsWith(args[0].toLowerCase())) filtered.add(s);
            return filtered;
        } else if (args[0].equalsIgnoreCase("spawn") && args.length == 2) {
            if (player.hasPermission("pecora.admin") || player.isOp()) {
                return Arrays.asList("cow", "pig", "sheep", "rabbit");
            }
        } else if (args[0].equalsIgnoreCase("clickmode") && args.length == 2) {
            if (player.hasPermission("pecora.admin") || player.isOp()) {
                return Arrays.asList("on", "off");
            }
        } else if (args[0].equalsIgnoreCase("click") && args.length == 2) {
            if (player.hasPermission("pecora.admin") || player.isOp()) {
                return Arrays.asList("AA", "AT", "TA", "GG", "CC");
            }
        }
        return new ArrayList<>();
    }
}
