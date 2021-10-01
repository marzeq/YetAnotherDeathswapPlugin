package me.marzeq.deathswap.util;

import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
 
import java.util.HashSet;

public class TeleportUtils {
    //List of all the unsafe blocks
    public static HashSet<Material> bad_blocks = new HashSet<>();
 
    static {
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.FIRE);
        bad_blocks.add(Material.CACTUS);
    }
 
    public static Location generateLocation(Player player){
        int worldBorderLenEachDirection = (int) player.getWorld().getWorldBorder().getSize() / 2;
        
        int x = PlayerUtils.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection);
        int z = PlayerUtils.getRandomNumber(-worldBorderLenEachDirection, worldBorderLenEachDirection);
        int y = 150;
 
        Location randomLocation = new Location(player.getWorld(), x, y, z);
        y = randomLocation.getWorld().getHighestBlockYAt(randomLocation);
        randomLocation.setY(y);
 
        return randomLocation;
    }
 
    public static Location findSafeLocation(Player player){
 
        Location randomLocation = generateLocation(player);
 
        while (!isLocationSafe(randomLocation)) {
            randomLocation = generateLocation(player);
        }
        return randomLocation;
    }
 
    public static boolean isLocationSafe(Location location){
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        //Get instances of the blocks around where the player would spawn
        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);
 
        //Check to see if the surroundings are safe or not
        return !(bad_blocks.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid());
    }
}