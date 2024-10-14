package de.weihnachtsmannyt.status.Manager;

import de.weihnachtsmannyt.status.Status;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

public class UpdateManager implements Listener {
    private final JavaPlugin plugin;
    private final String currentVersion;

    public UpdateManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion().replace(".", "-"); // Replace dots with hyphens

        // Register this class as an event listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        initializeConfig();
    }

    private void initializeConfig() {
        // Ensure players' seen versions list exists
        FileConfiguration config = plugin.getConfig();
        if (!config.contains("seen_players")) {
            config.createSection("seen_players");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        checkPlayerForUpdates(player);
    }

    public void checkPlayerForUpdates(Player player) {
        UUID playerUUID = player.getUniqueId();
        FileConfiguration config = plugin.getConfig();

        // Get the list of versions that the player has seen
        List<String> seenVersions = config.getStringList("seen_players." + playerUUID);

        // Check if the player has seen the current version
        if (!seenVersions.contains(currentVersion)) {
            // Announce new features for this version
            announceNewFeatures(player);
            // Add the current version to the seen versions list
            seenVersions.add(currentVersion);
            config.set("seen_players." + playerUUID, seenVersions);
            plugin.saveConfig();
        }

        // Check for previous versions the player hasn't seen
        for (String version : config.getConfigurationSection("previous_versions").getKeys(false)) {
            if (!seenVersions.contains(version)) {
                // Announce features for previous versions if any
                announceNewFeaturesForVersion(player, version);
                // Add the version to the seen versions list
                seenVersions.add(version);
                config.set("seen_players." + playerUUID, seenVersions);
                plugin.saveConfig();
            }
        }
    }

    private void announceNewFeatures(Player player) {
        // Get the new features from the configuration
        FileConfiguration config = plugin.getConfig();
        if (config.contains("new_features")) {
            player.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§aThis plugin has new features:");
            for (String feature : config.getStringList("new_features")) {
                player.sendMessage(ChatColor.YELLOW + "- " + feature);
            }
        } else {
            if (player.hasPermission("status detailed update"))
                player.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "§cThe plugin was updated, but no new features were specified.");
        }

        // Save the current version to the list of previous versions
        config.set("previous_versions." + currentVersion, config.getStringList("new_features"));
        plugin.saveConfig();
    }

    private void announceNewFeaturesForVersion(Player player, String version) {
        FileConfiguration config = plugin.getConfig();
        if (config.contains("previous_versions." + version)) {
            player.sendMessage(Status.getInstance().getConfigVarManager().getStatus_Prefix() + "Some features from previous Version " + version + ":");
            for (String feature : config.getStringList("previous_versions." + version)) {
                player.sendMessage(ChatColor.YELLOW + "- " + feature);
            }
        }
    }
}
