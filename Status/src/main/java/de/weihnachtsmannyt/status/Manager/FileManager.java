package de.weihnachtsmannyt.status.Manager;

import de.weihnachtsmannyt.status.Status;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class FileManager {

    private final File statusDataFile;
    private final File blockedWordsDataFile;
    private YamlConfiguration statusData;
    private YamlConfiguration blockedWordsData;

    public FileManager() {
        File folder = new File("./plugins/Status/");
        this.statusDataFile = new File(folder, "status.yml");

        File folder2 = new File("./plugins/Status/");
        this.blockedWordsDataFile = new File(folder2, "blockedWords.yml");

        try {
            if (!folder.exists()) folder.mkdirs();
            if (!statusDataFile.exists()) statusDataFile.createNewFile();
            statusData = YamlConfiguration.loadConfiguration(statusDataFile);

            if (!folder2.exists()) folder2.mkdirs();
            if (!blockedWordsDataFile.exists()) blockedWordsDataFile.createNewFile();
            if (blockedWordsDataFile.length() == 0) {
                copy(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Status")).getResource("blockedWords-template.yml"), blockedWordsDataFile);
            }
            blockedWordsData = YamlConfiguration.loadConfiguration(blockedWordsDataFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerInStatus(Player player, String status, String color) {
        if (playerIsRegistered(player)){
            statusData.set(player.getUniqueId() + ".player", player.getName());
            statusData.set(player.getUniqueId() + ".status", status);
            statusData.set(player.getUniqueId() + ".color", color);
        }else{
            savePlayerInStatusWithPersonalWithAfk(player, status, color, true, true, true, true, false);
        }
    }

    public void savePlayerInStatusWithPersonalWithAfk(Player player, String status, String color,
                                               Boolean Status_Prefix_on_off,
                                               Boolean Join_Leave_Message_on_off,
                                               Boolean DeathCounter_on_off,
                                               Boolean AutoAfk_on_off,
                                               Boolean Afk) {

        int value = 1;

        if (statusData.contains(player.getUniqueId().toString())) {
            value = Objects.requireNonNull(statusData.getConfigurationSection(player.getUniqueId().toString())).getKeys(false).size() + 1;
        }

        statusData.set(player.getUniqueId() + ".player", player.getName());
        statusData.set(player.getUniqueId() + ".status", status);
        statusData.set(player.getUniqueId() + ".color", color);
        statusData.set(player.getUniqueId() + ".Afk", Afk);

        //personal Settings
        ConfigurationSection playerSettings = statusData.createSection(player.getUniqueId()+".p-settings");
        playerSettings.set(".Status_Prefix_on_off",Status_Prefix_on_off);
        playerSettings.set(".Join_Leave_Message_on_off",Join_Leave_Message_on_off);
        playerSettings.set(".DeathCounter_on_off",DeathCounter_on_off);
        playerSettings.set(".AutoAfk_on_off",AutoAfk_on_off);

        Status.getInstance().getFileManager().saveStatusFile();
    }

    public static Boolean playerIsRegistered(Player player) {
        YamlConfiguration statusData = Status.getInstance().getFileManager().getStatusData();
        return statusData.getString(player.getUniqueId().toString()) != null;
    }

    public static Boolean StringIsBlocked(String string) {
        if (Status.getInstance().getConfig().getBoolean("BlockedWords-Toggle")) {
            YamlConfiguration blockedWordsData = Status.getInstance().getFileManager().getBlockedWordsData();
            String[] words = string.split(" ");
            List<String> customWords = blockedWordsData.getStringList("cm-baned-words");
            List<String> blockedWords = blockedWordsData.getStringList("banned-words");
            List<String> blockedWords_enl = blockedWordsData.getStringList("banned-words-enl");
            List<String> blockedWords_gl_enl = blockedWordsData.getStringList("banned-words-gl-enl");

            for (String word : words) {
                for (String bWords : customWords) {
                    if (word.equalsIgnoreCase(bWords)) {
                        return true;
                    }
                }
            }

            for (String word : words) {
                for (String bWords : blockedWords) {
                    if (word.equalsIgnoreCase(bWords)) {
                        return true;
                    }
                }
            }

            for (String word : words) {
                for (String bWords : blockedWords_enl) {
                    if (word.equalsIgnoreCase(bWords)) {
                        return true;
                    }
                }
            }

            for (String word : words) {
                for (String bWords : blockedWords_gl_enl) {
                    if (word.equalsIgnoreCase(bWords)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public void reloadStatusFile() {
        YamlConfiguration.loadConfiguration(statusDataFile);
    }

    public void reloadBlockedWordsFile() {
        YamlConfiguration.loadConfiguration(blockedWordsDataFile);
    }

    public void saveStatusFile() {
        try {
            statusData.save(statusDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        reloadStatusFile();
    }

    public void saveBlockedWordsFile() {
        try {
            blockedWordsData.save(blockedWordsDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getStatusData() {
        reloadStatusFile();
        return statusData;
    }

    public YamlConfiguration getBlockedWordsData() {
        reloadBlockedWordsFile();
        return blockedWordsData;
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
