package de.weihnachtsmannyt.custominventoryui;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MethodsManager {

    public static String getCustomLocalizedName(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            NamespacedKey namespacedKey = new NamespacedKey(CustomInventoryUI.getInstance(), "LocalizedName");
            if (meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING)) {
                return meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
            }
        }
        return null;
    }

    public ItemStack CreateItemWithMaterial(Material m, int subid, int amount, String DisplayName, ArrayList<String> lore, String LocalizedName, int CustomModelData) {
        ItemStack is = new ItemStack(m, amount, (short) subid);
        ItemMeta im = is.getItemMeta();
        Objects.requireNonNull(im).setDisplayName(DisplayName);
        im.setLore(lore);

        NamespacedKey namespacedKey = new NamespacedKey(CustomInventoryUI.getInstance(), "LocalizedName");
        im.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, LocalizedName);

        im.setCustomModelData(CustomModelData);

        is.setItemMeta(im);
        return is;
    }

    public ItemStack getHead(Player player, String DisplayName, String lore, String LocalizedName, int CustomModelData) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setLore(Collections.singletonList(lore));
        skull.setOwningPlayer(player);

        Objects.requireNonNull(skull).setDisplayName(DisplayName);

        skull.setCustomModelData(CustomModelData);

        NamespacedKey namespacedKey = new NamespacedKey(CustomInventoryUI.getInstance(), "LocalizedName");
        skull.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, LocalizedName);

        item.setItemMeta(skull);
        return item;
    }
}
