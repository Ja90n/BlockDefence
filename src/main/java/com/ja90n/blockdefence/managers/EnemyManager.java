package com.ja90n.blockdefence.managers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.towers.Tower;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EnemyManager {

    private Game game;
    private ArrayList<Enemy> enemies;
    private ArrayList<Enemy> enemiesToRemove;

    public EnemyManager(Game game){
        this.game = game;
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

    public ArrayList<Enemy> getEnemies(List<Entity> entities){
        ArrayList<Enemy> enemies1 = new ArrayList<>();
        for (Entity entity : entities){
            if (entity instanceof ArmorStand){
                if (getEnemy((ArmorStand) entity) != null){
                    enemies1.add(getEnemy((ArmorStand) entity));
                }
            }
        }
        return enemies1;
    }

    public void addEnemyToRemove(Enemy enemy){
        enemiesToRemove.add(enemy);
    }

    public void addEnemy(Enemy enemy){
        enemies.add(enemy);
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
        for (Enemy enemy : enemies){
            enemy.getArmorStand().remove();
        }
        enemies.clear();
    }

    public Enemy getFirstEnemy(Location location, double range){
        Entity[] entities = getNearbyEntities(location,range);
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
        for (Enemy enemy : targetedEnemies){
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

    public Entity[] getNearbyEntities(Location l, double radius){
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16))/16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
        for (double chX = 0 -chunkRadius; chX <= chunkRadius; chX ++){
            for (double chZ = 0 -chunkRadius; chZ <= chunkRadius; chZ++){
                double x=l.getX(),y=l.getY(),z=l.getZ();
                for (Entity e : new Location(l.getWorld(),x+(chX*16),y,z+(chZ*16)).getChunk().getEntities()){
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) radiusEntities.add(e);
                }
            }
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
}
