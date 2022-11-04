package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import com.ja90n.blockdefence.util.TowerMenuGenerator;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

public class CannonTower implements Tower {

    private Location location;
    private ArmorStand armorStand;
    // Cooldown in ticks
    private int fireRate;
    private double damage;
    private double splashDamage;
    private double range;

    private Inventory towerMenu;

    private double totalDamage;

    private double totalValue;

    private Game game;
    private int upgradeState;

    private int shootCooldown = 0;

    public CannonTower(Location location, Game game){

        this.game = game;

        fireRate = 80;
        damage = 10;
        splashDamage = damage / 2;
        range = 1;

        totalDamage = 0;
        upgradeState = 0;

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,1));

        towerMenu = new TowerMenuGenerator().generateTowerMenu
                (Material.RED_STAINED_GLASS_PANE,ChatColor.DARK_RED + "Canon tower",game);
    }

    @Override
    public void shoot() {
        shootCooldown++;
        if (shootCooldown == fireRate){
            if (!armorStand.getNearbyEntities(range,range,range).isEmpty()){
                Enemy target = game.getEnemyManager().getFirstEnemy(Arrays.asList(getNearbyEntities(armorStand.getLocation(), (int) range)));
                if (target != null){
                    target.damage(damage);
                    addTotalDamage(damage);
                    for (UUID uuid : game.getCoins().keySet()){
                        game.getCoins().put(uuid,game.getCoins().get(uuid) + damage);
                    }
                    armorStand.getLocation().getWorld().playEffect(armorStand.getLocation(), Effect.SMOKE,4);
                    armorStand.teleport(armorStand.getLocation().setDirection(target.getArmorStand().getLocation().toVector()
                            .subtract(armorStand.getLocation().toVector())));

                    // Splash damage calculations
                    for (Entity entity : target.getArmorStand().getNearbyEntities(0.5,0.5,0.5)){
                        if (entity instanceof ArmorStand){
                            Enemy target1 = game.getEnemyManager().getEnemy((ArmorStand) entity);
                            if (target1 != null){
                                target1.damage(splashDamage);
                                addTotalDamage(splashDamage);
                                for (UUID uuid : game.getCoins().keySet()){
                                    game.getCoins().put(uuid,game.getCoins().get(uuid) + splashDamage / 4);
                                }
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

    public void addTotalDamage(double damage){
        totalDamage = totalDamage + damage;
        towerMenu.setItem(19,game.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED +
                "Damage: " + ChatColor.WHITE + totalDamage));
    }


    public static Entity[] getNearbyEntities(Location l, int radius){
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16))/16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
        for (int chX = 0 -chunkRadius; chX <= chunkRadius; chX ++){
            for (int chZ = 0 -chunkRadius; chZ <= chunkRadius; chZ++){
                int x=(int) l.getX(),y=(int) l.getY(),z=(int) l.getZ();
                for (Entity e : new Location(l.getWorld(),x+(chX*16),y,z+(chZ*16)).getChunk().getEntities()){
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) radiusEntities.add(e);
                }
            }
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    @Override
    public void upgrade() {
        upgradeState++;
        switch (upgradeState){
            case 1:
                armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.STONE_AXE,1));
                damage = damage / 2;
                splashDamage = splashDamage * 2;
                break;
        }
    }

    @Override
    public Inventory getTowerMenu() {
        return towerMenu;
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

    @Override
    public double getStarterPrice() {
        return 0;
    }

    @Override
    public double getUpgradePrice() {
        switch (upgradeState){
            case 0:
                return 400;
            case 1:
                return 800;
            default:
                return 999999999;
        }
    }
}
