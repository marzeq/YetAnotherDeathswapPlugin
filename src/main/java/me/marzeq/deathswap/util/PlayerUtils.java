package me.marzeq.deathswap.util;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUtils {
    public static boolean hasPermission(Player player, String permission) {
        return player.hasPermission("ds." + permission) || player.hasPermission("ds.*") || player.hasPermission("*") || player.isOp();
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission("ds." + permission) || sender.hasPermission("ds.*") || sender.hasPermission("*") || sender.isOp();
    }

    public static final String permissionErrorMessage = "§c§lYou don't have permission to use this command!";

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void sendMessageToPlayersInList(List<Player> players, String message) {
        for (Player player : players)
            player.sendMessage(message);
    }

    public static void sendMessageToPlayersInList(Collection<? extends Player> players, String message) {
        for (Player player : players)
            player.sendMessage(message);
    }
}
