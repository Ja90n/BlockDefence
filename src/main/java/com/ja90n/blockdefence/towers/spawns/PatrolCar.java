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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class PatrolCar implements TowerMoveable {

    private PatrolTower patrolTower;
    private ArrayList<Location> path;
    private ArmorStand armorStand;
    // Cooldown in ticks
    private int fireRate;
    private double damage;
    private double range;
    private double health;
    private int shootCooldown = 0;

    public PatrolCar(PatrolTower patrolTower){
        this.patrolTower = patrolTower;

        path = reverseArrayList(BlockDefence.getInstance().getPathGenerator().getPath(1));
        Location location = path.get(0);

        fireRate = 20;
        damage = 1;
        range = 5;
        health = 20.0;

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        //
        armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,4));

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

        if (!armorStand.getNearbyEntities(0.2,0.2,0.2).isEmpty()){
            ArrayList<Enemy> enemies = patrolTower.getGame().getEnemyManager().getEnemies(armorStand.getNearbyEntities(0.2,0.2,0.2));
            if (!enemies.isEmpty()){
                for (Enemy enemy : enemies){
                    if (enemy.getHealth() > health){
                        patrolTower.addTotalDamage(health);
                        enemy.damage(health);
                        remove();
                    } else {
                        patrolTower.addTotalDamage(enemy.getHealth());
                        health = health - enemy.getHealth();
                        enemy.remove();
                    }
                }
            }
        }
    }

    @Override
    public void shoot() {
        /*
        if (shootCooldown == fireRate){
            if (!armorStand.getNearbyEntities(range,range,range).isEmpty()){
                Enemy target = blockDefence.getEnemyManager().getFirstEnemy(armorStand.getNearbyEntities(range,range,range));
                if (target != null){
                    target.damage(damage);
                    armorStand.teleport(armorStand.getLocation().setDirection(target.getArmorStand().getLocation().toVector()
                            .subtract(armorStand.getLocation().toVector())));
                    shootCooldown = 0;
                }
            }
        } else {
            shootCooldown++;
        }

         */
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
    public Inventory getTowerMenu() {
        return null;
    }
    @Override
    public boolean upgrade(Player player) { return false; }
}
