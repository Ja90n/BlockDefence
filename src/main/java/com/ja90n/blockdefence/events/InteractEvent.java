package com.ja90n.blockdefence.events;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.towers.ArcherTower;
import com.ja90n.blockdefence.towers.CannonTower;
import com.ja90n.blockdefence.towers.PatrolTower;
import com.ja90n.blockdefence.towers.Tower;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class InteractEvent implements Listener {

    private BlockDefence blockDefence;

    private HashMap<UUID,Location> clickedLocation;

    public InteractEvent(BlockDefence blockDefence){
        this.blockDefence = blockDefence;
        clickedLocation = new HashMap<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (blockDefence.getTowerManager().getTower(event.getClickedBlock().getLocation().add(0.5,1,0.5)) == null){

                Inventory inventory = Bukkit.createInventory(event.getPlayer(),9, ChatColor.BLUE + "Place menu");

                inventory.setItem(0,new ItemStack(Material.BOW));
                inventory.setItem(1,new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,4,ChatColor.DARK_GREEN + "Patrol tower"));
                inventory.setItem(2,new ItemStackGenerator().getItemStack(Material.WOODEN_AXE,1,ChatColor.DARK_RED + "Cannon"));
                event.getPlayer().openInventory(inventory);

                clickedLocation.put(event.getPlayer().getUniqueId(),event.getClickedBlock().getLocation().add(0.5,1,0.5));

            }
        }
    }

    @EventHandler
    public void InteractAtEntity(PlayerInteractAtEntityEvent event){
        if (event.getRightClicked() instanceof ArmorStand){
            Tower tower = blockDefence.getTowerManager().getTower(event.getRightClicked());
            if (tower != null){
                blockDefence.getTowerManager().addTowerToRemove(tower);
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (event.getCurrentItem() != null){
            if (event.getView().getTitle().equals(ChatColor.BLUE + "Place menu")){
                event.setCancelled(true);
                switch (event.getSlot()){
                    case 0:
                        blockDefence.getTowerManager().addTower(new ArcherTower(clickedLocation.get(event.getWhoClicked().getUniqueId()), blockDefence));
                        break;
                    case 1:
                        blockDefence.getTowerManager().addTower(new PatrolTower(clickedLocation.get(event.getWhoClicked().getUniqueId()), blockDefence));
                        break;
                    case 2:
                        blockDefence.getTowerManager().addTower(new CannonTower(clickedLocation.get(event.getWhoClicked().getUniqueId()), blockDefence));
                        break;
                }
                event.getWhoClicked().closeInventory();
                clickedLocation.remove(event.getWhoClicked().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Place menu")){
            clickedLocation.remove(event.getPlayer().getUniqueId());
        }
    }
}
