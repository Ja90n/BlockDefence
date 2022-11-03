package com.ja90n.blockdefence.events;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enemies.Enemy;
import com.ja90n.blockdefence.enemies.Smiler;
import com.ja90n.blockdefence.enemies.Zombie;
import com.ja90n.blockdefence.enums.GameState;
import com.ja90n.blockdefence.instances.Arena;
import com.ja90n.blockdefence.instances.Game;
import com.ja90n.blockdefence.towers.ArcherTower;
import com.ja90n.blockdefence.towers.CannonTower;
import com.ja90n.blockdefence.towers.PatrolTower;
import com.ja90n.blockdefence.towers.Tower;
import com.ja90n.blockdefence.util.ItemStackGenerator;
import com.ja90n.blockdefence.util.PathGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
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
    private HashMap<UUID,Tower> clickedTower;

    public InteractEvent(BlockDefence blockDefence){
        this.blockDefence = blockDefence;
        clickedLocation = new HashMap<>();
        clickedTower = new HashMap<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (blockDefence.getArenaManager().getArena(event.getPlayer()) == null){
            return;
        }
        if (!blockDefence.getArenaManager().getArena(event.getPlayer()).getGameState().equals(GameState.LIVE)){
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (blockDefence.getArenaManager().getArena(event.getPlayer()).getGame().getTowerManager()
                    .getTower(event.getClickedBlock().getLocation().add(0.5,1,0.5)) == null){

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
        Player player = event.getPlayer();
        Arena arena = blockDefence.getArenaManager().getArena(player);
        if (arena == null){
            return;
        }
        if (!arena.getGameState().equals(GameState.LIVE)){
            return;
        }
        if (event.getRightClicked() instanceof ArmorStand){
            Game game = arena.getGame();
            Tower tower = game.getTowerManager().getTower(event.getRightClicked());
            Enemy enemy = game.getEnemyManager().getEnemy((ArmorStand) event.getRightClicked());
            if (tower != null){
                event.setCancelled(true);
                if (tower.getTowerMenu() != null){
                    clickedTower.put(player.getUniqueId(),tower);
                    player.openInventory(tower.getTowerMenu());

                }
            } else if (enemy != null){
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Health of enemy: " + ChatColor.WHITE + enemy.getHealth());
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() != null){
            Game game = blockDefence.getArenaManager().getArena(player).getGame();
            if (event.getView().getTitle().equals(ChatColor.BLUE + "Place menu")){
                event.setCancelled(true);
                switch (event.getSlot()){
                    case 0:
                        game.getTowerManager().addTower(new ArcherTower(clickedLocation.get(event.getWhoClicked().getUniqueId()), game));
                        break;
                    case 1:
                        game.getTowerManager().addTower(new PatrolTower(clickedLocation.get(event.getWhoClicked().getUniqueId()), game));
                        break;
                    case 2:
                        game.getTowerManager().addTower(new CannonTower(clickedLocation.get(event.getWhoClicked().getUniqueId()), game));
                        break;
                }
                event.getWhoClicked().closeInventory();
                clickedLocation.remove(event.getWhoClicked().getUniqueId());
            } else if (event.getView().getTitle().equals(ChatColor.BLUE + "Summon menu")){
                event.setCancelled(true);
                switch (event.getSlot()){
                    case 0:
                        game.getEnemyManager().addEnemy(new Zombie(game,new PathGenerator(blockDefence).getPathLocation("0")));
                        break;
                    case 1:
                        game.getEnemyManager().addEnemy(new Smiler(game,new PathGenerator(blockDefence).getPathLocation("0")));
                        break;
                }
            } else if (clickedTower.containsKey(player.getUniqueId())){
                if (event.getInventory().equals(clickedTower.get(player.getUniqueId()).getTowerMenu())){
                    event.setCancelled(true);
                    Tower tower = clickedTower.get(player.getUniqueId());
                    switch (event.getSlot()){
                        case 22:
                            // Upgrades tower
                            tower.upgrade();
                            player.closeInventory();
                            player.openInventory(tower.getTowerMenu());
                            break;
                        case 34:
                            // Sells tower
                            game.getCoins().put(player.getUniqueId(),game.getCoins().get(player.getUniqueId()) + tower.getTotalValue());
                            tower.remove();
                            player.closeInventory();
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        clickedLocation.remove(event.getPlayer().getUniqueId());
        clickedTower.remove(event.getPlayer().getUniqueId());
    }
}
