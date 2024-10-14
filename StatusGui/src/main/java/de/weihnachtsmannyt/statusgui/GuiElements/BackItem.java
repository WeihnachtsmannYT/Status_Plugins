package de.weihnachtsmannyt.statusgui.GuiElements;

import de.weihnachtsmannyt.statusgui.Statusgui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class BackItem extends AbstractItem {

    private int count;

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Statusgui.getInstance().getMethodsManager().CreateSkull("§7Zurück", "zurück",
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlODFmODA4Nzk3ZjVmNmYxODk2OTM0MTU0MzcyMzNiYjQ0YzQyZDM1YzczNzA4OTIyZTMwZGY4MmRjM2M5MiJ9fX0="));

    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick()) {
            player.closeInventory();
            player.performCommand("statusgui");
            event.setCancelled(true);
        }
        notifyWindows(); // this will update the ItemStack that is displayed to the player
    }
}