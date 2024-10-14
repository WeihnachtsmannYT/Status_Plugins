package de.weihnachtsmannyt.afkapi.Commands;

import de.weihnachtsmannyt.afkapi.AfkApi;
import de.weihnachtsmannyt.afkapi.Managers.AfkManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IsAfkCommand implements CommandExecutor {

    private final AfkManager afkManager;

    public IsAfkCommand(AfkManager afkManager) {
        this.afkManager = afkManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            System.out.println("You must be a player!");
            return false;
        }

        if (args.length == 0) {

            if (afkManager.isAFK(p)) {
                p.sendMessage(AfkApi.getInstance().getPrefix() + "§7 You are currently AFK.");
                p.sendMessage(AfkApi.getInstance().getPrefix() + "§7 since:" + AfkApi.getInstance().getAfkManager().getLastMovementTimeString(p));
            } else {
                p.sendMessage(AfkApi.getInstance().getPrefix() + "§7 You are not currently AFK.");
            }

        } else {
            Player target = Bukkit.getPlayerExact(args[0]);

            if (afkManager.isAFK(target)) {
                p.sendMessage(AfkApi.getInstance().getPrefix() + "§7 " + target.getDisplayName() + " is currently AFK.");
                p.sendMessage(AfkApi.getInstance().getPrefix() + "§7 since:" + AfkApi.getInstance().getAfkManager().getLastMovementTimeString(target));
            } else {
                p.sendMessage(AfkApi.getInstance().getPrefix() + "§7 " + target.getDisplayName() + " is not currently AFK.");
            }

        }
        return true;
    }
}