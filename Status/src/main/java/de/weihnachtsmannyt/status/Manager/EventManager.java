package de.weihnachtsmannyt.status.Manager;

import de.weihnachtsmannyt.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.Objects;

import static de.weihnachtsmannyt.status.Manager.PrefixManager.team;

public class EventManager implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        YamlConfiguration statusData = Status.getInstance().getFileManager().getStatusData();
        Player p = event.getPlayer();
        String joinMessage = "";
        event.setJoinMessage("");

        if (!FileManager.playerIsRegistered(p)) {
            Status.getInstance().getFileManager().savePlayerInStatus(p, "Default", "§f");
            Objects.requireNonNull(Status.getInstance().getPrefixManager().getDefaultScoreboard().getTeam(team)).addEntry(p.getDisplayName());
            Objects.requireNonNull(Status.getInstance().getPrefixManager().getDeathsScoreboard().getTeam(team)).addEntry(p.getDisplayName());
            Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
        }

        if (Status.getInstance().getConfigVarManager().getJoin_Leave_Message_on_off()) {
            if (Objects.equals(statusData.getString(p.getUniqueId() + ".status"), "Default")) {
                joinMessage=(Status.getInstance().getConfigVarManager().getJoinMessage() + " §f[" + statusData.getString(p.getUniqueId() + ".color") + "Spieler" + "§f] "
                        + statusData.getString(p.getUniqueId() + ".player"));
            } else {
                joinMessage=(Status.getInstance().getConfigVarManager().getJoinMessage() + " §f[" + statusData.getString(p.getUniqueId() + ".color")
                        + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(statusData.getString(p.getUniqueId() + ".status"))) + "§f] "
                        + statusData.getString(p.getUniqueId() + ".player"));
            }
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (statusData.getBoolean(target.getUniqueId()+".p-settings"+".Join_Leave_Message_on_off"))
                target.sendMessage(joinMessage);
        }

        statusData.set(p.getUniqueId()+".Afk",false);

        Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        YamlConfiguration statusData = Status.getInstance().getFileManager().getStatusData();

        Player p = e.getPlayer();
        final String m = e.getMessage().trim();
        final String message = ChatColor.translateAlternateColorCodes('&', e.getMessage());
        float uppercaseLetter = 0;
        for (int i = 0; i < m.length(); i++) {
            if (Character.isUpperCase(m.charAt(i)) && Character.isLetter(m.charAt(i))) {
                uppercaseLetter++;
            }
        }
        if (FileManager.StringIsBlocked(e.getMessage())) {
            e.setCancelled(true);
            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7Diese Nachricht enthält §9blockierte §7Wörter!");
        } else if (Status.getInstance().getConfigVarManager().getUppercase_LengthLimit_Toggle() && (uppercaseLetter / (float) m.length() > 0.3 && m.length() > Status.getInstance().getConfigVarManager().getPrefix_LengthLimit())) {
            e.setCancelled(true);
            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§9Bitte benutze nicht so viele Großbuchstaben!");
        } else {
            e.setFormat(Objects.requireNonNull(p.getScoreboard().getTeam(Status.getInstance().getPrefixManager().getTeamByPlayer(p))).getPrefix() + p.getDisplayName() + "§f: §r" + message);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        YamlConfiguration statusData = Status.getInstance().getFileManager().getStatusData();
        Player p = event.getPlayer();
        String leaveMessage = "";
        event.setQuitMessage("");

        if (Status.getInstance().getConfigVarManager().getJoin_Leave_Message_on_off()) {
            if (Objects.equals(statusData.getString(p.getUniqueId() + ".status"), "Default")) {
                leaveMessage=(Status.getInstance().getConfigVarManager().getLeaveMassage() + " §f[" + statusData.getString(p.getUniqueId() + ".color") + "Spieler" + "§f] " + statusData.getString(p.getUniqueId() + ".player"));
            } else {
                leaveMessage=(Status.getInstance().getConfigVarManager().getLeaveMassage() + " §f[" + statusData.getString(p.getUniqueId() + ".color") + ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(statusData.getString(p.getUniqueId() + ".status"))) + "§f] " + statusData.getString(p.getUniqueId() + ".player"));
            }
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (statusData.getBoolean(target.getUniqueId()+".p-settings"+".Join_Leave_Message_on_off"))
                target.sendMessage(leaveMessage);
        }

        statusData.set(p.getUniqueId()+".Afk",false);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        updateAfterDeath(event);
    }

    @EventHandler
    public void entityDeath(EntityDeathEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof Player) {
            updateAfterDeath(event);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        updateAfterDeath(event);
    }

    @EventHandler
    public void onPlayerSendCmd(PlayerCommandSendEvent event) {
        updateAfterDeath(event);
    }

    private void updateAfterDeath(Event event) {
        //TODO wrong update after death
        Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
    }
}
