package com.ja90n.blockdefence.command;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enums.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MainCommand implements CommandExecutor {

    private BlockDefence blockDefence;

    public MainCommand(BlockDefence blockDefence){
        this.blockDefence = blockDefence;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        switch (args.length) {
            case 0:
                Inventory inventory = Bukkit.createInventory(null, 18, ChatColor.BLUE + "Summon menu");
                inventory.setItem(0, new ItemStack(Material.ROTTEN_FLESH));
                inventory.setItem(1, new ItemStack(Material.RECOVERY_COMPASS));
                player.openInventory(inventory);
                break;
            case 1:
                switch (args[0]) {
                    case "join":
                        if (blockDefence.getArenaManager().getArena(player) != null) {
                            player.sendMessage("You are already in an arena");
                        } else {
                            if (blockDefence.getArenaManager().getArena(0).getGameState().equals(GameState.LIVE)){
                                player.sendMessage("Game is alreadt live");
                            } else {
                                blockDefence.getArenaManager().getArena(0).addPlayer(player);
                                player.sendMessage("added");
                            }
                        }
                        break;
                    case "start":
                        if (blockDefence.getArenaManager().getArena(player) != null) {
                            blockDefence.getArenaManager().getArena(player).startGame();
                        } else {
                            player.sendMessage("you are not in a game");
                        }
                        break;
                    case "leave":
                        if (blockDefence.getArenaManager().getArena(player) != null){
                            blockDefence.getArenaManager().getArena(player).removePlayer(player,false);
                        } else {
                            player.sendMessage("You are not in an arena");
                        }
                        break;
                    case "stop":
                        if (blockDefence.getArenaManager().getArena(player) != null){
                            blockDefence.getArenaManager().getArena(player).stopGame();
                        } else {
                            player.sendMessage("You are not in an arena");
                        }
                        break;
                    case "state":
                        player.sendMessage(blockDefence.getArenaManager().getArena(0).getGameState().name());
                }
        }
        return false;
    }
}
