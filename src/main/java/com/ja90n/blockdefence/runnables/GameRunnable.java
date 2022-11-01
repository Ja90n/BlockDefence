package com.ja90n.blockdefence.runnables;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.managers.EnemyManager;
import com.ja90n.blockdefence.managers.TowerManager;
import com.ja90n.blockdefence.towers.Tower;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class GameRunnable extends BukkitRunnable {

    public BlockDefence blockDefence;
    public TowerManager towerManager;
    public EnemyManager enemyManager;
    private ArrayList<Enemy> enemiesToRemove;

    public GameRunnable(BlockDefence blockDefence){
        this.blockDefence = blockDefence;
        towerManager = blockDefence.getTowerManager();
        enemyManager = blockDefence.getEnemyManager();
        runTaskTimer(blockDefence,0,1);
    }

    @Override
    public void run() {
        for (Tower tower : towerManager.getTowers()){
            tower.shoot();
        }
        for (Enemy enemy : enemyManager.getEnemies()){
            enemy.move();
        }

    }
}
