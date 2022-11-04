package com.ja90n.blockdefence.instances;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enums.GameState;
import com.ja90n.blockdefence.managers.EnemyManager;
import com.ja90n.blockdefence.managers.TowerManager;
import com.ja90n.blockdefence.runnables.GameRunnable;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Game {

    private BlockDefence blockDefence;
    private Arena arena;
    private HashMap<UUID, Double> coins;
    private GameRunnable gameRunnable;

    private TowerManager towerManager;
    private EnemyManager enemyManager;

    private double castleHealth;
    private ArmorStand armorStand;

    private int wave;

    public Game(BlockDefence blockDefence, Arena arena){

        towerManager = new TowerManager(this);
        enemyManager = new EnemyManager(this);

        this.blockDefence = blockDefence;
        this.arena = arena;
        wave = 0;
        gameRunnable = new GameRunnable(blockDefence,this);
        coins = new HashMap<>();
        castleHealth = 250;
    }

    public void start() {
        for (UUID uuid : arena.getPlayers()){
            Player player = Bukkit.getPlayer(uuid);
            player.setHealth(20);
            player.setGameMode(GameMode.ADVENTURE);
            coins.put(uuid,100.0);
        }
        ArrayList<Location> path = blockDefence.getPathGenerator().generatePath(1);
        Location location = path.get(path.size()-1);

        // Castle health indicator
        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setCustomName(ChatColor.BLUE + "Castle health: " + ChatColor.WHITE + castleHealth);
        armorStand.setCustomNameVisible(true);

        arena.setGameState(GameState.LIVE);
    }

    public void castleDamage(double damage){
        if (damage > castleHealth){
            arena.sendMessage("The castle has fallen.");
            arena.sendMessage("Stopping game.");
            arena.stopGame();
        } else {
            castleHealth = castleHealth - damage;
            armorStand.setCustomName(ChatColor.BLUE + "Castle health: " + ChatColor.WHITE + castleHealth);
            arena.sendActionBar(ChatColor.RED + "Castle is taking damage!");
        }
    }

    public HashMap<UUID, Double> getCoins() {
        return coins;
    }
    public GameRunnable getGameRunnable() {
        return gameRunnable;
    }
    public int getWave() {
        return wave;
    }
    public TowerManager getTowerManager() {
        return towerManager;
    }
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }
    public ItemStack createItem(Material material, String name){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
