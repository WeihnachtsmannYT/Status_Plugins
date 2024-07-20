package de.weihnachtsmannyt.custominventoryui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class EventManager implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        // Check if the inventory instance matches the stored inventory
        UiCommand uiCommand = CustomInventoryUI.getInstance().getUiCommand();
        if (!event.getView().getTitle().equals(uiCommand.getTestInvTitle())) return;

        Player p = (Player) event.getWhoClicked();

        ItemStack placeholder = CustomInventoryUI.getInstance().getMethodsManager().CreateItemWithMaterial(Material.BLACK_STAINED_GLASS_PANE, 0, 1, "§r", null, "placeholder", 0);
        switch (Objects.requireNonNull(MethodsManager.getCustomLocalizedName(event.getCurrentItem()))) {
            case "close":
                p.closeInventory();
                event.setCancelled(true);
                break;
            case "test":
                p.closeInventory();
                p.sendMessage("test");
                event.setCancelled(true);
                break;

            case "placeholder":
            default:
                event.setCancelled(true);
        }
    }
}
