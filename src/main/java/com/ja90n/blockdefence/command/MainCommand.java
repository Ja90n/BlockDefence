package com.ja90n.blockdefence.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Inventory inventory = Bukkit.createInventory(null,18, ChatColor.BLUE + "Summon menu");
        inventory.setItem(0,new ItemStack(Material.ROTTEN_FLESH));
        inventory.setItem(1,new ItemStack(Material.RECOVERY_COMPASS));
        if (sender instanceof Player){
            ((Player) sender).openInventory(inventory);
        }
        return false;
    }
}
