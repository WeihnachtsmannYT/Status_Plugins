package de.weihnachtsmannyt.afkapi;

import de.weihnachtsmannyt.afkapi.Commands.AfkCommand;
import de.weihnachtsmannyt.afkapi.Commands.IsAfkCommand;
import de.weihnachtsmannyt.afkapi.Managers.AfkManager;
import de.weihnachtsmannyt.afkapi.Tasks.MovementChecker;
import de.weihnachtsmannyt.afkapi.listeners.AfkListener;
import de.weihnachtsmannyt.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AfkApi extends JavaPlugin {

    private static AfkApi instance;

    //Api
    private static Status statusApi;

    private AfkManager afkManager;

    public static AfkApi getInstance() {
        return instance;
    }

    private final String prefix = "§7[§9Afk§7]§r §7->§r";

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        //Api
        statusApi = Status.getInstance();

        AfkApi.getInstance().getStatusApi().getPrefixManager().resetAfkAll();

        this.afkManager = new AfkManager();

        getCommand("isafk").setExecutor(new IsAfkCommand(this.afkManager));
        getCommand("afk").setExecutor(new AfkCommand(this.afkManager));

        getServer().getPluginManager().registerEvents(new AfkListener(this.afkManager), this);

        Bukkit.getScheduler().runTaskTimer(this, new MovementChecker(this.afkManager), 0L, 600L);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        AfkApi.getInstance().getStatusApi().getPrefixManager().resetAfkAll();
    }

    public Status getStatusApi() {
        statusApi = Status.getInstance();
        return statusApi;
    }

    public String getPrefix() {
        return prefix;
    }
}
