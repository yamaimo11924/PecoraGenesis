package your.domain.minecraft.pecoraGenesis;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import your.domain.minecraft.pecoraGenesis.animals.LivestockManager;
import your.domain.minecraft.pecoraGenesis.config.ConfigManager;
import your.domain.minecraft.pecoraGenesis.gene.GeneDatabase;
import your.domain.minecraft.pecoraGenesis.language.LanguageManager;
import your.domain.minecraft.pecoraGenesis.listener.*;
import your.domain.minecraft.pecoraGenesis.update.Update;

import java.util.Objects;

public final class PecoraGenesis extends JavaPlugin {

    private static PecoraGenesis instance;

    private ConfigManager configManager;
    private GeneDatabase geneDatabase;
    private LivestockManager livestockManager;

    private LanguageManager languageManager;

    public static PecoraGenesis getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        // config.yml
        saveDefaultConfig();

        // Language
        languageManager = new LanguageManager(this);

        getLogger().info(languageManager.get("plugin-start"));

        // ConfigManager
        configManager = new ConfigManager(this);
        configManager.load();

        // database
        geneDatabase = new GeneDatabase(this);
        geneDatabase.load();

        // manager
        livestockManager = new LivestockManager(this);

        // listener
        registerListeners();

        // update
        Update.check(this);

        // Command
        Objects.requireNonNull(this.getCommand("pecora")).setExecutor(new your.domain.minecraft.pecoraGenesis.commands.PecoraCommand());
        Objects.requireNonNull(this.getCommand("pecora")).setTabCompleter(new your.domain.minecraft.pecoraGenesis.commands.PecoraCommand());

        getLogger().info(languageManager.get("plugin-enabled"));

    }

    @Override
    public void onDisable() {

        getLogger().info(languageManager.get("saving-database"));

        if (geneDatabase != null) {
            geneDatabase.close();
        }

        getLogger().info(languageManager.get("plugin-disabled"));
    }

    private void registerListeners() {

        Bukkit.getPluginManager().registerEvents(
                new BreedListener(livestockManager), this);

        Bukkit.getPluginManager().registerEvents(
                new GeneInspectListener(livestockManager), this);

        Bukkit.getPluginManager().registerEvents(
                new DropListener(livestockManager), this);

        Bukkit.getPluginManager().registerEvents(
                new PlayerEatListener(livestockManager), this);

        Bukkit.getPluginManager().registerEvents(
                new MilkListener(livestockManager), this);

        Bukkit.getPluginManager().registerEvents(
                new CommandListener(livestockManager), this);

        Bukkit.getPluginManager().registerEvents(
                new CampfireLogListener(livestockManager), this);

        Bukkit.getPluginManager().registerEvents(
                new FurnaceListener(livestockManager), this);

    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GeneDatabase getGeneDatabase() {
        return geneDatabase;
    }

    public LivestockManager getLivestockManager() {
        return livestockManager;
    }

    public LanguageManager getLanguage() {
        return languageManager;
    }

}
