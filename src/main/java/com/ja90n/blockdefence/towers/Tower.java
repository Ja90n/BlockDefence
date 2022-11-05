package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.instances.Game;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface Tower {
    void shoot();
    void upgrade();
    default void remove(){
        getGame().getTowerManager().addTowerToRemove(this);
    }

    // Getters
    double getTotalValue();
    double getStarterPrice();
    double getUpgradePrice();
    double getTotalDamage();
    void setTotalDamage(double damage);
    default void addTotalDamage(double damage){
        setTotalDamage(getTotalDamage() + damage);
        getTowerMenu().setItem(19,getGame().createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED +
                "Damage: " + ChatColor.WHITE + getTotalDamage()));
    }
    Inventory getTowerMenu();
    ArmorStand getArmorStand();
    Game getGame();
}
