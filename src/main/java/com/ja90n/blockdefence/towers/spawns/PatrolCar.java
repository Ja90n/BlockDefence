package com.ja90n.blockdefence.towers.spawns;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.towers.TowerMoveable;
import com.ja90n.blockdefence.towers.PatrolTower;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

public class PatrolCar implements TowerMoveable {

    private PatrolTower patrolTower;
    private ArrayList<Location> path;
    private ArmorStand armorStand;
    // Cooldown in ticks
    private int fireRate;
    private int upgradeState;
    private double damage;
    private double range;
    private double health;
    private int shootCooldown = 0;

    public PatrolCar(PatrolTower patrolTower, int upgradeState){
        this.patrolTower = patrolTower;
        this.upgradeState = upgradeState;

        path = reverseArrayList(BlockDefence.getInstance().getPathGenerator().getPath(1));
        Location location = path.get(0);

        fireRate = 5;
        damage = 1;
        range = 2;
        health = 20.0;

        armorStand = getGame().getTowerManager().getArmorStand
                (new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,4), location);

        switch (upgradeState){
            case 1:
                armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.STONE_AXE,4));
                break;
        }
    }

    @Override
    public void move() {
        ArrayList <Location> path = getPath();
        if (path.isEmpty()){
            remove();
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

        // Collision check with an entity
        if (!armorStand.getNearbyEntities(0.1,0.1,0.1).isEmpty()){
            ArrayList<Enemy> enemies = getGame().getEnemyManager().getEnemies(armorStand.getNearbyEntities(0.2,0.2,0.2));
            // Gets all the enemies in range
            if (!enemies.isEmpty()){
                // Gets the first enemy
                Enemy enemy = getGame().getEnemyManager().getFirstEnemy(armorStand.getLocation(),range);
                // Checks if the enemy has more health as the car
                if (enemy.getHealth() > health){
                    // If so it removes the car
                    patrolTower.addTotalDamage(health);
                    for (UUID uuid : getGame().getCoins().keySet()){
                        getGame().getCoins().put(uuid,getGame().getCoins().get(uuid) + health);
                    }
                    enemy.damage(health);
                    remove();
                } else {
                    // Otherwise it damages the car and goes on
                    patrolTower.addTotalDamage(enemy.getHealth());
                    health = health - enemy.getHealth();
                    for (UUID uuid : getGame().getCoins().keySet()){
                        getGame().getCoins().put(uuid,getGame().getCoins().get(uuid) + enemy.getHealth());
                    }
                    enemy.remove();
                }
            }
        }
    }

    @Override
    public void shoot() {
        if (upgradeState >= 1){
            if (shootCooldown == fireRate){
                if (!armorStand.getNearbyEntities(range,range,range).isEmpty()){
                    Enemy target = getGame().getEnemyManager().getFirstEnemy(armorStand.getLocation(),range);
                    if (target != null){
                        target.damage(damage);
                        getGame().addCoins(damage);
                        patrolTower.addTotalDamage(damage);
                        shootCooldown = 0;
                    }
                }
            } else {
                shootCooldown++;
            }
        }
    }

    public ArrayList<Location> reverseArrayList(ArrayList<Location> alist) {
        // Arraylist for storing reversed elements
        ArrayList<Location> revArrayList = new ArrayList<Location>();
        for (int i = alist.size() - 1; i >= 0; i--) {
            // Append the elements in reverse order
            revArrayList.add(alist.get(i));
        }
        // Return the reversed arraylist
        return revArrayList;
    }

    // Getters
    @Override
    public ArmorStand getArmorStand() {
        return armorStand;
    }

    @Override
    public Game getGame() {
        return patrolTower.getGame();
    }

    @Override
    public ArrayList<Location> getPath() {
        return path;
    }

    // Dummy returns because it is not necessary for this tower
    @Override
    public double getTotalValue() {
        return 0;
    }
    @Override
    public double getStarterPrice() { return 0; }
    @Override
    public double getUpgradePrice() { return 0; }
    @Override
    public double getTotalDamage() {return 0;}
    @Override
    public void setTotalDamage(double damage) {}

    @Override
    public Inventory getTowerMenu() {
        return null;
    }
    @Override
    public void upgrade() {}
}
