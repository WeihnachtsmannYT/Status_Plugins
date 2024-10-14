package de.weihnachtsmannyt.afkapi.Commands;

import de.weihnachtsmannyt.afkapi.AfkApi;
import de.weihnachtsmannyt.afkapi.Managers.AfkManager;
import de.weihnachtsmannyt.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AfkCommand implements CommandExecutor {

    private final AfkManager afkManager;

    public AfkCommand(AfkManager afkManager) {
        this.afkManager = afkManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            System.out.println("You must be a player!");
            return false;
        }

        if (args.length > 1) {
            sendUsage(p);
            return false;
        }


        if (args.length == 1) {
            if (p.hasPermission("afk.admin")) {

                Player target = Bukkit.getPlayerExact(args[0]);

                if (afkManager.toggleAFKStatus(target)) {
                    target.sendMessage(AfkApi.getInstance().getPrefix() + "§7You set " + target.getDisplayName() + " that he is AFK.");
                    AfkManager.setPlayerAfk(target, true, true);
                    afkManager.announceToOthers(target, true);

                } else {
                    target.sendMessage(AfkApi.getInstance().getPrefix() + "§7You set " + target.getDisplayName() + " that he is no longer AFK.");
                    AfkManager.setPlayerAfk(target, false, true);
                    afkManager.announceToOthers(target, false);
                    Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
                }
            } else {
                p.sendMessage(AfkApi.getInstance().getPrefix() + ChatColor.RED + "Du darfst dies nicht!");
            }
        } else {
            if (afkManager.toggleAFKStatus(p)) {
                p.sendMessage(AfkApi.getInstance().getPrefix() + " §7You are now AFK.");
                AfkManager.setPlayerAfk(p, true, true);
                afkManager.announceToOthers(p, true);
            } else {
                p.sendMessage(AfkApi.getInstance().getPrefix() + " §7You are no longer AFK.");
                AfkManager.setPlayerAfk(p, false, true);
                afkManager.announceToOthers(p, false);
                Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
            }
        }
        return true;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(AfkApi.getInstance().getPrefix() + ChatColor.RED + "Ungültiger Befehl. Benutze /afk.");
    }
}