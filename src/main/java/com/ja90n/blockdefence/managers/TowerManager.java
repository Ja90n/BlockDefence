package com.ja90n.blockdefence.managers;

import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.towers.TowerMoveable;
import com.ja90n.blockdefence.towers.Tower;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class TowerManager {

    private Game game;
    private ArrayList<TowerMoveable> moveablesToSpawn;
    private ArrayList<Tower> towersToRemove;
    private ArrayList<Tower> towers;

    public TowerManager(Game game){
        this.game = game;
        towers = new ArrayList<>();
        moveablesToSpawn = new ArrayList<>();
        towersToRemove = new ArrayList<>();
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

    public void addMoveablesToSpawn(TowerMoveable moveables){
        moveablesToSpawn.add(moveables);
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
        for (Tower tower : towers){
            tower.getArmorStand().remove();
        }
        towers.clear();
    }

    public void addTowerToRemove(Tower tower){
        if (tower.getArmorStand() != null){
            tower.getArmorStand().remove();
        }
        towersToRemove.add(tower);
    }

    public ArrayList<Tower> getTowers() {
        if (!moveablesToSpawn.isEmpty()){
            towers.addAll(moveablesToSpawn);
            moveablesToSpawn.clear();
        }
        if (!towersToRemove.isEmpty()){
            for (Tower tower : towersToRemove){
                towers.remove(tower);
            }
            towersToRemove.clear();
        }
        return towers;
    }

    public ArmorStand getArmorStand(ItemStack itemStack, Location location){
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.getEquipment().setHelmet(itemStack);
        return armorStand;
    }
}
