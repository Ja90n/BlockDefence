package com.ja90n.blockdefence.instances;

import com.ja90n.blockdefence.BlockDefence;
import com.ja90n.blockdefence.enums.GameState;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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

    public void startGame(){
        game.start();
        sendMessage("game has been started");
    }

    public void stopGame(){
        if (gameState.equals(GameState.LIVE)){
            game.getGameRunnable().cancel();
            sendMessage("ggame is stopping");
            for (UUID uuid : players){
                removePlayer(Bukkit.getPlayer(uuid), true);
            }
            players.clear();
            gameState = GameState.RECRUITING;
            game.getTowerManager().clearTowers();
            game.getEnemyManager().clearEnemies();
            game = new Game(blockDefence,this);
        }
    }

    // Player management
    public void addPlayer(Player player){
        players.add(player.getUniqueId());
        player.setGameMode(GameMode.ADVENTURE);
    }

    public void removePlayer(Player player, boolean gameEnd){
        player.getInventory().clear();
        player.getEnderChest().clear();
        player.setInvisible(false);
        player.setInvulnerable(false);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGameMode(GameMode.ADVENTURE);
        player.sendTitle(" ", " ");
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(" "));

        if (!gameEnd){
            players.remove(player.getUniqueId());
            if (gameState.equals(GameState.LIVE)){
                game.getCoins().remove(player.getUniqueId());
                if (players.size() == 0){
                    stopGame();
                }
            }
        }
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

    public GameState getGameState() {
        return gameState;
    }
}