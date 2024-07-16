package de.weihnachtsmannyt.statusgui.Commands;

import de.weihnachtsmannyt.status.Status;
import de.weihnachtsmannyt.statusgui.Statusgui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Objects;

public class guicommand implements CommandExecutor {



    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            System.out.println("You must be a player!") ;
            return false;
        }

        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Status Gui");

        if (args.length > 1) {
            sendUsage(player);
            return true;
        } else {
            inventory.setItem(22, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.BARRIER, 0, 1, "§7Close", null, "close"));
            inventory.setItem(11, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.NAME_TAG, 0, 1, "§3Set", null, "set"));
            inventory.setItem(15, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.SPYGLASS,0, 1, "§4Get", null, "get"));
            inventory.setItem(18, Statusgui.getInstance().getMethodsManager().CreateSkull("§aP-Settings", "pSettings", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkNDliYWU5NWM3OTBjM2IxZmY1YjJmMDEwNTJhNzE0ZDYxODU0ODFkNWIxYzg1OTMwYjNmOTlkMjMyMTY3NCJ9fX0="));


            if (player.hasPermission("statusgui.settings")) {
                ItemStack aSettings = Statusgui.getInstance().getMethodsManager().
                        CreateSkull("§aA-Settings", "aSettings",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkNDliYWU5NWM3OTBjM2IxZmY1YjJmMDEwNTJhNzE0ZDYxODU0ODFkNWIxYzg1OTMwYjNmOTlkMjMyMTY3NCJ9fX0=");
                Objects.requireNonNull(aSettings.getItemMeta()).setLore(Collections.singletonList("Admin Settings"));
                inventory.setItem(26, aSettings);

            }
            inventory.setItem(4, Statusgui.getInstance().getMethodsManager().getHead(player,
                    "Your Status", ChatColor.RESET +
                           Objects.requireNonNull(player.getScoreboard().getTeam(player.getPlayerListName())).getPrefix()
                             + player.getDisplayName(), "playerSkull"));

            player.openInventory(inventory);
        }
        return false;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage("        §c§k!!!!§r§9This Plugin was coded by §cWeihnachtsmannYT§c§k!!!!");
        sender.sendMessage("    §c§k!!!!§r§9This Plugin was coded by §cWeihnachtsmannYT§c§k!!!!");
        sender.sendMessage("    §7Verwendung§8: §9/statusgui       ");
    }
}
