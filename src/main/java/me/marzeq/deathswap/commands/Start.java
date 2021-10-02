package me.marzeq.deathswap.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.marzeq.deathswap.Deathswap;
import me.marzeq.deathswap.util.PlayerUtils;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (PlayerUtils.hasPermission(player, "start")) {
            player.sendMessage("§eStarting the game...");
        } else {
            player.sendMessage(PlayerUtils.permissionErrorMessage);
            return true;
        }

        if (Deathswap.plugin().game.started) {
            player.sendMessage("§cGame has already started!");
            return true;
        } else {
            if (args.length > 0) {
                ArrayList<Player> argPlayers = new ArrayList<Player>();
                
                for (String arg : args) {
                    Player argPlayer = Deathswap.plugin().getServer().getPlayer(arg);
                    if (argPlayer != null) {
                        if (player.isOnline()) {
                            argPlayers.add(argPlayer);
                        } else {
                            player.sendMessage("§c" + argPlayer.getName() + " is not online!");
                        }
                    } else {
                        player.sendMessage("§c" + arg + " is not a real player!");
                    }
                }

                if (!Deathswap.plugin().game.start(argPlayers)) {
                    player.sendMessage("§cNumber of players must be bigger than 1!");
                    return true;
                }
            } else {
                if (!Deathswap.plugin().game.start()) {
                    player.sendMessage("§cNumber of players must be bigger than 1!");
                    return true;
                }
            }
        }

        player.sendMessage("§aStarted the game!");

        return true;
    }
}
