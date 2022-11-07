package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import com.ja90n.blockdefence.util.TowerMenuGenerator;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArcherTower implements Tower {

    private ArmorStand armorStand;
    // Cooldown in ticks
    private int fireRate;
    private double damage;
    private double range;
    private Game game;
    private int shootCooldown = 0;
    private double totalValue;
    private double totalDamage;

    private ArcherUpgradeStates archerUpgradeStates;

    private Inventory towerMenu;

    public ArcherTower(Location location, Game game){
        this.game = game;

        archerUpgradeStates = ArcherUpgradeStates.STANDARDARCHER;

        fireRate = archerUpgradeStates.getFirerate();
        damage = archerUpgradeStates.getDamage();
        range = archerUpgradeStates.getRange();

        totalDamage = 0;
        totalValue = 100;

        armorStand = game.getTowerManager().getArmorStand(archerUpgradeStates.getItemStack(), location);

        towerMenu = new TowerMenuGenerator().generateTowerMenu
                (Material.RED_STAINED_GLASS_PANE, ChatColor.WHITE + "Archer tower",game);

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
        if (shootCooldown == fireRate){
            if (!armorStand.getNearbyEntities(range,range,range).isEmpty()){
                Enemy target = game.getEnemyManager().getFirstEnemy(armorStand.getLocation(),range);
                if (target != null){
                    target.damage(damage);
                    addTotalDamage(damage);
                    game.addCoins(damage);
                    armorStand.teleport(armorStand.getLocation().setDirection(target.getArmorStand().getLocation().toVector()
                            .subtract(armorStand.getLocation().toVector())));
                }
            }
            shootCooldown = 0;
        }
    }


    @Override
    public void upgrade() {
        totalValue = totalValue + archerUpgradeStates.getUpgradePrice();
        switch (archerUpgradeStates){
            case STANDARDARCHER:
                archerUpgradeStates = ArcherUpgradeStates.NOVICEARCHER;
                break;
            case NOVICEARCHER:
                archerUpgradeStates = ArcherUpgradeStates.ADVANCEDARCHER;
                break;
            case ADVANCEDARCHER:
                archerUpgradeStates = ArcherUpgradeStates.ROBINHOOD;
                break;
        }
        damage = archerUpgradeStates.getDamage();
        fireRate = archerUpgradeStates.getFirerate();
        range = archerUpgradeStates.getRange();

        shootCooldown = 0;

        new TowerMenuGenerator().updateTowerMenu(this);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Fire rate: " + ChatColor.WHITE + fireRate);
        lore.add(ChatColor.RED + "Damage: " + ChatColor.WHITE + damage);
        lore.add(ChatColor.GREEN + "Range: " + ChatColor.WHITE + range);
        towerMenu.setItem(10,game.createItem(Material.PAPER,"Statistics",lore));

        armorStand.getEquipment().setHelmet(archerUpgradeStates.getItemStack());
    }

    @Override
    public double getStarterPrice() {
        return 100;
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
    public double getUpgradePrice() {
        return archerUpgradeStates.getUpgradePrice();
    }

    @Override
    public double getTotalDamage() {
        return totalDamage;
    }

    // Setters

    @Override
    public void setTotalDamage(double damage) {
        totalDamage = damage;
    }
}

enum ArcherUpgradeStates {
    STANDARDARCHER(20, 1, 3, 100,new ItemStackGenerator().getItemStack(Material.WOODEN_HOE,1)),
    NOVICEARCHER(15, 2, 4,200,new ItemStackGenerator().getItemStack(Material.STONE_HOE,1)),
    ADVANCEDARCHER(10, 4, 6,400,new ItemStackGenerator().getItemStack(Material.STONE_HOE,1)),
    ROBINHOOD(7, 8, 8,999999999,new ItemStackGenerator().getItemStack(Material.STONE_HOE,1));

    private int fireRate;
    private double damage,range,upgradePrice;
    private ItemStack itemStack;

    ArcherUpgradeStates(int fireRate, double damage, double range, double upgradePrice, ItemStack itemStack) {
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
