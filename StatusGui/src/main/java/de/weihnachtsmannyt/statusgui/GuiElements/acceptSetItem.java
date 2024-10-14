package de.weihnachtsmannyt.statusgui.GuiElements;

import de.weihnachtsmannyt.status.Status;
import de.weihnachtsmannyt.statusgui.Statusgui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.AnvilWindow;
import xyz.xenondevs.invui.window.Window;
import xyz.xenondevs.invui.window.WindowManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class acceptSetItem extends AbstractItem {
    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Statusgui.getInstance().getMethodsManager().CreateSkull("§aAnnehmen", "§r",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkyZTMxZmZiNTljOTBhYjA4ZmM5ZGMxZmUyNjgwMjAzNWEzYTQ3YzQyZmVlNjM0MjNiY2RiNDI2MmVjYjliNiJ9fX0="));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        Window window = WindowManager.getInstance().getOpenWindow(player);

        //Get Anvil instance
        if (window instanceof AnvilWindow anvilWindow) {
            //Get text
            String text = anvilWindow.getRenameText();
            anvilWindow.close();
            //Do stuff with text
            Status.getInstance().getFileManager().savePlayerInStatus(player, text, "§f");
            Status.getInstance().getPrefixManager().updatePrefixAllPlayers();

            @NotNull ItemBuilder placeholder = new ItemBuilder(Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.BLACK_STAINED_GLASS_PANE, 0, 1, "§r", null, "placeholder"));
            @NotNull ItemBuilder placeholder_t = new ItemBuilder(Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.WHITE_STAINED_GLASS_PANE, 0, 1, "§r", null, "placeholder"));

            //16 color

            List<Item> woolItems = Arrays.stream(Material.values())
                    .filter(material ->
                            !material.isAir()
                                    && material.isItem()
                                    && material.name().toLowerCase().contains("wool")
                                    && !material.name().toLowerCase().contains("pink")
                                    && !material.name().toLowerCase().contains("brown")
                                    && !material.name().toLowerCase().contains("orange")
                    )
                    .map(material -> new SimpleItem(new ItemBuilder(material)))
                    .collect(Collectors.toList());

            woolItems.add(new SimpleItem(new ItemBuilder(Material.GOLD_INGOT)));
            woolItems.add(new SimpleItem(new ItemBuilder(Material.PRISMARINE)));
            woolItems.remove(13);

            Gui colorInv = Gui.normal()
                    .setStructure(
                            "x x x x x x x x x",
                            "x x x x x x x x x",
                            "; ; ; ; ; ; ; ; ;",
                            ", , , , , , , , b")
                    .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                    .addIngredient(',', placeholder)
                    .addIngredient(';', placeholder_t)
                    .addIngredient('b', new BackItem())
                    .setBackground(placeholder)
                    .build();

            for (Item item : woolItems) {
                colorInv.addItems(new colorItems(item));
            }

            Window colorWindow = Window.single()
                    .setViewer(player)
                    .setTitle("Set your Color")
                    .setGui(colorInv)
                    .build();
            colorWindow.open();
            event.setCancelled(true);
        }
    }
}
