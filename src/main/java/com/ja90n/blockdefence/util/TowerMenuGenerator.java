package com.ja90n.blockdefence.util;

import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.towers.Tower;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class TowerMenuGenerator {

    public Inventory generateTowerMenu(Material border, String name, Game game){

        // Generate Tower Menu
        Inventory towerMenu = Bukkit.createInventory(null,45, name);

        // Frame
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44}) {
            towerMenu.setItem(i,game.createItem(border, " "));
        }

        // Upgrade button
        towerMenu.setItem(22,game.createItem(Material.EMERALD, ChatColor.GREEN + "Upgrade"));

        // Sell button
        towerMenu.setItem(34,game.createItem(Material.BARRIER, ChatColor.RED + "Sell"));

        // Total damage statistic
        towerMenu.setItem(19,game.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED +
                "Damage: " + ChatColor.WHITE + "0"));

        towerMenu.setItem(10,game.createItem(Material.PAPER,ChatColor.BLUE + "Statistics"));

        return towerMenu;
    }

    public void updateTowerMenu(Tower tower){
        tower.getTowerMenu().setItem(
                22,tower.getGame().createItem(Material.EMERALD, ChatColor.GREEN + "Upgrade",
                        ChatColor.GREEN + "Upgrade cost: " + ChatColor.WHITE + tower.getUpgradePrice()));
        tower.getTowerMenu().setItem(
                34,tower.getGame().createItem(Material.BARRIER, ChatColor.RED + "Sell",
                        ChatColor.RED + "Coins returned: " + ChatColor.WHITE + tower.getTotalValue()/2));

    }
}
