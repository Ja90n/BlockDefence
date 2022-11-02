package com.ja90n.blockdefence.instances;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enums.GameState;
import com.ja90n.blockdefence.runnables.GameRunnable;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    private BlockDefence blockDefence;
    private Arena arena;
    private HashMap<UUID, Double> coins;
    private GameRunnable gameRunnable;

    public Game(BlockDefence blockDefence, Arena arena){
        this.blockDefence = blockDefence;
        this.arena = arena;
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
}
