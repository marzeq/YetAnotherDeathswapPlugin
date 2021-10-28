package me.marzeq.deathswap.util;

import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class TeleportUtils {
    public static HashSet<Material> bad_blocks = new HashSet<>();

    static {
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.FIRE);
        bad_blocks.add(Material.CACTUS);
    }

    public static Location generateLocation(Player player) {
        int worldBorderLenEachDirection = (int) player.getWorld().getWorldBorder().getSize() / 2,
                worldBorderOffsetX = (int) player.getWorld().getWorldBorder().getCenter().getX(),
                worldBorderOffsetZ = (int) player.getWorld().getWorldBorder().getCenter().getZ();


        int x = PlayerUtils.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection) - worldBorderOffsetX;
        int z = PlayerUtils.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection) - worldBorderOffsetZ;
        int y = 256;

        Location randomLocation = new Location(player.getWorld(), x, y, z);
        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        randomLocation.setY(y);

        return randomLocation;
    }

    public static Location findSafeLocation(Player player) {

        Location randomLocation = generateLocation(player);

        while (!isLocationSafe(randomLocation)) {
            randomLocation = generateLocation(player);
        }
        return randomLocation;
    }

    public static boolean isLocationSafe(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);

        return !(bad_blocks.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid());
    }
}
