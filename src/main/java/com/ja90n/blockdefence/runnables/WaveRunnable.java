package com.ja90n.blockdefence.runnables;

import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.enemies.Smiler;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.managers.WaveManager;
import com.ja90n.blockdefence.util.PathGenerator;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class WaveRunnable extends BukkitRunnable {

    private Game game;
    private int wave;
    private WaveManager waveManager;
    private Location location;

    public WaveRunnable(Game game){
        this.game = game;
        waveManager = game.getWaveManager();
        location = waveManager.getLocation();
        wave = 0;
    }

    @Override
    public void run() {
        for (Object object : waveManager.getEnemies(wave).keySet()){
            if (object instanceof Enemy){
                Enemy enemy = (Enemy) object;
                for (int i = waveManager.getEnemies(wave).get(object); i > 0; i--){
                    enemy.initialize(game,location);
                    game.getEnemyManager().addEnemy(enemy);
                }
            }
        }
        wave++;
    }
}
