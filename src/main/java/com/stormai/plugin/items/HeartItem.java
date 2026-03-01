package com.stormai.plugin.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class HeartItem {

    private static final NamespacedKey HEART_KEY = new NamespacedKey("lifesteal", "heart");

    public static ShapedRecipe getHeartItemRecipe() {
        ItemStack heartItem = createHeartItem();

        ShapedRecipe recipe = new ShapedRecipe(HEART_KEY, heartItem);
        recipe.shape("GGG", "GNG", "GGG");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('N', Material.NETHER_STAR);

        return recipe;
    }

    public static ItemStack createHeartItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§cHeart");
        meta.setLore(Arrays.asList("§7Right-click to consume", "§7and gain +1 max health"));
        meta.getPersistentDataContainer().set(HEART_KEY, PersistentDataType.BYTE, (byte) 1);

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isHeartItem(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        return meta.getPersistentDataContainer().has(HEART_KEY, PersistentDataType.BYTE);
    }
}