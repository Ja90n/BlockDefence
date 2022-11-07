package com.ja90n.blockdefence.enemies;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.util.PathGenerator;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class Smiler implements Enemy{

    private Game game;
    private double health;
    private ArrayList<Location> path;
    private double movementSpeed;
    private ArmorStand armorStand;

    @Override
    public void initialize(Game game, Location location) {
        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setInvisible(true);
        armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,2));

        this.game = game;

        health = 50.0;
        movementSpeed = 2;
        path = BlockDefence.getInstance().getPathGenerator().getPath(movementSpeed);
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void damage(double damage){
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(186, 9, 9), 1.0F);
        armorStand.getWorld().spawnParticle(Particle.REDSTONE, armorStand.getLocation().add(0,0.5,0), 10, dustOptions);
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

    @Override
    public Game getGame() {
        return game;
    }
}