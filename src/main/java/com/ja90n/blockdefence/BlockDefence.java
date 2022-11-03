package com.ja90n.blockdefence;

import com.ja90n.blockdefence.command.MainCommand;
import com.ja90n.blockdefence.events.InteractEvent;
import com.ja90n.blockdefence.managers.ArenaManager;
import com.ja90n.blockdefence.managers.ConfigManager;
import com.ja90n.blockdefence.managers.EnemyManager;
import com.ja90n.blockdefence.managers.TowerManager;
import com.ja90n.blockdefence.util.PathGenerator;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockDefence extends JavaPlugin {

    private ArenaManager arenaManager;
    private ConfigManager configManager;

    private PathGenerator pathGenerator;
    private static BlockDefence instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        arenaManager = new ArenaManager(this);
        configManager = new ConfigManager();
        pathGenerator = new PathGenerator(this);

        getCommand("bd").setExecutor(new MainCommand(this));
        getServer().getPluginManager().registerEvents(new InteractEvent(this),this);
        instance = this;
        Bukkit.getConsoleSender().sendMessage("--------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage("Block Defence started up");
        Bukkit.getConsoleSender().sendMessage("--------------------------------------------------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Entity entity : Bukkit.getWorld("world").getEntities()){
            if (entity instanceof ArmorStand){
                entity.remove();
            }
        }
    }

    public PathGenerator getPathGenerator() {
        return pathGenerator;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public static BlockDefence getInstance() {
        return instance;
    }
}
