package me.marzeq.deathswap.commands;

import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.marzeq.deathswap.Deathswap;
import me.marzeq.deathswap.util.Util;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }
        Player player = (Player) sender;

        if (Util.hasPermission(player, "start")) {
            player.sendMessage(Color.YELLOW + "Starting game...");
        }
        else {
            player.sendMessage(Util.permissionErrorMessage);
            return true;
        }

        if (Deathswap.plugin().game.started) {
            player.sendMessage(Color.RED + "Game has already started!");
            return true;
        }
        else if (!Deathswap.plugin().game.start()) {
            player.sendMessage(Color.RED + "Number of players must be a multiple of 2!");
            return true;
        }

        return true;
    }
}
