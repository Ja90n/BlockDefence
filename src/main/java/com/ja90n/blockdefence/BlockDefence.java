package com.ja90n.blockdefence;

import com.ja90n.blockdefence.command.MainCommand;
import com.ja90n.blockdefence.events.DropEvent;
import com.ja90n.blockdefence.events.InteractEvent;
import com.ja90n.blockdefence.managers.EnemyManager;
import com.ja90n.blockdefence.managers.TowerManager;
import com.ja90n.blockdefence.runnables.GameRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockDefence extends JavaPlugin {

    private TowerManager towerManager;
    private EnemyManager enemyManager;
    private static BlockDefence instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        towerManager = new TowerManager(this);
        enemyManager = new EnemyManager(this);
        new GameRunnable(this);
        getServer().getPluginManager().registerEvents(new DropEvent(this),this);
        getCommand("bd").setExecutor(new MainCommand());
        // getServer().getPluginManager().registerEvents(new InteractEvent(this),this);
        instance = this;
        Bukkit.getConsoleSender().sendMessage("--------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("Block Defence started up");
        Bukkit.getConsoleSender().sendMessage("--------------------------------------------------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        towerManager.clearTowers();
        enemyManager.clearEnemies();
        for (Entity entity : Bukkit.getWorld("world").getEntities()){
            if (entity instanceof ArmorStand){
                entity.remove();
            }
        }
    }

    public TowerManager getTowerManager() {
        return towerManager;
    }
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public static BlockDefence getInstance() {
        return instance;
    }
}
