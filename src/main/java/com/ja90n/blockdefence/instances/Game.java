package com.ja90n.blockdefence.instances;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enums.GameState;
import com.ja90n.blockdefence.managers.EnemyManager;
import com.ja90n.blockdefence.managers.TowerManager;
import com.ja90n.blockdefence.runnables.GameRunnable;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Game {

    private BlockDefence blockDefence;
    private Arena arena;
    private HashMap<UUID, Double> coins;
    private GameRunnable gameRunnable;

    private TowerManager towerManager;
    private EnemyManager enemyManager;

    private int wave;

    public Game(BlockDefence blockDefence, Arena arena){

        towerManager = new TowerManager(this);
        enemyManager = new EnemyManager(this);

        this.blockDefence = blockDefence;
        this.arena = arena;
        wave = 0;
        gameRunnable = new GameRunnable(blockDefence,this);
        coins = new HashMap<>();
    }

    public void start() {
        for (UUID uuid : arena.getPlayers()){
            Player player = Bukkit.getPlayer(uuid);
            player.setHealth(20);
            player.setGameMode(GameMode.ADVENTURE);
            coins.put(uuid,100.0);
        }
        arena.setGameState(GameState.LIVE);
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

}
