package your.domain.minecraft.pecoraGenesis.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import your.domain.minecraft.pecoraGenesis.PecoraGenesis;
import your.domain.minecraft.pecoraGenesis.language.LanguageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PecoraCommand implements CommandExecutor, TabCompleter {

    private final AdminCommand adminCommand = new AdminCommand();
    private final GeneralCommand generalCommand = new GeneralCommand();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String @NotNull [] args) {

        LanguageManager lang = PecoraGenesis.getInstance().getLanguage();

        if (args.length == 0) {
            // 一般プレイヤーでも表示
            generalCommand.execute(sender, args);
            return true;
        }

        String sub = args[0].toLowerCase();
        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);

        switch (sub) {
            case "version", "ver" -> generalCommand.execute(sender, subArgs);
            case "admin" -> {
                // admin 以下は権限チェック
                if (!(sender instanceof Player player) || (!player.hasPermission("pecora.admin") && !player.isOp())) {
                    sender.sendMessage(lang.get("no-permission"));
                    return true;
                }
                adminCommand.execute(sender, subArgs);
            }
            default -> sender.sendMessage(lang.get("unknown-subcommand").replace("{arg}", args[0]));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("version"); // 誰でも見える
            if (sender instanceof Player player && (player.hasPermission("pecora.admin") || player.isOp())) {
                completions.add("admin"); // 権限ある人だけ
            }
        }
        else if (args[0].equalsIgnoreCase("admin")) {
            completions = adminCommand.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        } else if (args[0].equalsIgnoreCase("version")) {
            completions = generalCommand.tabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return completions;
    }
}
