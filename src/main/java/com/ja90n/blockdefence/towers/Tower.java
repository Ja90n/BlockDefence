package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.BlockDefence;
import org.bukkit.entity.ArmorStand;

public interface Tower {
    void shoot();
    void upgrade();
    ArmorStand getArmorStand();

    default void remove(){
        BlockDefence.getInstance().getTowerManager().removeTower(this);
        getArmorStand().remove();
    }
}
