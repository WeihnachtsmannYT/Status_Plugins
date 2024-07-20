package de.weihnachtsmannyt.custominventoryui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomInventoryUI extends JavaPlugin {

    private static CustomInventoryUI instance;
    private MethodsManager methodsManager;
    private UiCommand UiCommand;

    public static CustomInventoryUI getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;
        this.methodsManager = new MethodsManager();
        this.UiCommand = new UiCommand();

        Bukkit.getPluginManager().registerEvents(new EventManager(), this);

        this.getCommand("openui").setExecutor(new UiCommand());
    }

    public MethodsManager getMethodsManager() {
        return methodsManager;
    }

    public UiCommand getUiCommand() {
        return UiCommand;
    }
}
