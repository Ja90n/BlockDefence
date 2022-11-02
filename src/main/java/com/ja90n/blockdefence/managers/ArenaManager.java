package com.ja90n.blockdefence.managers;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.instances.Arena;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    private List<Arena> arenas = new ArrayList<>();

    public ArenaManager (BlockDefence blockDefence){
        /*
        try {
            FileConfiguration config = blockDefence.getConfig();
            for (String str : config.getConfigurationSection("arenas.").getKeys(false)){
                arenas.add(new Arena(blockDefence, Integer.parseInt(str),config.getString("arenas." + str + ".name")));
            }
        } catch (NullPointerException e){
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "The config file is not set up correctly");
        }

         */
        arenas.add(new Arena(blockDefence,0,"arena"));
    }

    public List<Arena> getArenas() { return arenas; }

    public Arena getArena(Player player){
        for (Arena arena : arenas){
            if (arena.getPlayers().contains(player.getUniqueId())){
                return arena;
            }
        }
        return null;
    }

    public Arena getArena(int id){
        for (Arena arena : arenas){
            if (arena.getId() == id){
                return arena;
            }
        }
        return null;
    }
}