package de.weihnachtsmannyt.status.Manager;

import de.weihnachtsmannyt.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

public class PrefixManager {

    static String team = "001Spieler";
    private static Scoreboard scoreboard;

    public void setScoreboard() {
        scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();

        scoreboard.registerNewTeam(team);

        Objects.requireNonNull(scoreboard.getTeam(team)).setPrefix("§f[Spieler] §f");
    }

    public void resetAfkAll(){
        for (Player target : Bukkit.getOnlinePlayers()) {
            Status.getInstance().getFileManager().saveStatusFile();
            Status.getInstance().getFileManager().getStatusData().set(target.getUniqueId()+".Afk", false);
            Status.getInstance().getFileManager().saveStatusFile();
            Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
        }
    }

    public void updatePrefix(Player player) {
        YamlConfiguration statusData = Status.getInstance().getFileManager().getStatusData();
        String playerTeam = "001" + player;

        if (player.isOnline()) {
            try {
                scoreboard.registerNewTeam(playerTeam);
            } catch (Exception e){
                System.out.println(Status.getInstance().getConfigVarManager().getStatus_Prefix()+"Register new Team error!");
            }

            if (Status.getInstance().getConfigVarManager().getDeathCounter_on_off() && statusData.getBoolean(player.getUniqueId()+".p-settings"+".DeathCounter_on_off")) {
                if (Objects.equals(statusData.getString(player.getUniqueId() + ".status"), "Default")) {
                    if (player.getStatistic(Statistic.DEATHS) > 0 && statusData.getBoolean(player.getUniqueId()+".p-settings"+".DeathCounter_on_off")) {
                        Objects.requireNonNull(scoreboard.getTeam(playerTeam)).setPrefix("§f[" + player.getStatistic(Statistic.DEATHS) + "§f]" + " §f[" + "Spieler" + "§f] §f");
                    } else {
                        Objects.requireNonNull(scoreboard.getTeam(playerTeam)).setPrefix("§f[" + "Spieler" + "§f] §f");
                    }
                } else {
                    if (player.getStatistic(Statistic.DEATHS) > 0 && statusData.getBoolean(player.getUniqueId()+".p-settings"+".DeathCounter_on_off")) {
                        Objects.requireNonNull(scoreboard.getTeam(playerTeam)).setPrefix("§f[" + player.getStatistic(Statistic.DEATHS) + "§f]"
                                + " §f[" + statusData.getString(player.getUniqueId() + ".color")
                                + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(statusData.getString(player.getUniqueId() + ".status"))) + "§f] §f");
                    } else {
                        Objects.requireNonNull(scoreboard.getTeam(playerTeam)).setPrefix("§f[" + statusData.getString(player.getUniqueId() + ".color")
                                + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(statusData.getString(player.getUniqueId() + ".status"))) + "§f] §f");
                    }
                }
            } else {
                if (Objects.equals(statusData.getString(player.getUniqueId() + ".status"), "Default")) {
                    Objects.requireNonNull(scoreboard.getTeam(playerTeam)).setPrefix("§f["+ "Spieler" +"§f] §f");
                } else {
                    Objects.requireNonNull(scoreboard.getTeam(playerTeam)).setPrefix("§f["+ statusData.getString(player.getUniqueId() + ".color")
                            + ChatColor.translateAlternateColorCodes('&', (statusData.getString(player.getUniqueId() + ".status")) + "§f] §f"));
                }
            }

            if (statusData.getBoolean(player.getUniqueId() + ".Afk")){
                Objects.requireNonNull(scoreboard.getTeam(playerTeam)).setSuffix("§r §c["+ "AFK" +"]§r");
            }else{
                Objects.requireNonNull(scoreboard.getTeam(playerTeam)).setSuffix("");
            }

            Objects.requireNonNull(scoreboard.getTeam(playerTeam)).addEntry(player.getDisplayName());
            Status.getInstance().getFileManager().saveStatusFile();
            player.setScoreboard(scoreboard);
        }
    }

    public String getTeamByPlayer(Player player) {
        return "001" + player;
    }

    public void updatePrefixAllPlayers() {
        YamlConfiguration statusData = Status.getInstance().getFileManager().getStatusData();

        Status.getInstance().getPrefixManager().setScoreboard();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (Objects.equals(statusData.getString(target.getUniqueId() + ".status"), "Default")) {
                Objects.requireNonNull(Status.getInstance().getPrefixManager().getScoreboard().getTeam(team)).addEntry(target.getDisplayName());
            } else {
                Status.getInstance().getFileManager().saveStatusFile();
                Status.getInstance().getPrefixManager().updatePrefix(target);
            }
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.setScoreboard(Status.getInstance().getPrefixManager().getScoreboard());
        }
    }

    public Boolean isColorAColor(String Color) {
        return switch (Color.toLowerCase()) {
            case "§" + "0",
                    "§" + "1",
                    "§" + "2",
                    "§" + "3",
                    "§" + "4",
                    "§" + "5",
                    "§" + "6",
                    "§" + "7",
                    "§" + "8",
                    "§" + "9",
                    "§" + "a",
                    "§" + "b",
                    "§" + "c",
                    "§" + "d",
                    "§" + "e",
                    "§" + "f",
                    "black",
                    "dark_blue",
                    "dark_green",
                    "dark_aqua",
                    "dark_red",
                    "dark_purple",
                    "gold",
                    "gray",
                    "dark_gray",
                    "blue",
                    "green",
                    "aqua",
                    "red",
                    "light_purple",
                    "yellow",
                    "white" -> true;
            default -> false;
        };
    }

    public String getColorFromRaw(String raw) {
        return switch (raw.toLowerCase()) {
            case "§0" -> "Black";
            case "§1" -> "Dark Blue";
            case "§2" -> "Dark Green";
            case "§3" -> "Dark Aqua";
            case "§4" -> "Dark Red";
            case "§5" -> "Dark Purple";
            case "§6" -> "Gold";
            case "§7" -> "Gray";
            case "§8" -> "Dark Gray";
            case "§9" -> "Blue";
            case "§a" -> "Green";
            case "§b" -> "Aqua";
            case "§c" -> "Red";
            case "§d" -> "Light Purple";
            case "§e" -> "Yellow";
            case "§f" -> "White";
            default -> "default";
        };
    }

    public String getRawFromColor(String Color) {
        return switch (Color.toLowerCase()) {
            case "black" -> "§0";
            case "dark_blue" -> "§1";
            case "dark_green" -> "§2";
            case "dark_aqua" -> "§3";
            case "dark_red" -> "§4";
            case "dark_purple" -> "§5";
            case "gold" -> "§6";
            case "gray" -> "§7";
            case "dark_gray" -> "§8";
            case "blue" -> "§9";
            case "green" -> "§a";
            case "aqua" -> "§b";
            case "red" -> "§c";
            case "light_purple" -> "§d";
            case "yellow" -> "§e";
            case "white" -> "§f";
            default -> Color;
        };
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public String getTeam() {
        return team;
    }
}
