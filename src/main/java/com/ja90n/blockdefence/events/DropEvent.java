package com.ja90n.blockdefence.events;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Smiler;
import com.ja90n.blockdefence.enemies.Zombie;
import com.ja90n.blockdefence.towers.ArcherTower;
import com.ja90n.blockdefence.towers.CannonTower;
import com.ja90n.blockdefence.util.GeneratePath;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropEvent implements Listener {

    private BlockDefence blockDefence;

    public DropEvent(BlockDefence blockDefence){
        this.blockDefence = blockDefence;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if (event.getItemDrop().getItemStack().getType().equals(Material.ROTTEN_FLESH)){

        } else if (event.getItemDrop().getItemStack().getType().equals(Material.BOW)){
            blockDefence.getTowerManager().addTower(new ArcherTower(event.getPlayer().getLocation(),blockDefence));
        } else if (event.getItemDrop().getItemStack().getType().equals(Material.TNT)){
            blockDefence.getTowerManager().addTower(new CannonTower(event.getPlayer().getLocation(),blockDefence));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (event.getCurrentItem() == null){
            return;
        }
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Summon menu")){
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("huh");
            switch (event.getSlot()){
                case 0:
                    blockDefence.getEnemyManager().addEnemy(new Zombie(blockDefence,new GeneratePath(blockDefence).getPathLocation("0")));
                case 1:
                    blockDefence.getEnemyManager().addEnemy(new Smiler(blockDefence,new GeneratePath(blockDefence).getPathLocation("0")));
            }
        }
    }
}
