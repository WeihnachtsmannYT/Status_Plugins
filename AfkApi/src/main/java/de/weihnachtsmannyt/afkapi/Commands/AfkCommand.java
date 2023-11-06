package de.weihnachtsmannyt.afkapi.Commands;

import de.weihnachtsmannyt.afkapi.Managers.AfkManager;
import de.weihnachtsmannyt.status.Status;
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
            System.out.println("You must be a player!") ;
            return false;
        }
        if (afkManager.toggleAFKStatus(p)) {
            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7You are now AFK.");
            AfkManager.setPlayerAfk(p, true);
            afkManager.announceToOthers(p, true);

        } else {
            p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7You are no longer AFK.");
            AfkManager.setPlayerAfk(p, false);
            afkManager.announceToOthers(p, false);
        }
        return true;
    }
}