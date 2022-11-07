package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import com.ja90n.blockdefence.util.TowerMenuGenerator;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
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

    private CanonUpgradeStates canonUpgradeStates;

    private Game game;
    private int shootCooldown = 0;

    public CannonTower(Location location, Game game){
        this.game = game;

        canonUpgradeStates = CanonUpgradeStates.STANDARDCANNON;

        fireRate = canonUpgradeStates.getFirerate();
        damage = canonUpgradeStates.getDamage();
        range = canonUpgradeStates.getRange();

        splashDamage = damage / 4;

        totalValue = 300;
        totalDamage = 0;

        armorStand = game.getTowerManager().getArmorStand
                (new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,1), location);

        towerMenu = new TowerMenuGenerator().generateTowerMenu
                (Material.RED_STAINED_GLASS_PANE,ChatColor.DARK_RED + "Canon tower",game);

        new TowerMenuGenerator().updateTowerMenu(this);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Fire rate: " + ChatColor.WHITE + fireRate);
        lore.add(ChatColor.RED + "Damage: " + ChatColor.WHITE + damage);
        lore.add(ChatColor.GREEN + "Range: " + ChatColor.WHITE + range);
        towerMenu.setItem(10,game.createItem(Material.PAPER,"Statistics",lore));
    }

    @Override
    public void shoot() {
        shootCooldown++;
        if (shootCooldown >= fireRate){
            if (!armorStand.getNearbyEntities(range,range,range).isEmpty()){
                Enemy target = game.getEnemyManager().getFirstEnemy(armorStand.getLocation(),range);
                if (target != null){
                    target.damage(damage);
                    addTotalDamage(damage);
                    game.addCoins(damage);
                    armorStand.getLocation().getWorld().playEffect(armorStand.getLocation(), Effect.SMOKE,4);

                    armorStand.teleport(armorStand.getLocation().setDirection(target.getArmorStand().getLocation().toVector()
                            .subtract(armorStand.getLocation().toVector())));

                    // Splash damage calculations
                    for (Entity entity : target.getArmorStand().getNearbyEntities(range/3,range/3,range/3)){
                        if (entity instanceof ArmorStand){
                            Enemy target1 = game.getEnemyManager().getEnemy((ArmorStand) entity);
                            if (target1 != null){
                                target1.damage(splashDamage);
                                addTotalDamage(splashDamage);
                                game.addCoins(splashDamage / 2);
                                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(186, 9, 9), 1.0F);
                                target1.getArmorStand().getWorld().spawnParticle(Particle.REDSTONE, target1.getArmorStand().getLocation().add(0,0.5,0), 10,dustOptions);
                            }
                        }
                    }
                    shootCooldown = 0;
                }
            }
        }
    }

    @Override
    public void upgrade() {

        totalValue = totalValue + canonUpgradeStates.getUpgradePrice();

        switch (canonUpgradeStates){
            case STANDARDCANNON:
                canonUpgradeStates = CanonUpgradeStates.NOVICECANNON;
                break;
            case NOVICECANNON:
                canonUpgradeStates = CanonUpgradeStates.ADVANCEDCANNON;
                break;
            case ADVANCEDCANNON:
                canonUpgradeStates = CanonUpgradeStates.WOWCANNON;
                break;
        }
        damage = canonUpgradeStates.getDamage();
        fireRate = canonUpgradeStates.getFirerate();
        range = canonUpgradeStates.getRange();

        splashDamage = damage / 4;

        shootCooldown = 0;

        new TowerMenuGenerator().updateTowerMenu(this);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Fire rate: " + ChatColor.WHITE + fireRate);
        lore.add(ChatColor.RED + "Damage: " + ChatColor.WHITE + damage);
        lore.add(ChatColor.GREEN + "Range: " + ChatColor.WHITE + range);
        towerMenu.setItem(10,game.createItem(Material.PAPER,"Statistics",lore));

        armorStand.getEquipment().setHelmet(canonUpgradeStates.getItemStack());
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
        return game;
    }

    @Override
    public double getTotalValue() {
        return totalValue;
    }

    @Override
    public double getStarterPrice() {
        return 300;
    }

    @Override
    public double getUpgradePrice() {
        return canonUpgradeStates.getUpgradePrice();
    }

    @Override
    public double getTotalDamage() {
        return totalDamage;
    }

    @Override
    public void setTotalDamage(double damage) {
        totalDamage = damage;
    }
}

enum CanonUpgradeStates {
    STANDARDCANNON(40, 10, 3, 600,new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,1)),
    NOVICECANNON(30, 15, 3,1800,new ItemStackGenerator().getItemStack(Material.STONE_AXE,1)),
    ADVANCEDCANNON(30, 20, 4,3200,new ItemStackGenerator().getItemStack(Material.STONE_AXE,1)),
    WOWCANNON(20, 40, 5,999999999,new ItemStackGenerator().getItemStack(Material.STONE_AXE,1));

    private int fireRate;
    private double damage,range,upgradePrice;
    private ItemStack itemStack;

    CanonUpgradeStates(int fireRate, double damage, double range, double upgradePrice, ItemStack itemStack) {
        this.fireRate = fireRate;
        this.damage = damage;
        this.range = range;
        this.upgradePrice = upgradePrice;
        this.itemStack = itemStack;
    }
    public int getFirerate(){return fireRate;}
    public double getDamage(){return damage;}
    public double getRange(){return range;}
    public double getUpgradePrice(){return upgradePrice;}
    public ItemStack getItemStack() {return itemStack;}
}
