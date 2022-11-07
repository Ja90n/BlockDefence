package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.towers.spawns.PatrolCar;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import com.ja90n.blockdefence.util.TowerMenuGenerator;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class PatrolTower implements Tower {

    private final Game game;
    private final ArmorStand armorStand;
    private final Inventory towerMenu;
    private PatrolTowerUpgrateStates patrolTowerUpgrateStates;
    private final double starterPrice;
    private double totalDamage;
    private double totalValue;
    private int fireRate; // Cooldown in ticks (20/s)
    private int shootCooldown = 0;


    public PatrolTower(Location location, Game game){
        this.game = game;

        patrolTowerUpgrateStates = PatrolTowerUpgrateStates.STANDARD_PATROLTOWER;

        fireRate = patrolTowerUpgrateStates.getFirerate();
        starterPrice = 400.0;

        totalDamage = 0;
        totalValue = starterPrice;

        armorStand = game.getTowerManager().getArmorStand
                (patrolTowerUpgrateStates.getItemStack(), location);

        towerMenu = new TowerMenuGenerator().generateTowerMenu
                (Material.GREEN_STAINED_GLASS_PANE,ChatColor.DARK_GREEN + "Patrol tower",game);
    }

    @Override
    public void shoot() {
        shootCooldown++;
        if (shootCooldown == fireRate){
            game.getTowerManager().addMoveablesToSpawn(new PatrolCar(this));
            shootCooldown = 0;
        }
    }

    @Override
    public void upgrade() {
        totalValue = totalValue + patrolTowerUpgrateStates.getUpgradePrice();

        switch (patrolTowerUpgrateStates){
            case STANDARD_PATROLTOWER:
                patrolTowerUpgrateStates = PatrolTowerUpgrateStates.NOVICE_PATROLTOWER;
                break;
            case NOVICE_PATROLTOWER:
                patrolTowerUpgrateStates = PatrolTowerUpgrateStates.ADVANCED_PATROLTOWER;
                break;
            case ADVANCED_PATROLTOWER:
                patrolTowerUpgrateStates = PatrolTowerUpgrateStates.TANKTASTIC;
                break;
        }

        fireRate = patrolTowerUpgrateStates.getFirerate();

        shootCooldown = 0;

        new TowerMenuGenerator().updateTowerMenu(this);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Fire rate: " + ChatColor.WHITE + fireRate);
        towerMenu.setItem(10,game.createItem(Material.PAPER,"Statistics",lore));

        armorStand.getEquipment().setHelmet(patrolTowerUpgrateStates.getItemStack());
    }

    public void addTotalDamage(double damage){
        totalDamage = totalDamage + damage;
        towerMenu.setItem(19,game.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED +
                "Damage: " + ChatColor.WHITE + totalDamage));
    }

    // Getters
    public Game getGame() {
        return game;
    }

    @Override
    public double getTotalValue() {
        return totalValue;
    }
    @Override
    public double getStarterPrice() {
        return starterPrice;
    }

    @Override
    public double getUpgradePrice() {
        return patrolTowerUpgrateStates.getUpgradePrice();
    }

    @Override
    public double getTotalDamage() {
        return totalDamage;
    }

    @Override
    public void setTotalDamage(double damage) {
        totalDamage = damage;
    }

    @Override
    public Inventory getTowerMenu() {
        return towerMenu;
    }

    @Override
    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public double getCarFireRate() {
        return patrolTowerUpgrateStates.getCarFireRate();
    }
    public double getDamage() {
        return patrolTowerUpgrateStates.getDamage();
    }
    public double getRange() {
        return patrolTowerUpgrateStates.getRange();
    }
    public double getHealth() {
        return patrolTowerUpgrateStates.getHealth();
    }
}

enum PatrolTowerUpgrateStates {
    STANDARD_PATROLTOWER(600, 0,0,0,20,800,new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,5)),
    NOVICE_PATROLTOWER(600, 0,1,0,60,3200,new ItemStackGenerator().getItemStack(Material.STONE_AXE,5)),
    ADVANCED_PATROLTOWER(400, 10,10,8,120,8000,new ItemStackGenerator().getItemStack(Material.STONE_HOE,1)),
    TANKTASTIC(300, 40,200,10,400,999999999,new ItemStackGenerator().getItemStack(Material.STONE_HOE,1));

    private int fireRate;
    private double damage,range,upgradePrice,carFireRate,health;
    private ItemStack itemStack;

    PatrolTowerUpgrateStates(int fireRate, double carFireRate, double damage, double range, double health ,double upgradePrice, ItemStack itemStack) {
        this.fireRate = fireRate;
        this.carFireRate = carFireRate;
        this.damage = damage;
        this.range = range;
        this.health = health;
        this.upgradePrice = upgradePrice;
        this.itemStack = itemStack;
    }
    public int getFirerate(){return fireRate;}
    public double getUpgradePrice(){return upgradePrice;}
    public ItemStack getItemStack() {return itemStack;}
    public double getDamage() {return damage;}
    public double getRange() {return range;}
    public double getCarFireRate() {return carFireRate;}
    public double getHealth() {return health;}
}
