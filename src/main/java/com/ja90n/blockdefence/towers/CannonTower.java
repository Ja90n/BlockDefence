package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CannonTower implements Tower {

    private Location location;
    private ArmorStand armorStand;
    // Cooldown in ticks
    private int fireRate;
    private double damage;
    private double range;

    private BlockDefence blockDefence;

    private int shootCooldown = 0;

    public CannonTower(Location location, BlockDefence blockDefence){

        this.blockDefence = blockDefence;

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
                Enemy target = blockDefence.getEnemyManager().getFirstEnemy(armorStand.getNearbyEntities(range,range,range));
                if (target != null){
                    target.damage(damage);
                    armorStand.getLocation().getWorld().playEffect(armorStand.getLocation(), Effect.SMOKE,4);
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(186, 9, 9), 1.0F);
                    target.getArmorStand().getWorld().spawnParticle(Particle.REDSTONE, target.getArmorStand().getLocation().add(0,0.5,0), 10, dustOptions);
                    armorStand.teleport(armorStand.getLocation().setDirection(target.getArmorStand().getLocation().toVector()
                            .subtract(armorStand.getLocation().toVector())));
                    for (Entity entity : target.getArmorStand().getNearbyEntities(1,1,1)){
                        if (entity instanceof ArmorStand){
                            Enemy target1 = blockDefence.getEnemyManager().getEnemy((ArmorStand) entity);
                            if (target1 != null){
                                target1.damage(damage / 2);
                                target1.getArmorStand().getWorld().spawnParticle(Particle.REDSTONE, target1.getArmorStand().getLocation().add(0,0.5,0), 10, dustOptions);
                            }
                        }
                    }
                }
            }
            shootCooldown = 0;
        }
    }

    @Override
    public void upgrade() {

    }

    @Override
    public ArmorStand getArmorStand() {
        return armorStand;
    }
}
