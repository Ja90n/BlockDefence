package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.instances.Game;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface Tower {
    void shoot();
    boolean upgrade(Player player);
    default void remove(){
        getGame().getTowerManager().addTowerToRemove(this);
    }

    // Getters
    Game getGame();
    double getTotalValue();
    double getStarterPrice();
    Inventory getTowerMenu();
    ArmorStand getArmorStand();
}
