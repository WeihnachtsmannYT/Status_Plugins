package de.weihnachtsmannyt.afkapi.listeners;

import de.weihnachtsmannyt.afkapi.Managers.AfkManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AfkListener implements Listener {

    private final AfkManager afkManager;

    public AfkListener(AfkManager afkManager) {
        this.afkManager = afkManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        afkManager.playerJoined(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        afkManager.playerLeft(e.getPlayer());
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent e) {
        if (e.getTo() != e.getFrom()) {
            afkManager.playerMoved(e.getPlayer());
        }
    }

}