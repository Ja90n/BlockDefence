package com.ja90n.blockdefence.enemies;

import com.ja90n.blockdefence.instances.Game;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;

public interface Enemy {

    void initialize(Game game, Location location);

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
        getGame().castleDamage(getHealth());
        remove();
    }
    default void remove(){
        getGame().getEnemyManager().addEnemyToRemove(this);
        getArmorStand().remove();
        getPath().clear();
    }
    double getHealth();
    void damage(double damage);
    ArrayList<Location> getPath();
    ArmorStand getArmorStand();
    int getPointOnTrack();
    Game getGame();
}
