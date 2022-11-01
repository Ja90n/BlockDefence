package com.ja90n.blockdefence.enemies;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.managers.EnemyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public interface Enemy {

    default void move(){
        ArrayList <Location> path = getPath();
        if (path.isEmpty()){
            finished();
            return;
        }
        // Saves the next point
        Location nextPoint = path.get(0);
        // Checks if there are more points left in the track besides the next point
        if (path.size() != 1){
            // Teleports and makes the ArmorStand look at the next point
            getArmorStand().teleport(nextPoint.setDirection(path.get(1).toVector().subtract(nextPoint.toVector())));
        } else {
            // Teleports the ArmorStand
            getArmorStand().teleport(nextPoint);
        }
        // Removes the current point from the path
        getPath().remove(0);
    }
    default void finished(){
        remove();
        Bukkit.broadcastMessage("an enemy with " + getHealth() + " health made it to the end");
    }
    default void remove(){
        BlockDefence.getInstance().getEnemyManager().addEnemyToRemove(this);
        getArmorStand().remove();
        getPath().clear();
    }
    double getHealth();
    void damage(double health);
    ArrayList<Location> getPath();
    ArmorStand getArmorStand();
    int getPointOnTrack();
}
