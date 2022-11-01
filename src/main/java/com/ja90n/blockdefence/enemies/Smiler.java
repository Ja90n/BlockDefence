package com.ja90n.blockdefence.enemies;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.util.GeneratePath;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Smiler implements Enemy{

    private double health;
    private ArrayList<Location> path;
    private double movementSpeed;
    private ArmorStand armorStand;

    public Smiler(BlockDefence blockDefence,Location location){

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setInvisible(true);
        armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,2));

        health = 10.0;
        movementSpeed = 0.2;
        path = new GeneratePath(blockDefence).generatePath(movementSpeed);
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void damage(double damage){
        this.health = health-damage;
        if (health <= 0){
            remove();
        }
    }

    @Override
    public ArrayList<Location> getPath() {
        return path;
    }

    @Override
    public ArmorStand getArmorStand() {
        return armorStand;
    }

    @Override
    public int getPointOnTrack() {
        double antiMovementSpeed = 1/movementSpeed;
        return (int) Math.round(path.size()*antiMovementSpeed);
    }
}