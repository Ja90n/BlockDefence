package com.ja90n.blockdefence.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackGenerator {
    public ItemStack getItemStack(Material material, int id){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(id);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
