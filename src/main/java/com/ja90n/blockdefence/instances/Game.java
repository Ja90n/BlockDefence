package com.ja90n.blockdefence.instances;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enums.GameState;
import com.ja90n.blockdefence.managers.EnemyManager;
import com.ja90n.blockdefence.managers.TowerManager;
import com.ja90n.blockdefence.runnables.GameRunnable;
import com.ja90n.blockdefence.managers.WaveManager;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Game {

    private final BlockDefence blockDefence;
    private final Arena arena;
    private HashMap<UUID, Double> coins;
    private final GameRunnable gameRunnable;

    private final TowerManager towerManager;
    private final EnemyManager enemyManager;
    private WaveManager waveManager;
    private double castleHealth;
    private ArmorStand armorStand;

    public Game(BlockDefence blockDefence, Arena arena){

        waveManager = new WaveManager(this);
        towerManager = new TowerManager(this);
        enemyManager = new EnemyManager(this);

        this.blockDefence = blockDefence;
        this.arena = arena;
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
        armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0,-1,0), EntityType.ARMOR_STAND);
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

    public void addCoins(double damage){
        for (UUID uuid : getCoins().keySet()){
            getCoins().put(uuid,getCoins().get(uuid) + damage);
        }
    }

    public void addCoins(double damage,Player player){
        getCoins().put(player.getUniqueId(),getCoins().get(player.getUniqueId()) + damage);
    }

    public void removeCoins(double coins, Player player){
        getCoins().put(player.getUniqueId(),getCoins().get(player.getUniqueId()) - coins);
    }

    public HashMap<UUID, Double> getCoins() {
        return coins;
    }
    public GameRunnable getGameRunnable() {
        return gameRunnable;
    }
    public TowerManager getTowerManager() {
        return towerManager;
    }
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }
    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }

    public ItemStack createItem(Material material, String name){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public ItemStack createItem(Material material, String name,String lore){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        List<String> loreList = new ArrayList<>();
        loreList.add(lore);
        itemMeta.setLore(loreList);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public ItemStack createItem(Material material, String name, List<String> lore){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public BlockDefence getBlockDefence() {
        return blockDefence;
    }
}
