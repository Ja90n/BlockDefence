package com.ja90n.blockdefence.managers;

import com.ja90n.blockdefence.enemies.Smiler;
import com.ja90n.blockdefence.enemies.Zombie;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.util.PathGenerator;
import org.bukkit.Location;

import java.util.HashMap;

public class WaveManager {

    private final Game game;

    // HashMap<Wave id, Hashmap<Number of spawns, Enemy type>> waves
    private HashMap<Integer,HashMap<Object,Integer>> waves;

    public WaveManager(Game game){
        waves = new HashMap<>();
        this.game = game;

        // 0
        addEnemies(0,8, new Zombie());
        // 1
        addEnemies(1,16, new Zombie());
        addEnemies(1,1, new Smiler());
        // 2
        addEnemies(1,16, new Zombie());
        addEnemies(1,4, new Smiler());
    }

    public void addEnemies(int wave,int amount,Object object){
        waves.put(wave,new HashMap<>());
        if (waves.containsKey(wave)){
            waves.get(wave).put(object,waves.get(wave).get(object) + amount);
        } else {
            waves.get(wave).put(object,amount);
        }
    }

    public HashMap<Object,Integer> getEnemies(int waveID){
        return waves.get(waveID);
    }

    public Location getLocation(){
        return game.getBlockDefence().getPathGenerator().getPathLocation("0");
    }
}