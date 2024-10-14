package de.weihnachtsmannyt.statusgui.GuiElements;

import de.weihnachtsmannyt.status.Status;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class colorItems extends AbstractItem {

    Item internwoolItems;

    public colorItems(Item item) {
        internwoolItems = item;
    }

    public static boolean isWool(Material wool) {
        return wool.name().contains("_WOOL");// Not a valid wool material
    }

    public static String getColorNameOfWool(Material wool) {
        if (wool.name().contains("_WOOL")) {
            return "not valid";// Not a valid wool material
        }

        // Extract the color name from the material name (e.g., "MAGENTA_WOOL" -> "MAGENTA")
        String colorName = wool.name().split("_")[0];

        if (colorName.equals("LIGHT")) {
            if (wool.name().contains("LIGHT_BLUE")) {
                return "LIGHT_BLUE";
            } else if (wool.name().contains("LIGHT_GRAY")) {
                return "LIGHT_GRAY";
            }
        }

        return colorName;
    }

    public static String getcolorCodeOfWool(Material wool) {
        DyeColor dyeColor;
        try {
            dyeColor = DyeColor.valueOf(getColorNameOfWool(wool));
        } catch (IllegalArgumentException e) {
            return null; // Invalid dye color
        }

        // Return the Minecraft color code corresponding to the DyeColor
        return getColorCodeFromWool(dyeColor);
    }

    public static String getColorCodeFromWool(DyeColor dyeColor) {
        switch (dyeColor) {
            case BLACK:
                return "§0";  // Schwarz
            case BLUE:
                return "§1";  // Dunkelblau
            case GREEN:
                return "§2";  // Dunkelgrün
            case RED:
                return "§4";  // Dunkelrot
            case PURPLE:
                return "§5";  // Dunkelviolett
            case LIGHT_GRAY:
                return "§7";  // Grau
            case GRAY:
                return "§8";  // Dunkelgrau
            case LIGHT_BLUE:
                return "§9";  // Blau
            case LIME:
                return "§a";  // Grün
            case CYAN:
                return "§b";  // Aqua
            case MAGENTA:
                return "§d";  // Hellviolett
            case YELLOW:
                return "§e";  // Gelb
            case WHITE:
                return "§f";  // Weiß
            default:
                return "Unknown Wool Color ";
        }
    }

    @Override
    public ItemProvider getItemProvider() {
        return internwoolItems.getItemProvider();
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();

        YamlConfiguration statusData = Status.getInstance().getFileManager().getStatusData();
        String staus = statusData.getString(player.getUniqueId() + ".status");


        String color = "";
        if (event.getCurrentItem().getType() == Material.GOLD_INGOT) {
            color = "§6";
        }

        if (event.getCurrentItem().getType() == Material.PRISMARINE) {
            color = "§3";
        }
        color = isWool(event.getCurrentItem().getType()) ? getcolorCodeOfWool(event.getCurrentItem().getType()) : color;

        Status.getInstance().getFileManager().savePlayerInStatus(p, staus, color);
        Status.getInstance().getPrefixManager().updatePrefixAllPlayers();

        if (Status.getInstance().getDebug()) {
            p.sendMessage("Color: " + (isWool(event.getCurrentItem().getType()) ? getcolorCodeOfWool(event.getCurrentItem().getType()) : color) + getColorNameOfWool(event.getCurrentItem().getType()));
            p.performCommand("status update deaths-debugger");
        }

        p.closeInventory();
        event.setCancelled(true);
    }
}
