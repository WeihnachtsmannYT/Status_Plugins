package de.weihnachtsmannyt.statusgui.Managers;

import de.weihnachtsmannyt.statusgui.Statusgui;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class MethodsManager {

    public static String getCustomLocalizedName(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(Statusgui.getInstance(), "LocalizedName");
            if (meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING)) {
                return meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
            }
        }
        return null;
    }

    public ItemStack CreateItemWithMaterial(Material m, int subid, int amount, String DisplayName, ArrayList<String> lore, String LocalizedName) {
        ItemStack is = new ItemStack(m, amount, (short) subid);
        ItemMeta im = is.getItemMeta();
        Objects.requireNonNull(im).setDisplayName(DisplayName);
        im.setLore(lore);

        NamespacedKey namespacedKey = new NamespacedKey(Statusgui.getInstance(), "LocalizedName");
        im.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, LocalizedName);

        is.setItemMeta(im);
        return is;
    }

    public ItemStack getHead(Player player, String DisplayName, String lore, String LocalizedName) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        Objects.requireNonNull(skull).setDisplayName(player.getName());
        skull.setDisplayName(DisplayName);
        skull.setLore(Collections.singletonList(lore));
        skull.setOwningPlayer(player);

        NamespacedKey namespacedKey = new NamespacedKey(Statusgui.getInstance(), "LocalizedName");
        skull.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, LocalizedName);

        item.setItemMeta(skull);
        return item;
    }

    public ItemStack CreateSkull(String DisplayName, String LocalizedName, String base64) {
        ItemStack head = Statusgui.getInstance().getHeadManager().getHead(base64);

        ItemMeta meta = Objects.requireNonNull(head.getItemMeta());

        NamespacedKey namespacedKey = new NamespacedKey(Statusgui.getInstance(), "LocalizedName");
        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, LocalizedName);

        meta.setDisplayName(DisplayName);
        head.setItemMeta(meta);
        return head;
    }
}
