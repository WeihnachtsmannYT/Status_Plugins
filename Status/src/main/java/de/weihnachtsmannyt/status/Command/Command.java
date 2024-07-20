package de.weihnachtsmannyt.status.Command;

import de.weihnachtsmannyt.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

import static de.weihnachtsmannyt.status.Manager.FileManager.StringIsBlocked;
import static de.weihnachtsmannyt.status.Manager.FileManager.playerIsRegistered;


public class Command implements CommandExecutor {

    YamlConfiguration statusData = Status.getInstance().getFileManager().getStatusData();

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            System.out.println("You must be a player!") ;
            return false;
        }

        if (args.length == 0) {
            sendUsage(p);
            return true;
        }

        switch (args[0].toLowerCase()) { //status ___
            case "help" -> sendUsage(p);
            case "update" -> {
                if (p.hasPermission("status.update")) {
                    Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
                    p.sendMessage("Status has been updated");
                    if (args.length==2) {
                        switch (args[1].toLowerCase()) {
                            case "debug" -> {
                                Status.getInstance().getFileManager().saveStatusFile();
                                p.sendMessage("-player:" + statusData.get(p.getUniqueId() + ".player"));
                                p.sendMessage("-deaths:" + p.getStatistic(Statistic.DEATHS));
                                p.sendMessage("-status:" + statusData.get(p.getUniqueId() + ".status"));
                                p.sendMessage("-color:" + statusData.get(p.getUniqueId() + ".color"));
                                p.sendMessage("-Afk:" + statusData.get(p.getUniqueId() + ".Afk"));
                                p.sendMessage("-Status_Prefix_on_off:" + statusData.get(p.getUniqueId() + ".p-settings" + ".Status_Prefix_on_off"));
                                p.sendMessage("-Join_Leave_Message_on_off:" + statusData.get(p.getUniqueId() + ".p-settings" + ".Join_Leave_Message_on_off"));
                                p.sendMessage("-DeathCounter_on_off:" + statusData.get(p.getUniqueId() + ".p-settings" + ".DeathCounter_on_off"));
                                p.sendMessage("-AutoAfk_on_off:" + statusData.get(p.getUniqueId() + ".p-settings" + ".AutoAfk_on_off"));
                            }
                            case "deaths-debugger" -> {
                                Status.getInstance().getFileManager().saveStatusFile();
                                p.sendMessage("-----------------------------------------------------");
                                for (Player target : Bukkit.getOnlinePlayers()) {
                                    p.sendMessage("-player:                     " + statusData.get(target.getUniqueId() + ".player"));
                                    p.sendMessage("-DeathCounter_on_off:        " + statusData.get(target.getUniqueId() + ".p-settings" + ".DeathCounter_on_off"));
                                    p.sendMessage("-Tablist-Prefix:             " + Objects.requireNonNull(target.getScoreboard().getTeam(Status.getInstance().getPrefixManager().getTeamByPlayer(target))).getPrefix());
                                    p.sendMessage("-----------------------------------------------------");
                                }
                            }
                            default -> {
                                sendFailedCmd(p);
                            }
                        }
                    }
                }else sendUsage(p);
            }
            case "reset" -> {
                switch (args.length) {
                    case 1 -> {
                        if (!Objects.requireNonNull(statusData.getString(p.getUniqueId() + ".status")).equals("Default")) {
                            Status.getInstance().getFileManager().savePlayerInStatus(p, "Default", "§f");
                            Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
                            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                    "§7Dein Status wurde §9zurück §7gesetzt!");
                        } else {
                            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                    "§7Dein Status ist schon auf §9Default§7!");
                        }
                    }
                    case 2 -> {
                        if (p.hasPermission("status.admin")) {
                            Player target = Bukkit.getPlayerExact(args[1]);
                            assert target != null;
                            if (playerIsRegistered(target)) {
                                Status.getInstance().getFileManager().savePlayerInStatus(target, "Default", "§f");
                                Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
                                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix()
                                        + "§7Der Status von§9 "
                                        + target.getName()
                                        + "§7 wurde §9zurück §7gesetzt!");
                            } else {
                                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                        "§9Dieser Spieler wurde noch nicht registriert!");
                            }
                        } else {
                            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                    "§9Du darfst dies nicht!");
                        }
                    }
                    default -> sendUsage(p);
                }
            }
            case "set" -> {
                Boolean UppercaseLengthLimitToggle = Status.getInstance().getConfigVarManager().getUppercase_LengthLimit_Toggle();
                switch (args.length) {
                    case 2 -> {
                        if (UppercaseLengthLimitToggle) {
                            String translated = ChatColor.stripColor(args[1]);
                            int length = translated.length();
                            if (length > Status.getInstance().getConfigVarManager().getPrefix_LengthLimit()) {
                                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                        "§7Dieser Status ist §9zu lang§7 (max " + Status.getInstance().getConfigVarManager().getPrefix_LengthLimit() + ") Zeichen!");
                                return true;
                            }
                        }
                        String status = args[1];
                        Status.getInstance().getFileManager().savePlayerInStatus(p, status, "§f");
                        Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
                        p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                "§7Dein Status wurde auf §f" +
                                ChatColor.translateAlternateColorCodes('&', status) +
                                "§7 gesetzt!");
                    }
                    case 3 -> {
                        if (UppercaseLengthLimitToggle) {
                            String translated = ChatColor.stripColor(args[1]);
                            int length = translated.length();
                            if (length > Status.getInstance().getConfigVarManager().getPrefix_LengthLimit()) {
                                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                        "§7Dieser Status ist §9zu lang§7 (max " + Status.getInstance().getConfigVarManager().getPrefix_LengthLimit() + ") Zeichen!");
                                return true;
                            }
                        }
                        String status = args[1];
                        String arg2 = args[2].replace('&', '§');
                        String color = Status.getInstance().getPrefixManager().getRawFromColor(arg2);
                        if (Status.getInstance().getPrefixManager().isColorAColor(color)) {
                            Status.getInstance().getFileManager().savePlayerInStatus(p, status, color);
                            Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
                            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                    "§7Dein Status wurde auf " +
                                    color +
                                    ChatColor.translateAlternateColorCodes('&', status) +
                                    "§7 mit der Farbe " +
                                    color +
                                    Status.getInstance().getPrefixManager().getColorFromRaw(color) +
                                    "§7 gesetzt!");
                        } else
                            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                    "§7Diese Farbe ist §9ungültig§7 und dein Status hat sich nicht geändert!");
                    }
                    case 4 -> {
                        if (p.hasPermission("status.admin")) {
                            String status = args[1];
                            String arg2 = args[2].replace('&', '§');
                            String color = Status.getInstance().getPrefixManager().getRawFromColor(arg2);
                            if (!StringIsBlocked(args[1])) {
                                if (UppercaseLengthLimitToggle) {
                                    String translated = ChatColor.stripColor(args[1]);
                                    int length = translated.length();
                                    if (length > Status.getInstance().getConfigVarManager().getPrefix_LengthLimit()) {
                                        p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                                "§7Dieser Status ist §9zu lang§7 (max " + Status.getInstance().getConfigVarManager().getPrefix_LengthLimit() + ") Zeichen!");
                                        return true;
                                    }
                                }
                                Player target = Bukkit.getPlayerExact(args[3]);
                                if (Status.getInstance().getPrefixManager().isColorAColor(color)) {
                                    Status.getInstance().getFileManager().savePlayerInStatus(target, status, color);
                                    Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
                                    for (Player all : Bukkit.getOnlinePlayers()) {
                                        all.setScoreboard(Status.getInstance().getPrefixManager().getScoreboard(all));
                                    }
                                    assert target != null;
                                    p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix()
                                            + "§7Der Status von§9 "
                                            + target.getName()
                                            + "§7 wurde auf "
                                            + color
                                            + ChatColor.translateAlternateColorCodes('&', status)
                                            + "§7 mit der Farbe "
                                            + color
                                            + Status.getInstance().getPrefixManager().getColorFromRaw(color)
                                            + "§7 gesetzt!"
                                    );
                                    target.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix()
                                            + "§7 Der Spieler §9"
                                            + p.getName()
                                            + "§7 hat den Status von dir auf "
                                            + color
                                            + ChatColor.translateAlternateColorCodes('&', status)
                                            + "§7 mit der Farbe "
                                            + color
                                            + Status.getInstance().getPrefixManager().getColorFromRaw(color)
                                            + "§7 gesetzt!"
                                    );
                                } else {
                                    assert target != null;
                                    p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                            "§7Diese Farbe ist §9ungültig§7 und der Status von " +
                                            target.getName() + " hat sich nicht geändert!");
                                }
                            } else
                                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                        "§9 Dieser Spieler wurde noch nicht registriert!");
                        } else p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                "§9 Du darfst dies nicht!");
                    }

                    default -> sendUsage(p);
                }
            }
            case "get" -> {
                switch (args.length) {
                    case 1 -> {
                        String status = String.valueOf(statusData.get(p.getUniqueId() + ".status"));
                        String color = String.valueOf(statusData.get(p.getUniqueId() + ".color"));

                        if (playerIsRegistered(p)) {
                            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                    "§7Dein Status ist " +
                                    color +
                                    ChatColor.translateAlternateColorCodes('&', status) +
                                    "§7 mit der Farbe " +
                                    color +
                                    Status.getInstance().getPrefixManager().getColorFromRaw(color));
                        }
                    }
                    case 2 -> {
                        String t1 = args[1];
                        OfflinePlayer target = Bukkit.getPlayerExact(t1);
                        assert target != null;
                        if (target.hasPlayedBefore()) {
                            String status = String.valueOf(statusData.get(target.getUniqueId() + ".status"));
                            String color = String.valueOf(statusData.get(target.getUniqueId() + ".color"));

                            if (Status.getInstance().getPrefixManager().isColorAColor(color)) {
                                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() +
                                        "§7Der Status von §9" +
                                        t1 +
                                        "§7 ist " +
                                        color +
                                        ChatColor.translateAlternateColorCodes('&', status) +
                                        "§7 mit der Farbe " +
                                        color +
                                        Status.getInstance().getPrefixManager().getColorFromRaw(color));
                            } else {
                                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7Der Spieler wurde nicht gefunden!");
                            }
                        } else {
                            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7Der Spieler wurde nicht gefunden!");
                        }
                    }
                    default -> sendUsage(p);
                }
            }
        }
        return true;
    }

    private void sendUsage(CommandSender sender) {
        if (sender.hasPermission("status.admin")) {
            sender.sendMessage("        §c§k!!!!§r§9This Plugin was coded by §cWeihnachtsmannYT§c§k!!!!");
            sender.sendMessage("    §7Verwendung§8: §9/status <operator> <status> <color>          ");
            sender.sendMessage("    §7Verwendung§8: §9/status <operator> <status> <spieler>        ");
            sender.sendMessage("       §7<operator>§8: §7< §9\" help \"§7/§9\" set \"§7/§9\" get \"§7/§9\" reset \"§7/§9\" config \" §9\" update \" §7>  ");
        } else {
            sender.sendMessage("§a|" + "        §c§k!!!!§r§9This Plugin was coded by §cWeihnachtsmannYT§c§k!!!!");
            sender.sendMessage("§a|" + "    §7Verwendung§8: §9/status <operator> <status> <color>");
            sender.sendMessage("§a|" + "       §7<operator>§8: §7< §9\" help \"§7/§9\" set \"§7/§9\" get \"§7/§9\" reset \"§7>");
        }
    }

    private void sendUsage_Config(CommandSender sender) {
        sender.sendMessage("§a|§a" + "---------------------------------------------------" + "§a|§r");
        sender.sendMessage("§a|" + " §c§k!!!!§r§9This Plugin was coded by §cWeihnachtsmannYT§c§k!!!!  " + "§a|§r");
        sender.sendMessage("§a|" + "    §7Verwendung§8: §9/config <operator>   " + "§a|§r");
        sender.sendMessage("§a|" + "    §7<operator>§8: §7< §9\" Coming soon \"§7>  " + "§a|§r");
        sender.sendMessage("§a|§a" + "---------------------------------------------------" + "§a|§r");
    }

    private void sendFailedCmd(CommandSender sender) {
        sender.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§c§l Failed, Wrong Cmd or Token!");
    }
}