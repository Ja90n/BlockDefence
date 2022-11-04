package com.ja90n.blockdefence.runnables;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.managers.EnemyManager;
import com.ja90n.blockdefence.managers.TowerManager;
import com.ja90n.blockdefence.towers.TowerMoveable;
import com.ja90n.blockdefence.towers.Tower;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GameRunnable extends BukkitRunnable {

    private final TowerManager towerManager;
    private final EnemyManager enemyManager;
    private Game game;

    public GameRunnable(BlockDefence blockDefence, Game game){
        this.game = game;
        towerManager = game.getTowerManager();
        enemyManager = game.getEnemyManager();
        runTaskTimer(blockDefence,0,1);
    }

    @Override
    public void run() {
        for (Tower tower : towerManager.getTowers()){
            tower.shoot();
            if (tower instanceof TowerMoveable){
                ((TowerMoveable) tower).move();
            }
        }
        for (Enemy enemy : enemyManager.getEnemies()){
            enemy.move();
        }
        for (UUID uuid : game.getCoins().keySet()){
            Bukkit.getPlayer(uuid).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText
                    (ChatColor.GOLD + "Coins: " + ChatColor.WHITE + Math.round(game.getCoins().get(uuid))));
        }
    }
}
