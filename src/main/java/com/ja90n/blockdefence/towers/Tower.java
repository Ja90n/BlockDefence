package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.instances.Game;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;

public interface Tower {
    void shoot();
    void upgrade();
    Inventory getTowerMenu();
    ArmorStand getArmorStand();
    default void remove(){
        getGame().getTowerManager().addTowerToRemove(this);
    }

    Game getGame();

    double getTotalValue();
}
