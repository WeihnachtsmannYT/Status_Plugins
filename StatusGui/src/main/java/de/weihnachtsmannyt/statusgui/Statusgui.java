package de.weihnachtsmannyt.statusgui;

import de.weihnachtsmannyt.status.Status;
import de.weihnachtsmannyt.statusgui.Commands.guicommand;
import de.weihnachtsmannyt.statusgui.Managers.InvListener;
import de.weihnachtsmannyt.statusgui.Managers.MethodsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Statusgui extends JavaPlugin {

    private static Statusgui instance;
    private static Status statusApi;

    private MethodsManager methodsManager;
    public static Statusgui getInstance() {
        return instance;
    }

    private final String prefix = "§7[§9Status-Gui§7]§r §7->§r";


    @Override
    public void onEnable() {
        // Plugin startup logic

        //APi Stuff
        instance = this;
        this.methodsManager = new MethodsManager();

        statusApi = Status.getInstance();

        Bukkit.getPluginManager().registerEvents(new InvListener(), this);
        getCommand("statusgui").setExecutor(new guicommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MethodsManager getMethodsManager() {
        return methodsManager;
    }

    public Status getStatusApi() {
        return statusApi;
    }

    public String getPrefix() {
        return prefix;
    }
}
