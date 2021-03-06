package me.marzeq.deathswap.game;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
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

    public boolean start() {
        return start(new ArrayList<Player>());
    }

    public boolean start(ArrayList<Player> players) {
        boolean includeOps = Deathswap.plugin().getConfig().getBoolean("include-ops");
        int minTime = Deathswap.plugin().getConfig().getInt("min-time");
        int maxTime = Deathswap.plugin().getConfig().getInt("max-time");
        int resistanceAfterSpawn = Deathswap.plugin().getConfig().getInt("resistance-after-spawn");
        int fireResistanceAfterSpawn = Deathswap.plugin().getConfig().getInt("fire-resistance-after-spawn");

        this.players = players;

        if (players.size() == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!includeOps) {
                    if (player.isOp() || player.hasPermission("ds.op"))
                        continue;
                }
                players.add(player);
                player.sendMessage("§aYou were added as a death swap player. §eGame starting soon!");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }

        if (players.size() < 2) {
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
            player.setSaturation(5); // (from Minecraft wiki on saturaion) Its initial value on world creation or
                                     // respawn is 5.
            player.setFireTicks(0);
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setFallDistance(0);
            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
            player.addPotionEffect(
                    new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, resistanceAfterSpawn * 20, Integer.MAX_VALUE));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, fireResistanceAfterSpawn * 20,
                    Integer.MAX_VALUE));
        }

        started = true;
        PlayerUtils.sendMessageToPlayersInList(players, "§aGame started!");

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Deathswap.plugin(), () -> {
            swap(minTime, maxTime);
        }, PlayerUtils.getRandomNumber(minTime, maxTime) * 20);

        return true;
    }

    public Player endGame(boolean announceWinner) {
        started = false;
        Player winner = players.get(0);
        players.clear();
        if (announceWinner) {
            winner.sendMessage("§a§lYou won the game!");
            PlayerUtils.sendMessageToPlayersInList(Bukkit.getOnlinePlayers(),
                    "§b§l" + winner.getName() + "§a has won the game!");
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
            if (!started || timer < 0) {
                Bukkit.getServer().getScheduler().cancelTask(taskID);
                return;
            }
            if (timer > 0) {
                PlayerUtils.sendMessageToPlayersInList(players, "§cSwapping in §e§l" + timer + "§c seconds...");
                timer--;
            } else {
                PlayerUtils.sendMessageToPlayersInList(players, "§c§lSwapping!");

                Collections.shuffle(players);

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
                Bukkit.getServer().getScheduler().cancelTask(taskID);
            }
        }, 0, 20);

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Deathswap.plugin(), () -> {
            swap(minTime, maxTime);
        }, PlayerUtils.getRandomNumber(minTime, maxTime) * 20 + 200);
    }
}
