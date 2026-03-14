package your.domain.minecraft.pecoraGenesis.language;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import your.domain.minecraft.pecoraGenesis.PecoraGenesis;

import java.io.File;

public class LanguageManager {

    private final PecoraGenesis plugin;
    private FileConfiguration lang;
    private String language;

    public LanguageManager(PecoraGenesis plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {

        File file = new File(plugin.getDataFolder(), "language.yml");

        if (!file.exists()) {
            plugin.saveResource("language.yml", false);
        }

        lang = YamlConfiguration.loadConfiguration(file);

        language = plugin.getConfig().getString("language", "en");
        language = plugin.getConfig().getString("language", "ja");
    }

    public String get(String key) {

        String path = language + "." + key;

        if (lang.contains(path)) {
            return lang.getString(path);
        }

        return "MissingLang: " + key;
    }
}
