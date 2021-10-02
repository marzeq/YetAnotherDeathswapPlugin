package me.marzeq.deathswap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.marzeq.deathswap.Deathswap;
import me.marzeq.deathswap.util.PlayerUtils;

public class Stop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }
        Player player = (Player) sender;

        if (PlayerUtils.hasPermission(player, "stop")) {
            player.sendMessage("§eStopping the game...");
        } else {
            player.sendMessage(PlayerUtils.permissionErrorMessage);
            return true;
        }

        if (!Deathswap.plugin().game.started) {
            player.sendMessage("§cNo game running!");
            return true;
        }

        Deathswap.plugin().game.endGame(false);
        player.sendMessage("§aGame stopped!");

        return true;
    }
}
