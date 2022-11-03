package com.ja90n.blockdefence.towers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.towers.spawns.PatrolCar;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class PatrolTower implements Tower {

    private Location location;
    private ArmorStand armorStand;

    // Cooldown in ticks
    private int fireRate;
    private Game game;

    private double starterPrice;
    private int shootCooldown = 0;
    private double totalDamage;
    private double totalValue;

    private Inventory towerMenu;

    public PatrolTower(Location location, Game game){

        this.game = game;

        fireRate = 80;
        starterPrice = 400.0;

        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.getEquipment().setHelmet(new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,5));

        totalDamage = 0;
        totalValue = 0;

        // Generate Tower Menu
        towerMenu = Bukkit.createInventory(null,45,ChatColor.DARK_GREEN + "Patrol tower");

        // Frame
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44}) {
            towerMenu.setItem(i,game.createItem(Material.GREEN_STAINED_GLASS_PANE,ChatColor.GREEN + " "));
        }

        // Upgrade button
        towerMenu.setItem(22,game.createItem(Material.EMERALD, ChatColor.GREEN + "Upgrade"));

        // Sell button
        towerMenu.setItem(34,game.createItem(Material.BARRIER, ChatColor.RED + "Sell"));

        // Total damage statistic
        towerMenu.setItem(19,game.createItem(Material.DIAMOND_SWORD, ChatColor.DARK_RED +
                "Damage: " + ChatColor.WHITE + totalDamage));
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
    public boolean upgrade(Player player) {
        return false;
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
    public Inventory getTowerMenu() {
        return towerMenu;
    }

    @Override
    public ArmorStand getArmorStand() {
        return armorStand;
    }
}
