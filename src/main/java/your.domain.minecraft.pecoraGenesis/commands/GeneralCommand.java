package your.domain.minecraft.pecoraGenesis.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class GeneralCommand {

    @SuppressWarnings("deprecation") // getDescription() の警告を抑制
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        var plugin = your.domain.minecraft.pecoraGenesis.PecoraGenesis.getInstance();

        // Paper/Spigot/Purpur 共通で安全
        String version = plugin.getDescription().getVersion();

        sender.sendMessage(
                Component.text("PecoraGenesis Version: ", NamedTextColor.GREEN)
                        .append(Component.text(version, NamedTextColor.YELLOW))
        );
    }

    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
