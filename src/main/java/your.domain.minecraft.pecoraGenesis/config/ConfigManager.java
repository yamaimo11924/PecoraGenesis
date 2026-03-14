package your.domain.minecraft.pecoraGenesis.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import org.bukkit.entity.EntityType;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    private int inheritChance;
    private int dominanceChance;
    private int swapChance;
    private Material inspectItem;
    private final Map<EntityType, List<Material>> feedItems = new EnumMap<>(EntityType.class);


    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void load() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        inheritChance = config.getInt("gene.inherit_chance", 75);
        dominanceChance = config.getInt("gene.dominance_chance", 20);
        swapChance = config.getInt("gene.swap_chance", 5);
        inspectItem = Material.valueOf(config.getString("gene.inspect_item", "STONE_HOE"));

        feedItems.clear();
        if (config.isConfigurationSection("gene.feed_item")) {
            for (String key : Objects.requireNonNull(config.getConfigurationSection("gene.feed_item")).getKeys(false)) {
                try {
                    EntityType type = EntityType.valueOf(key);
                    List<String> itemNames = config.getStringList("gene.feed_item." + key);
                    List<Material> materials = new ArrayList<>();
                    for (String name : itemNames) {
                        try {
                            materials.add(Material.valueOf(name));
                        } catch (IllegalArgumentException ignored) {}
                    }
                    if (!materials.isEmpty()) feedItems.put(type, materials);
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }

    public int getInheritChance() { return inheritChance; }
    public int getDominanceChance() { return dominanceChance; }
    public int getSwapChance() { return swapChance; }
    public Material getInspectItem() { return inspectItem; }

    public List<Material> getFeedItems(EntityType type) {
        return feedItems.getOrDefault(type, Collections.emptyList());
    }
}
