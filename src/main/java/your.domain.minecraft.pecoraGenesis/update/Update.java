package your.domain.minecraft.pecoraGenesis.update;

import your.domain.minecraft.pecoraGenesis.PecoraGenesis;
import org.bukkit.Bukkit;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Update {

    // GitHub のリリース API
    private static final String GITHUB_API_URL = "https://api.github.com/repos/yamaimo11924/PecoraGenesis/releases/latest";

    public static void check(PecoraGenesis plugin) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URI uri = new URI(GITHUB_API_URL);
                URL url = uri.toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                    String latestVersion = json.get("tag_name").getAsString();

                    if (latestVersion.startsWith("v") || latestVersion.startsWith("V")) {
                        latestVersion = latestVersion.substring(1);
                    }

                    @SuppressWarnings("deprecation")
                    String currentVersion = plugin.getDescription().getVersion();


                    if (!currentVersion.equals(latestVersion)) {
                        Bukkit.getConsoleSender().sendMessage(
                                Component.text("[PecoraGenesis] New version available: " + latestVersion, NamedTextColor.GOLD)
                        );

                        String finalLatestVersion = latestVersion;
                        Bukkit.getOnlinePlayers().stream()
                                .filter(player -> player.isOp())
                                .forEach(player -> player.sendMessage(
                                        Component.text("[PecoraGenesis] Update available: " + finalLatestVersion, NamedTextColor.AQUA)
                                ));
                    } else {
                        Bukkit.getConsoleSender().sendMessage(
                                Component.text("[PecoraGenesis] Plugin is up to date.", NamedTextColor.GREEN)
                        );
                    }
                }

            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(
                        Component.text("[PecoraGenesis] Failed to check updates: " + e.getMessage(), NamedTextColor.RED)
                );
            }
        });
    }
}
