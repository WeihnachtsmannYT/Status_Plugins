package de.weihnachtsmannyt.status;

import de.weihnachtsmannyt.status.Command.Command;
import de.weihnachtsmannyt.status.Command.TabComplete;
import de.weihnachtsmannyt.status.Manager.ConfigVarManager;
import de.weihnachtsmannyt.status.Manager.EventManager;
import de.weihnachtsmannyt.status.Manager.FileManager;
import de.weihnachtsmannyt.status.Manager.PrefixManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Status extends JavaPlugin {

    private static Status instance;

    //Api
    private PrefixManager prefixManager;
    private FileManager fileManager;
    private ConfigVarManager configVarManager;

    public static Status getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

        //APi Stuff
        instance = this;
        this.prefixManager = new PrefixManager();
        this.fileManager = new FileManager();
        this.configVarManager = new ConfigVarManager();

        configVarManager.updateVar();
        Status.getInstance().getPrefixManager().resetAfkAll();

        Bukkit.getPluginManager().registerEvents(new EventManager(), this);
        getCommand("status").setExecutor(new Command());
        getCommand("status").setTabCompleter(new TabComplete());
        Status.getInstance().getPrefixManager().setScoreboard();

        Status.getInstance().getPrefixManager().updatePrefixAllPlayers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Status.getInstance().getFileManager().saveStatusFile();
        Status.getInstance().getFileManager().saveBlockedWordsFile();
        Status.getInstance().getPrefixManager().resetAfkAll();
        saveDefaultConfig();
    }

    //Api
    public FileManager getFileManager() {
        return fileManager;
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    public ConfigVarManager getConfigVarManager() {
        return configVarManager;
    }
}
