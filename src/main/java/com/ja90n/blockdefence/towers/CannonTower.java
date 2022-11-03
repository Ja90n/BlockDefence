package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CannonTower implements Tower {

    private Location location;
    private ArmorStand armorStand;
    // Cooldown in ticks
    private int fireRate;
    private double damage;
    private double range;

    private double totalValue;

    private Game game;

    private int shootCooldown = 0;

    public CannonTower(Location location, Game game){

        this.game = game;

        fireRate = 80;
        damage = 10;
        range = 5;

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,1));
    }

    @Override
    public void shoot() {
        shootCooldown++;
        if (shootCooldown == fireRate){
            if (!armorStand.getNearbyEntities(range,range,range).isEmpty()){
                Enemy target = game.getEnemyManager().getFirstEnemy(armorStand.getNearbyEntities(range,range,range));
                if (target != null){
                    target.damage(damage);
                    armorStand.getLocation().getWorld().playEffect(armorStand.getLocation(), Effect.SMOKE,4);
                    armorStand.teleport(armorStand.getLocation().setDirection(target.getArmorStand().getLocation().toVector()
                            .subtract(armorStand.getLocation().toVector())));

                    // Splash damage calculations
                    for (Entity entity : target.getArmorStand().getNearbyEntities(1,1,1)){
                        if (entity instanceof ArmorStand){
                            Enemy target1 = game.getEnemyManager().getEnemy((ArmorStand) entity);
                            if (target1 != null){
                                target1.damage(damage / 2);
                                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(186, 9, 9), 1.0F);
                                target1.getArmorStand().getWorld().spawnParticle(Particle.REDSTONE, target1.getArmorStand().getLocation().add(0,0.5,0), 10,dustOptions);
                            }
                        }
                    }
                }
            }
            shootCooldown = 0;
        }
    }

    @Override
    public boolean upgrade() {

    }

    @Override
    public Inventory getTowerMenu() {
        return null;
    }

    @Override
    public ArmorStand getArmorStand() {
        return armorStand;
    }

    @Override
    public Game getGame() {
        return null;
    }

    @Override
    public double getTotalValue() {
        return totalValue;
    }
}
