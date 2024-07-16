package de.weihnachtsmannyt.statusgui.Managers;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

public class MethodsManager {

    public ItemStack CreateItemWithMaterial(Material m, int subid, int amount, String DisplayName, ArrayList<String> lore, String LocalizedName) {
        ItemStack is = new ItemStack(m, amount, (short) subid);
        ItemMeta im = is.getItemMeta();
        Objects.requireNonNull(im).setDisplayName(DisplayName);
        im.setLore(lore);
        im.setLocalizedName(LocalizedName);
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
        skull.setLocalizedName(LocalizedName);
        item.setItemMeta(skull);
        return item;
    }

    public ItemStack CreateSkull(String DisplayName, String LocalizedName, String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        if (url.isEmpty())
            return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Field profileField = Objects.requireNonNull(headMeta).getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        Objects.requireNonNull(headMeta).setLocalizedName(LocalizedName);
        headMeta.setDisplayName(DisplayName);
        head.setItemMeta(headMeta);
        return head;
    }
}
