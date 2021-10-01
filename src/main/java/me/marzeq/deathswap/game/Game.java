package me.marzeq.deathswap.game;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Sound;

import me.marzeq.deathswap.Deathswap;
import me.marzeq.deathswap.util.PlayerUtils;
import me.marzeq.deathswap.util.TeleportUtils;

public class Game {
    public ArrayList<Player> players = new ArrayList<Player>();
    public boolean started = false;
    private int timer = 10;
    int taskID;
    boolean runningCountdown = false;

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
            PlayerUtils.sendMessageToPlayersInList(players, "§cNot enough players to start the game.");
            players.clear();
            return false;
        }
        
        for (Player player : players) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 255));
            player.teleport(TeleportUtils.findSafeLocation(player));
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
        
        started = true;
        PlayerUtils.sendMessageToPlayersInList(players, "§aGame started!");

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Deathswap.plugin(), () -> {
            swap(minTime, maxTime);
        }, PlayerUtils.getRandomNumber(minTime, maxTime)*20);

        return true;
    }

    public Player endGame(boolean announceWinner) {
        started = false;
        Player winner = players.get(0);
        players.clear();
        if (announceWinner) {
            winner.sendMessage("§aYou won the game!");
            PlayerUtils.sendMessageToPlayersInList((List<Player>) Bukkit.getOnlinePlayers(), "§b" + winner.getName() + "§a has won the game!");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, .5f, 1);
            }
            return winner;
        }
        return null;
    }

    private void swap(int minTime, int maxTime) {
        if (!started)
            return;

        taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Deathswap.plugin(), () -> {
            if (!started || runningCountdown) {
                Bukkit.getServer().getScheduler().cancelTask(taskID);
                return;
            }
            runningCountdown = true;
            if (timer > 0) {
                PlayerUtils.sendMessageToPlayersInList(players, "§cSwapping in §e" + timer + "§c seconds...");
                timer--;
            }
            else {
                PlayerUtils.sendMessageToPlayersInList(players, "§cSwapping!");

                Location player1Pos = players.get(0).getLocation();
                for (int i = 0; i < players.size(); i++) {
                    Player player = players.get(i);
                    if (i == players.size() - 1) {
                        player.teleport(player1Pos);
                        break;
                    }
                    Location nextPlayerPos = players.get(i + 1).getLocation();
                    player.teleport(nextPlayerPos);
                }

                timer = 10;
                runningCountdown = false;
                Bukkit.getServer().getScheduler().cancelTask(taskID);
            }
        }, 0, 20);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Deathswap.plugin(), () -> {
            swap(minTime, maxTime);
        }, PlayerUtils.getRandomNumber(minTime, maxTime)*20+200);
    }
}
