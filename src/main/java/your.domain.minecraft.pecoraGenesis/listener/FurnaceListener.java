package your.domain.minecraft.pecoraGenesis.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import your.domain.minecraft.pecoraGenesis.animals.LivestockManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.List;

public class FurnaceListener implements Listener {

    private final LivestockManager livestockManager;

    public FurnaceListener(LivestockManager livestockManager) {
        this.livestockManager = livestockManager;
    }

    @EventHandler
    public void onFurnaceStart(FurnaceStartSmeltEvent event) {

        Furnace furnaceBlock = event.getBlock().getState() instanceof Furnace f ? f : null;
        if (furnaceBlock == null) return;

        FurnaceInventory inv = furnaceBlock.getInventory();
        ItemStack input = inv.getSmelting();

        if (input == null || input.getType() == Material.AIR) return;

        ItemMeta meta = input.getItemMeta();
        if (meta == null) return;

        // loreをComponent形式で取得
        List<Component> loreComponents = meta.lore(); // nullの場合もある
        if (loreComponents == null || loreComponents.isEmpty()) return;

        // Componentを文字列化
        List<String> lore = loreComponents.stream()
                .map(component -> PlainTextComponentSerializer.plainText().serialize(component))
                .toList();

        boolean containsTarget = lore.stream().anyMatch(line -> line.contains("FOOD") || line.contains("STATUS"));
        if (!containsTarget) return;

        // かまどから取り除く
        inv.setSmelting(null);

        // かまどの向きに応じてドロップ位置を調整
        BlockFace facing = furnaceBlock.getBlock().getBlockData() instanceof org.bukkit.block.data.Directional dir
                ? dir.getFacing()
                : BlockFace.NORTH;

        Location dropLoc = furnaceBlock.getBlock().getLocation().add(0.5, 1, 0.5);
        switch (facing) {
            case NORTH -> dropLoc.add(0, 0, -1);
            case SOUTH -> dropLoc.add(0, 0, 1);
            case WEST -> dropLoc.add(-1, 0, 0);
            case EAST -> dropLoc.add(1, 0, 0);
            default -> {}
        }

        furnaceBlock.getWorld().dropItem(dropLoc, input);
    }
}
