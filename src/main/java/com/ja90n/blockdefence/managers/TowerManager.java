package com.ja90n.blockdefence.managers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.towers.Tower;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;

public class TowerManager {

    private BlockDefence blockDefence;
    private ArrayList<Tower> towers;

    public TowerManager(BlockDefence blockDefence){
        this.blockDefence = blockDefence;
        towers = new ArrayList<>();
    }

    public void addTower(Tower tower){
        towers.add(tower);
    }

    public Tower getTower(Entity entity){
        for (Tower tower : towers){
            if (tower.getArmorStand().equals(entity)){
                return tower;
            }
        }
        return null;
    }

    public Tower getTower(Location location){
        for (Tower tower : towers){
            Location location1 = tower.getArmorStand().getLocation();
            location1.setPitch(0);
            location1.setYaw(0);
            if (location1.equals(location)){
                return tower;
            }
        }
        return null;
    }

    public void clearTowers(){
        ArrayList<Tower> towersToRemove = new ArrayList<>();
        towers = towersToRemove;
        for (Tower tower : towersToRemove){
            tower.remove();
        }
        towersToRemove.clear();
    }

    public void removeTower(Tower tower){
        towers.remove(tower);
    }

    public ArrayList<Tower> getTowers() {
        return towers;
    }
}
