package de.weihnachtsmannyt.afkapi.Commands;

import de.weihnachtsmannyt.afkapi.Managers.AfkManager;
import de.weihnachtsmannyt.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IsAfkCommand implements CommandExecutor {

    private final AfkManager afkManager;

    public IsAfkCommand(AfkManager afkManager){
        this.afkManager = afkManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            System.out.println("You must be a player!") ;
            return false;
        }

        if(args.length == 0){

            if(afkManager.isAFK(p)){
                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7You are currently AFK.");
            }else{
                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7You are not currently AFK.");
            }

        }else{
            Player target = Bukkit.getPlayerExact(args[0]);

            if(afkManager.isAFK(target)){
                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7"+target.getDisplayName() + " is currently AFK.");
            }else{
                p.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§7"+target.getDisplayName() + " is not currently AFK.");
            }

        }
        return true;
    }
}