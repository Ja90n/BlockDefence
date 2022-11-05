package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.towers.spawns.PatrolCar;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import com.ja90n.blockdefence.util.TowerMenuGenerator;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class PatrolTower implements Tower {

    private final Game game;
    private final ArmorStand armorStand;
    private final Inventory towerMenu;
    private final double starterPrice;
    private double totalDamage;
    private double totalValue;
    private int upgradeState;
    private int fireRate; // Cooldown in ticks (20/s)
    private int shootCooldown = 0;


    public PatrolTower(Location location, Game game){
        this.game = game;

        fireRate = 200;
        starterPrice = 400.0;
        upgradeState = 0;

        totalDamage = 0;
        totalValue = starterPrice;

        armorStand = game.getTowerManager().getArmorStand
                (new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,5), location);

        towerMenu = new TowerMenuGenerator().generateTowerMenu
                (Material.GREEN_STAINED_GLASS_PANE,ChatColor.DARK_GREEN + "Patrol tower",game);
    }

    @Override
    public void shoot() {
        shootCooldown++;
        if (shootCooldown == fireRate){
            game.getTowerManager().addMoveablesToSpawn(new PatrolCar(this,upgradeState));
            shootCooldown = 0;
        }
    }

    @Override
    public void upgrade() {
        totalValue = totalValue + getUpgradePrice();
        upgradeState = upgradeState + 1;
        switch (upgradeState){
            case 1:
                armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.STONE_AXE,5));
                break;
        }
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
        switch (upgradeState){
            case 0:
                return 400;
            case 1:
                return 800;
            default:
                return 999999999;
        }
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
}
