package com.ja90n.blockdefence.towers.spawns;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.towers.Moveable;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PatrolCar implements Moveable {

    private BlockDefence blockDefence;
    private ArrayList<Location> path;
    private ArmorStand armorStand;
    // Cooldown in ticks
    private int fireRate;
    private double damage;
    private double range;
    private double health;
    private int shootCooldown = 0;

    public PatrolCar(BlockDefence blockDefence){
        this.blockDefence = blockDefence;
        path = reverseArrayList(blockDefence.getPathGenerator().getPath(1));
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
            ArrayList<Enemy> enemies = blockDefence.getEnemyManager().getEnemies(armorStand.getNearbyEntities(0.2,0.2,0.2));
            if (!enemies.isEmpty()){
                for (Enemy enemy : enemies){
                    if (enemy.getHealth() > health){
                        enemy.damage(health);
                        remove();
                    } else {
                        health = health - enemy.getHealth();
                        enemy.remove();
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<Location> getPath() {
        return path;
    }

    @Override
    public void shoot() {
        /*
        if (shootCooldown == fireRate){
            if (!armorStand.getNearbyEntities(range,range,range).isEmpty()){
                Enemy target = blockDefence.getEnemyManager().getFirstEnemy(armorStand.getNearbyEntities(range,range,range));
                if (target != null){
                    target.damage(damage);
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(186, 9, 9), 1.0F);
                    target.getArmorStand().getWorld().spawnParticle(Particle.REDSTONE, target.getArmorStand().getLocation().add(0,0.5,0), 10, dustOptions);
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

    @Override
    public void upgrade() {

    }

    @Override
    public ArmorStand getArmorStand() {
        return armorStand;
    }
}
