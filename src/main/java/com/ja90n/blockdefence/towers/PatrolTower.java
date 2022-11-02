package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.towers.spawns.PatrolCar;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PatrolTower implements Tower {

    private Location location;
    private ArmorStand armorStand;
    // Cooldown in ticks
    private int fireRate;
    private BlockDefence blockDefence;
    private int shootCooldown = 0;

    public PatrolTower(Location location, BlockDefence blockDefence){

        this.blockDefence = blockDefence;

        fireRate = 80;

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,5));

    }

    @Override
    public void shoot() {
        shootCooldown++;
        if (shootCooldown == fireRate){
            blockDefence.getTowerManager().addMoveablesToSpawn(new PatrolCar(blockDefence));
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
