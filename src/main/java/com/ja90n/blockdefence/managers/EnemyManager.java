package com.ja90n.blockdefence.managers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.towers.Tower;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class EnemyManager {

    private BlockDefence blockDefence;
    private ArrayList<Enemy> enemies;
    private ArrayList<Enemy> enemiesToRemove;

    public EnemyManager(BlockDefence blockDefence){
        this.blockDefence = blockDefence;
        enemies = new ArrayList<>();
        enemiesToRemove = new ArrayList<>();
    }

    public ArrayList<Enemy> getEnemies(){
        if (enemiesToRemove.size() == 0){
            return enemies;
        } else {
            for (Enemy enemy : enemiesToRemove){
                enemies.remove(enemy);
            }
            return enemies;
        }
    }

    public void addEnemyToRemove(Enemy enemy){
        enemiesToRemove.add(enemy);
    }

    public void addEnemy(Enemy enemy){
        enemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy){
        enemies.remove(enemy);
    }

    public Enemy getEnemy(ArmorStand armorStand){
        for (Enemy enemy : enemies){
            if (enemy.getArmorStand().equals(armorStand)){
                return enemy;
            }
        }
        return null;
    }

    public void clearEnemies(){
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
        enemies = enemiesToRemove;
        for (Enemy enemy : enemiesToRemove){
            enemy.remove();
        }
        enemiesToRemove.clear();
    }

    public Enemy getFirstEnemy(List<Entity> entities){
        ArrayList<Enemy> targetedEnemies = new ArrayList<>();
        for (Entity entity : entities){
            if (entity instanceof ArmorStand){
                Enemy target = getEnemy((ArmorStand) entity);
                if (target != null){
                    targetedEnemies.add(target);
                }
            }
        }
        Enemy firstEnemy = null;
        for (Enemy enemy : enemies){
            if (firstEnemy == null){
                firstEnemy = enemy;
            } else {
                if (firstEnemy.getPointOnTrack() > enemy.getPointOnTrack()){
                    firstEnemy = enemy;
                }
            }
        }
        return firstEnemy;
    }
}
