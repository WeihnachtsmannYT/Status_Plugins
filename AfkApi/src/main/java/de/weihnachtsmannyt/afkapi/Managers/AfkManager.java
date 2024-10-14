package de.weihnachtsmannyt.afkapi.Managers;

import de.weihnachtsmannyt.afkapi.AfkApi;
import de.weihnachtsmannyt.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AfkManager {

    //see if they have moved since 1 * 5 minute
    //60000
    private final long MOVEMENT_THRESHOLD = 60000L * 5;

    public final HashMap<Player, Long> lastMovement = new HashMap<>();
    public final HashMap<Player, Boolean> previousData = new HashMap<>();

    public static void setPlayerAfk(Player target, Boolean Afk, Boolean forced) {
        AfkApi.getInstance().getStatusApi().getFileManager().saveStatusFile();
        if (forced) {
            AfkApi.getInstance().getStatusApi().getFileManager().getStatusData().set(target.getUniqueId() + ".Afk", Afk);
        } else {
            if (Status.getInstance().getFileManager().getStatusData().getBoolean(target.getUniqueId() + ".p-settings" + ".AutoAfk_on_off")) {
                AfkApi.getInstance().getStatusApi().getFileManager().getStatusData().set(target.getUniqueId() + ".Afk", Afk);
            }
        }
        AfkApi.getInstance().getStatusApi().getFileManager().saveStatusFile();
        AfkApi.getInstance().getStatusApi().getPrefixManager().updatePrefixAllPlayers();
    }

    public void playerJoined(Player player) {
        lastMovement.put(player, System.currentTimeMillis());
    }

    public void playerLeft(Player player) {
        lastMovement.remove(player);
    }

    public void playerMoved(Player player) {

        lastMovement.put(player, System.currentTimeMillis());

        checkPlayerAFKStatus(player);

    }

    public boolean isAFK(Player player) {

        if (lastMovement.containsKey(player)) {
            if (lastMovement.get(player) == -1L) {
                return true;
            } else {
                long timeElapsed = System.currentTimeMillis() - lastMovement.get(player);


                return timeElapsed >= MOVEMENT_THRESHOLD;
            }
        } else {
            lastMovement.put(player, System.currentTimeMillis());
        }

        return false;
    }

    public long getlastMovementTime(Player player) {
        return System.currentTimeMillis() - lastMovement.get(player);
    }

    public void checkAllPlayersAFKStatus() {

        for (Map.Entry<Player, Long> entry : lastMovement.entrySet()) {
            checkPlayerAFKStatus(entry.getKey());
        }
    }

    public boolean toggleAFKStatus(Player player) {

        if (isAFK(player)) {
            previousData.put(player, false);
            AfkManager.setPlayerAfk(player, false, false);
            lastMovement.put(player, System.currentTimeMillis());
            return false;
        } else {
            previousData.put(player, true);
            AfkManager.setPlayerAfk(player, true, false);
            lastMovement.put(player, -1L);
            return true;
        }

    }

    public void announceToOthers(Player target, boolean isAFK) {
        if (!Status.getInstance().getFileManager().getStatusData().getBoolean(target.getUniqueId() + ".p-settings" + ".AutoAfk_on_off"))
            Bukkit.getServer().getOnlinePlayers()
                    .forEach(players -> {
                        if (!players.equals(target)) {
                            if (isAFK) {
                                players.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7" + target.getDisplayName() + " is now AFK.");
                            } else {
                                players.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7" + target.getDisplayName() + " is no longer AFK.");
                            }
                        }
                    });
    }

    public void checkPlayerAFKStatus(Player player) {
        if (lastMovement.containsKey(player)) {

            boolean nowAFK = isAFK(player);

            if (previousData.containsKey(player)) {

                boolean wasAFK = previousData.get(player);

                if (wasAFK && !nowAFK) {
                    player.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7You are no longer AFK.");

                    AfkManager.setPlayerAfk(player, false, false);
                    previousData.put(player, false);

                    announceToOthers(player, false);

                } else if (!wasAFK && nowAFK) {
                    player.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7You are now AFK.");

                    AfkManager.setPlayerAfk(player, true, false);
                    previousData.put(player, true);

                    announceToOthers(player, true);

                }

            } else {
                previousData.put(player, nowAFK);
            }
        }
    }
}