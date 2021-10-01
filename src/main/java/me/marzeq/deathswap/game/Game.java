package me.marzeq.deathswap.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.SECONDS;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Sound;

import me.marzeq.deathswap.Deathswap;
import me.marzeq.deathswap.util.Util;

public class Game {
    public ArrayList<Player> players = new ArrayList<Player>();
    public boolean started = false;

    public boolean start() {
        World world = Bukkit.getWorlds().get(0);
        int worldBorderLenEachDirection = (int) world.getWorldBorder().getSize() / 2;
        boolean includeOps = Deathswap.plugin().getConfig().getBoolean("include-ops");
        int minTime = Deathswap.plugin().getConfig().getInt("min-time");
        int maxTime = Deathswap.plugin().getConfig().getInt("max-time");
        int immunityAfterSpawn = Deathswap.plugin().getConfig().getInt("immunity-after-spawn");
        int fireResistanceAfterSpawn = Deathswap.plugin().getConfig().getInt("fire-resistance-after-spawn");

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!includeOps) {
                if (player.isOp())
                    continue;
            }
            else {
                players.add(player);
                player.sendMessage("§eYou were added as a death swap player, game starting soon...");
            }
        }

        if (players.size() % 2 != 0 || players.size() < 2) {
            Util.sendMessageToPlayersInList(players, "§cNot enough players to start the game.");
            players.clear();
            return false;
        }
        
        for (Player player : players) {
            Location location = Util.locationAt(Util.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection), Util.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection), world);
            while (location == null)
                location = Util.locationAt(Util.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection), Util.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection), world);
            
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 255));
            player.teleport(location);
        }

        for (Player player : players) {
            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setFallDistance(0);
            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, immunityAfterSpawn * 20, Integer.MAX_VALUE));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, fireResistanceAfterSpawn * 20, Integer.MAX_VALUE));
        }

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(new Runnable(){
            public void run() {
                swap(minTime, maxTime);
            }
        }, Util.getRandomNumber(minTime, maxTime), SECONDS);
        
        started = true;
        Util.sendMessageToPlayersInList(players, "§aGame started!");
        return true;
    }

    public Player endGame(boolean announceWinner) {
        started = false;
        Player winner = players.get(0);
        players.clear();
        if (announceWinner) {
            winner.sendMessage("§aYou won the game!");
            Util.sendMessageToPlayersInList((List<Player>) Bukkit.getOnlinePlayers(), "§b" + winner.getName() + "§a has won the game!");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
            }
            return winner;
        }
        return null;
    }

    private void swap(int minTime, int maxTime) {
        if (!started)
            return;
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(new Runnable() {
            int countdownStarter = 10;

            public void run() {
                if (countdownStarter > 0) {
                    Util.sendMessageToPlayersInList(players, "§cSwapping in §e" + countdownStarter + "§c seconds...");
                    countdownStarter--;
                }
                else {
                    Util.sendMessageToPlayersInList(players, "§cSwapping!");

                    Collections.shuffle(players);

                    double[] player1Pos = {players.get(0).getLocation().getX(), players.get(0).getLocation().getY(), players.get(0).getLocation().getZ()};
                    for (int i = 0; i < players.size(); i++) {
                        Player player = players.get(i);
                        if (i + 1 == players.size()) {
                            player.getLocation().set(player1Pos[0], player1Pos[1], player1Pos[2]);
                            break;
                        }
                        double[] nextPlayerPos = {players.get(i + 1).getLocation().getX(), players.get(i + 1).getLocation().getY(), players.get(i + 1).getLocation().getZ()};
                        player.getLocation().set(nextPlayerPos[0], nextPlayerPos[1], nextPlayerPos[2]);
                    }
                }
            }
        }, 0, 1, SECONDS);

        scheduler2.schedule(new Runnable(){
            public void run() {
                swap(minTime, maxTime);
            }
        }, Util.getRandomNumber(minTime, maxTime) + 10, SECONDS);
    }
}
