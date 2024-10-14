package de.weihnachtsmannyt.statusgui.Listener;

import de.weihnachtsmannyt.status.Status;
import de.weihnachtsmannyt.statusgui.GuiElements.BackItem;
import de.weihnachtsmannyt.statusgui.GuiElements.ScrollDownItem;
import de.weihnachtsmannyt.statusgui.GuiElements.ScrollUpItem;
import de.weihnachtsmannyt.statusgui.GuiElements.acceptSetItem;
import de.weihnachtsmannyt.statusgui.Managers.MethodsManager;
import de.weihnachtsmannyt.statusgui.Statusgui;
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
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.AnvilWindow;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InvListener implements Listener {

    String names = "Status Gui|Status Set|Status Get|Set your Color|Status A-Settings|Status P-Settings";

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!event.getView().getTitle().matches(names)) return;

        Player p = (Player) event.getWhoClicked();

        YamlConfiguration statusData = Status.getInstance().getFileManager().getStatusData();

        @NotNull ItemBuilder placeholder = new ItemBuilder(Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.BLACK_STAINED_GLASS_PANE, 0, 1, "§r", null, "placeholder"));
        @NotNull ItemBuilder placeholder_t = new ItemBuilder(Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.WHITE_STAINED_GLASS_PANE, 0, 1, "§r", null, "placeholder"));

        switch (Objects.requireNonNull(MethodsManager.getCustomLocalizedName(event.getCurrentItem()))) {
            case "close":
                p.closeInventory();
                event.setCancelled(true);
                break;
            case "playerSkull":
            case "§r":
            case "placeholder":
                event.setCancelled(true);
                break;
            case "zurück":
                p.closeInventory();
                p.performCommand("statusgui");
                event.setCancelled(true);
                break;
            case "set":
                if (p.hasPermission("status.set")) {
                    //TODO: AnvilGui

                    Item back = new SimpleItem(Statusgui.getInstance().getMethodsManager().CreateSkull("§7Zurück", "zurück",
                            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlODFmODA4Nzk3ZjVmNmYxODk2OTM0MTU0MzcyMzNiYjQ0YzQyZDM1YzczNzA4OTIyZTMwZGY4MmRjM2M5MiJ9fX0="));

                    Gui setInv = Gui.normal().setStructure(
                                    "x # y"
                            )
                            .addIngredient('x', back)
                            .addIngredient('y', new acceptSetItem())
                            .build();

                    setInv.setBackground(placeholder);

                    Window window = AnvilWindow.single()
                            .setViewer(p)
                            .setTitle("Status Set")
                            .setGui(setInv)
                            .build();

                    window.open();
                    event.setCancelled(true);
                }
                event.setCancelled(true);
                break;
            case "get":
                Item border = new SimpleItem(placeholder);
                Item trenner = new SimpleItem(placeholder_t);

                // an example list of items to display
                List<Item> items = new ArrayList<>();

                // Loop through all online players
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Item headItem = new SimpleItem(Statusgui.getInstance().getMethodsManager().getHead(onlinePlayer, "Dein Status",
                            ChatColor.WHITE +
                                    Objects.requireNonNull(onlinePlayer.getScoreboard().getTeam(Status.getInstance().getPrefixManager().getTeamByPlayer(onlinePlayer))).getPrefix() +
                                    onlinePlayer.getDisplayName() +
                                    Objects.requireNonNull(onlinePlayer.getScoreboard().getTeam(Status.getInstance().getPrefixManager().getTeamByPlayer(onlinePlayer))).getSuffix(),
                            "getSkull"));
                    items.add(headItem);
                }

                Gui getInv = ScrollGui.items()
                        .setStructure(
                                "x x x x x x x ; u",
                                "x x x x x x x ; #",
                                "x x x x x x x ; #",
                                "x x x x x x x ; b",
                                "x x x x x x x ; d")
                        .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                        .addIngredient('#', border)
                        .addIngredient(';', trenner)
                        .addIngredient('b', new BackItem())
                        .addIngredient('u', new ScrollUpItem())
                        .addIngredient('d', new ScrollDownItem())
                        .setContent(items)
                        .setBackground(placeholder)
                        .build();

                Window window = Window.single()
                        .setViewer(p)
                        .setTitle("Status Get")
                        .setGui(getInv)
                        .build();
                window.open();

                event.setCancelled(true);
                break;
            case "getSkull":
                //TODO getSkull
                event.setCancelled(true);
                break;

            //P-Settings
            case "pSettings":
                Inventory pSettingsInv = Bukkit.createInventory(null, 3 * 9, "Status P-Settings");

                //Update Variables
                Status.getInstance().getConfigVarManager().updateVar();

                pSettingsInv.setItem(18, Statusgui.getInstance().getMethodsManager().CreateSkull("§7Zurück", "zurück",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlODFmODA4Nzk3ZjVmNmYxODk2OTM0MTU0MzcyMzNiYjQ0YzQyZDM1YzczNzA4OTIyZTMwZGY4MmRjM2M5MiJ9fX0="));

                //Settings
                //pSettingsInv.setItem(2, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.PAPER, 1, 1, "Status_Prefix_on_off", null, ""));
                pSettingsInv.setItem(3, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.ANVIL, 1, 1, "Join_Leave_Message_on_off", null, ""));
                pSettingsInv.setItem(5, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.ANVIL, 1, 1, "DeathCounter_on_off", null, ""));
                pSettingsInv.setItem(6, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(Material.PAPER, 1, 1, "AutoAfk_on_off", null, ""));

                //Toggles
                //pSettingsInv.setItem(11, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(statusData.getBoolean(p.getUniqueId()+".p-settings"+".Status_Prefix_on_off") ? Material.LIME_DYE : Material.RED_DYE, 1, 1, statusData.getBoolean(p.getUniqueId()+".p-settings"+".Status_Prefix_on_off") ? "on" : "off", null, "Status_Prefix_on_off"));
                pSettingsInv.setItem(12, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(statusData.getBoolean(p.getUniqueId() + ".p-settings" + ".Join_Leave_Message_on_off") ? Material.LIME_DYE : Material.RED_DYE, 1, 1, statusData.getBoolean(p.getUniqueId() + ".p-settings" + ".Join_Leave_Message_on_off") ? "on" : "off", null, "Join_Leave_Message_on_off"));
                pSettingsInv.setItem(14, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(statusData.getBoolean(p.getUniqueId() + ".p-settings" + ".DeathCounter_on_off") ? Material.LIME_DYE : Material.RED_DYE, 1, 1, statusData.getBoolean(p.getUniqueId() + ".p-settings" + ".DeathCounter_on_off") ? "on" : "off", null, "DeathCounter_on_off"));
                pSettingsInv.setItem(15, Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(statusData.getBoolean(p.getUniqueId() + ".p-settings" + ".AutoAfk_on_off") ? Material.LIME_DYE : Material.RED_DYE, 1, 1, statusData.getBoolean(p.getUniqueId() + ".p-settings" + ".AutoAfk_on_off") ? "on" : "off", null, /*"AutoAfk_on_off"**/ "AutoAfk_on_off"));

                p.openInventory(pSettingsInv);
                event.setCancelled(true);
                break;

            case "Status_Prefix_on_off":
            case "Join_Leave_Message_on_off":
            case "DeathCounter_on_off":
            case "AutoAfk_on_off":

                String localizedName = MethodsManager.getCustomLocalizedName(event.getCurrentItem());

                assert localizedName != null;
                if (localizedName.equals("AutoAfk_on_off")) {
                    event.setCancelled(true);
                    return;
                } else {
                    statusData.set(p.getUniqueId() + ".p-settings" + "." + localizedName, !statusData.getBoolean(p.getUniqueId() + ".p-settings" + "." + localizedName));
                    Status.getInstance().getFileManager().reloadStatusFile();
                    event.getView().setItem(event.getSlot(), Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(statusData.getBoolean(p.getUniqueId() + ".p-settings" + "." + localizedName) ? Material.LIME_DYE : Material.RED_DYE, 1, 1, statusData.getBoolean(p.getUniqueId() + ".p-settings" + "." + localizedName) ? "on" : "off", null, localizedName));
                    Status.getInstance().getFileManager().saveStatusFile();

                    Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
                    p.sendMessage(Statusgui.getInstance().getPrefix() + "§7Du hast " + localizedName + " " + (statusData.getBoolean(p.getUniqueId() + ".p-settings" + "." + localizedName) ? "an" : "aus") + "geschallten.");
                    event.setCancelled(true);
                }
                break;

            //Settings
            case "aSettings":
                Inventory settingsInv = Bukkit.createInventory(null, 3 * 9, "Status A-Settings");

                //Update Variables
                Status.getInstance().getConfigVarManager().updateVar();

                settingsInv.setItem(18, Statusgui.getInstance().getMethodsManager().CreateSkull("§7Zurück", "zurück",
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlODFmODA4Nzk3ZjVmNmYxODk2OTM0MTU0MzcyMzNiYjQ0YzQyZDM1YzczNzA4OTIyZTMwZGY4MmRjM2M5MiJ9fX0="));

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
                event.setCancelled(true);
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
                event.setCancelled(true);
                break;

            case "Status-Prefix-on/off":
                Status.getInstance().getConfigVarManager().setStatus_Prefix_on_off(!Status.getInstance().getConfigVarManager().getStatus_Prefix_on_off());
                toggleUpdater(event, p);
                event.setCancelled(true);
                break;
            case "Join/Leave-Message-on/off":
                Status.getInstance().getConfigVarManager().setJoin_Leave_Message_on_off(!Status.getInstance().getConfigVarManager().getJoin_Leave_Message_on_off());
                toggleUpdater(event, p);
                event.setCancelled(true);
                break;
            case "DeathCounter-on/off":
                Status.getInstance().getConfigVarManager().setDeathCounter_on_off(!Status.getInstance().getConfigVarManager().getDeathCounter_on_off());
                toggleUpdater(event, p);
                event.setCancelled(true);
                break;
            case "BlockedWords-Toggle":
                Status.getInstance().getConfigVarManager().setBlockedWords_Toggle(!Status.getInstance().getConfigVarManager().getBlockedWords_Toggle());
                toggleUpdater(event, p);
                event.setCancelled(true);
                break;
            case "Uppercase/LengthLimit-Toggle":
                Status.getInstance().getConfigVarManager().setUppercase_LengthLimit_Toggle(!Status.getInstance().getConfigVarManager().getUppercase_LengthLimit_Toggle());
                toggleUpdater(event, p);
                event.setCancelled(true);
                break;
            default:
                event.setCancelled(true);
                System.out.println("Unexpected value: " + MethodsManager.getCustomLocalizedName(event.getCurrentItem()));
                break;
        }
        Status.getInstance().getFileManager().saveStatusFile();
    }

    private void toggleUpdater(InventoryClickEvent event, Player p) {
        //RELOAD everything
        Status.getInstance().getFileManager().reloadStatusFile();
        Status.getInstance().getConfigVarManager().updateVar();

        //Define Variables
        String localizedName = MethodsManager.getCustomLocalizedName(Objects.requireNonNull(event.getCurrentItem()));
        FileConfiguration configFile = Status.getInstance().getConfig();

        //Reset Toggle
        assert localizedName != null;
        event.getView().setItem(event.getSlot(), Statusgui.getInstance().getMethodsManager().CreateItemWithMaterial(configFile.getBoolean(localizedName) ? Material.LIME_DYE : Material.RED_DYE, 1, 1, configFile.getBoolean(localizedName) ? "on" : "off", null, localizedName));

        //Update Stuff
        Status.getInstance().getFileManager().saveStatusFile();
        Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
        Status.getInstance().getConfigVarManager().updateVar();

        //Send Message
        p.sendMessage(Statusgui.getInstance().getPrefix() + "§7Du hast " + localizedName + " " + (configFile.getBoolean(localizedName) ? "an" : "aus") + "geschallten.");
    }
}
