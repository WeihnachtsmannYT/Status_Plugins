package de.weihnachtsmannyt.statusgui;

import de.weihnachtsmannyt.status.Status;
import de.weihnachtsmannyt.statusgui.Commands.GuiCommand;
import de.weihnachtsmannyt.statusgui.Listener.InvListener;
import de.weihnachtsmannyt.statusgui.Managers.HeadManager;
import de.weihnachtsmannyt.statusgui.Managers.MethodsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Statusgui extends JavaPlugin {

    private static Statusgui instance;
    private static Status statusApi;

    private MethodsManager methodsManager;
    private HeadManager headManager;

    public static Statusgui getInstance() {
        return instance;
    }

    private final String prefix = "§7[§9Status-Gui§7]§r §7->§r ";


    @Override
    public void onEnable() {
        // Plugin startup logic

        //APi Stuff
        instance = this;

        //Internal Api
        this.methodsManager = new MethodsManager();
        this.headManager = new HeadManager();

        statusApi = Status.getInstance();

        Bukkit.getPluginManager().registerEvents(new InvListener(), this);
        Objects.requireNonNull(getCommand("statusgui")).setExecutor(new GuiCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MethodsManager getMethodsManager() {
        return methodsManager;
    }

    public HeadManager getHeadManager() {
        return headManager;
    }

    public Status getStatusApi() {
        return statusApi;
    }

    public String getPrefix() {
        return prefix;
    }
}
