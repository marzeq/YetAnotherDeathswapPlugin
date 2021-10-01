package me.marzeq.deathswap.util;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Util {
    public static boolean hasPermission(Player player, String permission) {
        return player.hasPermission("ds." + permission) || player.hasPermission("ds.*") || player.hasPermission("*") || player.isOp();
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        return sender.hasPermission("ds." + permission) || sender.hasPermission("ds.*") || sender.hasPermission("*") || sender.isOp();
    }

    public static final String permissionErrorMessage = "§cYou don't have permission to use this command!";

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static Location locationAt(int x, int z, World world) {
        Block feet = world.getHighestBlockAt(x, z);
        
        if (!feet.getType().isEmpty() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isEmpty())
            return null;
        else if (!feet.getRelative(BlockFace.UP).getType().isEmpty())
            return null;
        else if (!feet.getRelative(BlockFace.DOWN).getType().isSolid())
            return null;
        feet.getLocation().setY(feet.getY() + 1);
        return feet.getLocation();
    }

    public static void sendMessageToPlayersInList(List<Player> players, String message) {
        for (Player player : players)
            player.sendMessage(message);
    }
}
