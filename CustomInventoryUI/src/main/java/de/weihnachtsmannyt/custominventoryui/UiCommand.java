package de.weihnachtsmannyt.custominventoryui;

import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.ToastNotification;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UiCommand implements CommandExecutor {

    // Format the title for the inventory
    private static final String TEST_INV_TITLE = ChatColor.translateAlternateColorCodes('&',
            CharRepo.getNeg(8) + "&f" + CharRepo.MENU_CONTAINER_27
                    + CharRepo.getNeg(170) + "&cTest&7"
                    + CharRepo.getPos(124) + CharRepo.MENU_BUTTON
                    + CharRepo.getNeg(100) + CharRepo.MENU_NAME
    );

    private Inventory testInventory; // Store the inventory instance

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ui.admin")) {
            sender.sendMessage("§cDu darfst dies nicht!");
            return true;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Create an inventory with 27 slots and the formatted title
            testInventory = Bukkit.createInventory(null, 27, TEST_INV_TITLE);

            ItemStack PlayerHead = new ItemStack(CustomInventoryUI.getInstance().getMethodsManager().getHead(player, CharRepo.getSpacing(1)
                    , null, "placeholder", 20));

            testInventory.setItem(10, PlayerHead);
            testInventory.setItem(8, CustomInventoryUI.getInstance().getMethodsManager()
                    .CreateItemWithMaterial(Material.PAPER, 1, 1, "§7close", null, "close", 1));

            // Open the inventory for the player
            player.openInventory(testInventory);

            ToastNotification notification = new ToastNotification(PlayerHead, "§fYou opened §6Test", AdvancementDisplay.AdvancementFrame.TASK);
            notification.send(player);

            return true; // Command was successful
        } else {
            // If the sender is not a player, send an error message
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return false; // Command was not successful
        }
    }

    public Inventory getTestInventory() {
        return testInventory;
    }

    public String getTestInvTitle() {
        return TEST_INV_TITLE;
    }
}
