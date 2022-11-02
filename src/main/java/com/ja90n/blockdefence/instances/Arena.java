package com.ja90n.blockdefence.instances;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enums.GameState;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {

    private BlockDefence blockDefence;
    private int id;
    private String name;
    private List<UUID> players;
    private Game game;
    private GameState gameState;

    public Arena(BlockDefence blockDefence, int id, String name) {
        this.blockDefence = blockDefence;
        this.id = id;
        this.name = name;
        this.game = new Game(blockDefence,this);
        this.gameState = GameState.RECRUITING;

        players = new ArrayList<>();
    }

    public void startGame(){ game.start();}

    public void stopGame(){
        if (gameState.equals(GameState.LIVE)){
            for (UUID uuid : players){
                Player player = Bukkit.getPlayer(uuid);
                player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                player.getEnderChest().clear();
                player.setInvisible(false);
                player.setInvulnerable(false);
                player.setAllowFlight(false);
                player.setFlying(false);
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                player.removePotionEffect(PotionEffectType.WEAKNESS);
            }
            players.clear();
        }
    }

    // Player management
    public void addPlayer(Player player){
        players.add(player.getUniqueId());
        player.setGameMode(GameMode.ADVENTURE);
    }

    public void removePlayer(Player player){
        players.remove(player.getUniqueId());
        player.getInventory().clear();
        player.getEnderChest().clear();
        player.setInvisible(false);
        player.setInvulnerable(false);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.setGameMode(GameMode.ADVENTURE);
        player.sendTitle(" ", " ");
    }

    public void sendMessage(String message){
        for (UUID uuid : getPlayers()){
            Bukkit.getPlayer(uuid).sendMessage(message);
        }
    }

    public void sendTitle(String title1, String title2){
        for (UUID uuid : getPlayers()){
            Bukkit.getPlayer(uuid).sendTitle(title1,title2);
        }
    }


    // Getters
    public List<UUID> getPlayers() {
        return players;
    }
    // Setters
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }
}