package me.marzeq.deathswap.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.SECONDS;


import org.bukkit.Bukkit;
import org.bukkit.Color;
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
            if (!includeOps && player.isOp()) 
                continue;
            else {
                players.add(player);
                player.sendMessage(Color.ORANGE + "Added as a death swap player, game starting soon...");
            }
        }

        if (players.size() % 2 != 2) {
            players.clear();
            Util.sendMessageToPlayersInList(players, Color.RED + "Not enough players to start the game.");
            return false;
        }
        
        for (Player player : players) {
            Location location = Util.locationAt(Util.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection), Util.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection), world);
            while (location == null)
                location = Util.locationAt(Util.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection), Util.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection), world);
            
            player.setWalkSpeed(0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 999999));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 999999));
            player.teleport(location);
        }

        for (Player player : players) {
            player.setWalkSpeed(1);
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
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, immunityAfterSpawn));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, fireResistanceAfterSpawn));
        }

        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(new Runnable(){
            public void run() {
                swap(minTime, maxTime);
            }
        }, Util.getRandomNumber(minTime, maxTime), SECONDS);
        
        started = true;
        return true;
    }

    public Player endGame(boolean announceWinner) {
        started = false;
        players.clear();
        if (announceWinner) {
            Player winner = players.get(0);
            winner.sendMessage(Color.GREEN + "You won the game!");
            Util.sendMessageToPlayersInList((List<Player>) Bukkit.getOnlinePlayers(), Color.LIME + winner.getName() + " has won the game!");
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
                    Util.sendMessageToPlayersInList(players, Color.RED + "Swapping in " + Color.YELLOW + countdownStarter + Color.RED + " seconds...");
                    countdownStarter--;
                }
                else {
                    Util.sendMessageToPlayersInList(players, Color.RED + "Swapping!");

                    Collections.shuffle(players);
                    for (int i = 0; i < players.size() / 2; i++) {
                        players.get(i).teleport(players.get(players.size() - i).getLocation());
                        players.get(players.size() - i).teleport(players.get(i).getLocation());
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
