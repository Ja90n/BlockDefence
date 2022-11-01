package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ArcherTower implements Tower {

    private Location location;
    private ArmorStand armorStand;
    // Cooldown in ticks
    private int fireRate;
    private double damage;
    private double range;

    private BlockDefence blockDefence;

    private int shootCooldown = 0;

    public ArcherTower(Location location, BlockDefence blockDefence){

        this.blockDefence = blockDefence;

        fireRate = 20;
        damage = 1;
        range = 5;

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.WOODEN_HOE,1));

    }

    @Override
    public void shoot() {
        shootCooldown++;
        if (shootCooldown == fireRate){
            if (!armorStand.getNearbyEntities(range,range,range).isEmpty()){
                Enemy target = blockDefence.getEnemyManager().getFirstEnemy(armorStand.getNearbyEntities(range,range,range));
                if (target != null){
                    target.damage(damage);
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(186, 9, 9), 1.0F);
                    target.getArmorStand().getWorld().spawnParticle(Particle.REDSTONE, target.getArmorStand().getLocation().add(0,0.5,0), 10, dustOptions);
                    armorStand.teleport(armorStand.getLocation().setDirection(target.getArmorStand().getLocation().toVector()
                            .subtract(armorStand.getLocation().toVector())));
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
