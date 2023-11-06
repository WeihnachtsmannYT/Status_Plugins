package de.weihnachtsmannyt.statusgui.Managers;

import com.google.common.collect.ImmutableSet;
import de.weihnachtsmannyt.status.Status;
import de.weihnachtsmannyt.statusgui.Statusgui;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class InvListener implements Listener {

    private static final Set<String> TITLES = ImmutableSet.of("Status Gui", "Status Set", "Status Get", "Status A-Settings","Status P-Settings");

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (TITLES.contains(event.getView().getTitle())) {
            Player p = (Player) event.getWhoClicked();
            YamlConfiguration statusData = Status.getInstance().getFileManager().getStatusData();
            event.setCancelled(true);
            if (Objects.requireNonNull(event.getCurrentItem().getItemMeta()).hasLocalizedName()) {
                switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
                    case "close":
                        p.closeInventory();
                        break;
                    case "playerSkull":
                    case "placeholder":
                        event.setCancelled(true);
                        break;
                    case "zurück":
                        p.closeInventory();
                        p.performCommand("statusgui");
                        break;
                    case "set":
                        if (p.hasPermission("status.set")) {
                            new AnvilGUI.Builder()
                                    .onClose(stateSnapshot -> stateSnapshot.getPlayer().sendMessage("You closed the inventory."))
                                    .onClick((slot, stateSnapshot) -> { // Either use sync or async variant, not both
                                        if (slot != AnvilGUI.Slot.OUTPUT) {
                                            return Collections.emptyList();
                                        }

                                        if (stateSnapshot.getText().equalsIgnoreCase("reset")) {
                                            stateSnapshot.getPlayer().sendMessage("Du hast dein Status zurückgesetzt!");
                                            Statusgui.getInstance().getStatusApi().getFileManager().savePlayerInStatus(p, "Default", "§f");
                                            Statusgui.getInstance().getStatusApi().getPrefixManager().updatePrefixAllPlayers();
                                        } else {
                                            stateSnapshot.getPlayer().sendMessage("Du hast dein Status zu " + stateSnapshot.getText() + "!");
                                            Statusgui.getInstance().getStatusApi().getFileManager().savePlayerInStatus(p, stateSnapshot.getText(), null);
                                        }
                                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                                    })
                                    .text("What status do you want?")                              //sets the text the GUI should start with
                                    .title("Enter your Status.")                                       //set the title of the GUI (only works in 1.14+)
                                    .plugin(Statusgui.getInstance())                                          //set the plugin instance
                                    .open(p);                                                   //opens the GUI for the player provided
                        }
                        break;
                    case "setConfirm":
                        Inventory setInvColor = Bukkit.createInventory(p, 3 * 9, event.getView().getTitle());
                        p.openInventory(setInvColor);
                        Statusgui.getInstance().getStatusApi().getPrefixManager().updatePrefixAllPlayers();
                        break;
                    case "setColor":
                       // ItemStack setColor = event.getCurrentItem();
                    case "get":
                        Inventory getInv = Bukkit.createInventory(null, 3 * 9, "Status Get");

                        //Heads
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            getInv.setItem(Bukkit.getOnlinePlayers().size() - 1, Statusgui.getInstance().getMethodsManager().getHead(all, "Status von " + all.getDisplayName(),
                                    ChatColor.RESET +
                                            Objects.requireNonNull(p.getScoreboard().getTeam(Status.getInstance().getPrefixManager().getTeamByPlayer(all))).getPrefix() +
                                            p.getDisplayName(), "getSkull"));
                        }

                        getInv.setItem(18, Statusgui.getInstance().getMethodsManager().CreateSkull("§7Zurück", "zurück", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlODFmODA4Nzk3ZjVmNmYxODk2OTM0MTU0MzcyMzNiYjQ0YzQyZDM1YzczNzA4OTIyZTMwZGY4MmRjM2M5MiJ9fX0="));

                        //Placeholders
                        for (int i = 1; i < 9; i++) {
                            getInv.setItem(i + 18, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.GRAY_STAINED_GLASS_PANE, 1, 1, null, null, "placeholder"));
                        }

                        p.openInventory(getInv);
                        break;
                    case "getSkull":
                        //TODO getSkull
                        event.setCancelled(true);
                        break;

                    //P-Settings
                    case "pSettings":
                        Inventory pSettingsInv = Bukkit.createInventory(null,3*9,"Status P-Settings");

                        //Update Variables
                        Status.getInstance().getConfigVarManager().updateVar();

                        pSettingsInv.setItem(18, Statusgui.getInstance().getMethodsManager().CreateSkull("§7Zurück", "zurück", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlODFmODA4Nzk3ZjVmNmYxODk2OTM0MTU0MzcyMzNiYjQ0YzQyZDM1YzczNzA4OTIyZTMwZGY4MmRjM2M5MiJ9fX0="));

                        //Settings
                        pSettingsInv.setItem(2, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.PAPER, 1, 1, "Status_Prefix_on_off", null, ""));
                        pSettingsInv.setItem(3, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.ANVIL, 1, 1, "Join_Leave_Message_on_off", null, ""));
                        pSettingsInv.setItem(5, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.ANVIL, 1, 1, "DeathCounter_on_off", null, ""));
                        pSettingsInv.setItem(6, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.PAPER, 1, 1, "AutoAfk_on_off", null, ""));

                        //Toggles
                        pSettingsInv.setItem(11, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(statusData.getBoolean(p.getUniqueId()+".p-settings"+".Status_Prefix_on_off") ? Material.LIME_DYE : Material.RED_DYE, 1, 1, statusData.getBoolean(p.getUniqueId()+".p-settings"+".Status_Prefix_on_off") ? "on" : "off", null, "Status_Prefix_on_off"));
                        pSettingsInv.setItem(12, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(statusData.getBoolean(p.getUniqueId()+".p-settings"+".Join_Leave_Message_on_off") ? Material.LIME_DYE : Material.RED_DYE, 1, 1, statusData.getBoolean(p.getUniqueId()+".p-settings"+".Join_Leave_Message_on_off") ? "on" : "off", null, "Join_Leave_Message_on_off"));
                        pSettingsInv.setItem(14, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(statusData.getBoolean(p.getUniqueId()+".p-settings"+".DeathCounter_on_off") ? Material.LIME_DYE : Material.RED_DYE, 1, 1, statusData.getBoolean(p.getUniqueId()+".p-settings"+".DeathCounter_on_off") ? "on" : "off", null, "DeathCounter_on_off"));
                        pSettingsInv.setItem(15, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(statusData.getBoolean(p.getUniqueId()+".p-settings"+".AutoAfk_on_off") ? Material.LIME_DYE : Material.RED_DYE, 1, 1, statusData.getBoolean(p.getUniqueId()+".p-settings"+".AutoAfk_on_off") ? "on" : "off", null, /*"AutoAfk_on_off"**/ "AutoAfk_on_off"));

                        p.openInventory(pSettingsInv);
                        break;

                    case "Status_Prefix_on_off":
                    case "Join_Leave_Message_on_off":
                    case "DeathCounter_on_off":
                    case "AutoAfk_on_off":
                        String localizedName = event.getCurrentItem().getItemMeta().getLocalizedName();
                        statusData.set(p.getUniqueId()+".p-settings"+"."+localizedName,!statusData.getBoolean(p.getUniqueId()+".p-settings"+"."+localizedName));
                        Status.getInstance().getFileManager().reloadStatusFile();
                        event.getView().setItem(event.getSlot(), Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(statusData.getBoolean(p.getUniqueId()+".p-settings"+"."+localizedName) ? Material.LIME_DYE : Material.RED_DYE, 1, 1, statusData.getBoolean(p.getUniqueId()+".p-settings"+"."+localizedName) ? "on" : "off", null, localizedName));
                        Status.getInstance().getFileManager().saveStatusFile();

                        if (localizedName.equals("AutoAfk_on_off")){

                        }

                        Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
                        p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7Du hast " + localizedName + " " + (statusData.getBoolean(p.getUniqueId()+".p-settings"+"."+localizedName) ? "an" : "aus") + "geschallten.");
                        break;

                    //Settings
                    case "aSettings":
                        Inventory settingsInv = Bukkit.createInventory(null, 3 * 9, "Status A-Settings");

                        //Update Variables
                        Status.getInstance().getConfigVarManager().updateVar();

                        settingsInv.setItem(18, Statusgui.getInstance().getMethodsManager().CreateSkull("§7Zurück", "zurück", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlODFmODA4Nzk3ZjVmNmYxODk2OTM0MTU0MzcyMzNiYjQ0YzQyZDM1YzczNzA4OTIyZTMwZGY4MmRjM2M5MiJ9fX0="));

                        //Settings
                        settingsInv.setItem(0, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.PAPER, 1, 1, "Status-Prefix", null, ""));
                        settingsInv.setItem(1, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.ANVIL, 1, 1, "Status-Prefix-on/off", null, ""));
                        settingsInv.setItem(2, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.ANVIL, 1, 1, "Join/Leave-Message-on/off", null, ""));
                        settingsInv.setItem(3, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.PAPER, 1, 1, "JoinMessage", null, ""));
                        settingsInv.setItem(4, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.PAPER, 1, 1, "LeaveMassage", null, ""));
                        settingsInv.setItem(5, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.ANVIL, 1, 1, "DeathCounter-on/off", null, ""));
                        settingsInv.setItem(6, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.ANVIL, 1, 1, "BlockedWords-Toggle", null, ""));
                        settingsInv.setItem(7, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.SLIME_BALL, 1, 1, "Prefix-LengthLimit", null, ""));
                        settingsInv.setItem(8, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.ANVIL, 1, 1, "Uppercase/LengthLimit-Toggle", null, ""));

                        //Integers
                        ArrayList<String> lore_Prefix_LengthLimit = new ArrayList<>();
                        lore_Prefix_LengthLimit.add("Linksklick +1");
                        lore_Prefix_LengthLimit.add("Rechtsklick -1");
                        settingsInv.setItem(16, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.SNOWBALL, 1, Status.getInstance().getConfigVarManager().getPrefix_LengthLimit(), Status.getInstance().getConfigVarManager().getPrefix_LengthLimit().toString(), lore_Prefix_LengthLimit, "Prefix-LengthLimit"));

                        //Toggles
                        settingsInv.setItem(10, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Status.getInstance().getConfigVarManager().getStatus_Prefix_on_off() ? Material.LIME_DYE : Material.RED_DYE, 1, 1, Status.getInstance().getConfigVarManager().getStatus_Prefix_on_off() ? "on" : "off", null, "Status-Prefix-on/off"));
                        settingsInv.setItem(11, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Status.getInstance().getConfigVarManager().getJoin_Leave_Message_on_off() ? Material.LIME_DYE : Material.RED_DYE, 1, 1, Status.getInstance().getConfigVarManager().getJoin_Leave_Message_on_off() ? "on" : "off", null, "Join/Leave-Message-on/off"));
                        settingsInv.setItem(14, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Status.getInstance().getConfigVarManager().getDeathCounter_on_off() ? Material.LIME_DYE : Material.RED_DYE, 1, 1, Status.getInstance().getConfigVarManager().getDeathCounter_on_off() ? "on" : "off", null, "DeathCounter-on/off"));
                        settingsInv.setItem(15, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Status.getInstance().getConfigVarManager().getBlockedWords_Toggle() ? Material.LIME_DYE : Material.RED_DYE, 1, 1, Status.getInstance().getConfigVarManager().getBlockedWords_Toggle() ? "on" : "off", null, "BlockedWords-Toggle"));
                        settingsInv.setItem(17, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Status.getInstance().getConfigVarManager().getUppercase_LengthLimit_Toggle() ? Material.LIME_DYE : Material.RED_DYE, 1, 1, Status.getInstance().getConfigVarManager().getUppercase_LengthLimit_Toggle() ? "on" : "off", null, "Uppercase/LengthLimit-Toggle"));

                        p.openInventory(settingsInv);
                        break;

                    case "Prefix-LengthLimit":
                        lore_Prefix_LengthLimit = new ArrayList<>();
                        lore_Prefix_LengthLimit.add("Linksklick +1");
                        lore_Prefix_LengthLimit.add("Rechtsklick -1");
                        if (event.isLeftClick()) {
                            if (Status.getInstance().getConfigVarManager().getPrefix_LengthLimit() > 64)
                                Status.getInstance().getConfigVarManager().setPrefix_LengthLimit(64);
                            if (Status.getInstance().getConfigVarManager().getPrefix_LengthLimit() <= 63)
                                Status.getInstance().getConfigVarManager().setPrefix_LengthLimit(Status.getInstance().getConfigVarManager().getPrefix_LengthLimit() + 1);
                        }
                        if (event.isRightClick()) {
                            if (Status.getInstance().getConfigVarManager().getPrefix_LengthLimit() < 1)
                                Status.getInstance().getConfigVarManager().setPrefix_LengthLimit(1);
                            if (Status.getInstance().getConfigVarManager().getPrefix_LengthLimit() >= 2)
                                Status.getInstance().getConfigVarManager().setPrefix_LengthLimit(Status.getInstance().getConfigVarManager().getPrefix_LengthLimit() - 1);
                        }
                        event.getView().setItem(16, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.SNOWBALL, 1, Status.getInstance().getConfigVarManager().getPrefix_LengthLimit(), Status.getInstance().getConfigVarManager().getPrefix_LengthLimit().toString(), lore_Prefix_LengthLimit, "Prefix-LengthLimit"));
                        break;

                    case "Status-Prefix-on/off":
                        Status.getInstance().getConfigVarManager().setStatus_Prefix_on_off(!Status.getInstance().getConfigVarManager().getStatus_Prefix_on_off());
                        toggleUpdater(event,p);
                        break;
                    case "Join/Leave-Message-on/off":
                        Status.getInstance().getConfigVarManager().setJoin_Leave_Message_on_off(!Status.getInstance().getConfigVarManager().getJoin_Leave_Message_on_off());
                        toggleUpdater(event,p);
                        break;
                    case "DeathCounter-on/off":
                        Status.getInstance().getConfigVarManager().setDeathCounter_on_off(!Status.getInstance().getConfigVarManager().getDeathCounter_on_off());
                        toggleUpdater(event,p);
                        break;
                    case "BlockedWords-Toggle":
                        Status.getInstance().getConfigVarManager().setBlockedWords_Toggle(!Status.getInstance().getConfigVarManager().getBlockedWords_Toggle());
                        toggleUpdater(event,p);
                        break;
                    case "Uppercase/LengthLimit-Toggle":
                        Status.getInstance().getConfigVarManager().setUppercase_LengthLimit_Toggle(!Status.getInstance().getConfigVarManager().getUppercase_LengthLimit_Toggle());
                        toggleUpdater(event,p);
                        break;

                    default:
                        event.setCancelled(true);
                        System.out.println("Unexpected value: " + event.getCurrentItem().getItemMeta().getLocalizedName());
                }
                Status.getInstance().getFileManager().saveStatusFile();
            }
        }
    }

    private void toggleUpdater(InventoryClickEvent event,Player p) {
        //RELOAD everything
        Status.getInstance().getFileManager().reloadStatusFile();
        Status.getInstance().getConfigVarManager().updateVar();

        //Define Variables
        String localizedName = Objects.requireNonNull(Objects.requireNonNull(event.getCurrentItem()).getItemMeta()).getLocalizedName();
        FileConfiguration configFile = Status.getInstance().getConfig();

        //Reset Toggle
        event.getView().setItem(event.getSlot(), Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(configFile.getBoolean(localizedName)  ? Material.LIME_DYE : Material.RED_DYE, 1, 1, configFile.getBoolean(localizedName) ? "on" : "off", null, localizedName));

        //Update Stuff
        Status.getInstance().getFileManager().saveStatusFile();
        Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
        Status.getInstance().getConfigVarManager().updateVar();

        //Send Message
        p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7Du hast " + localizedName + " " + (configFile.getBoolean(localizedName) ? "an" : "aus") + "geschallten.");
    }
}
