package me.marzeq.deathswap.commands;

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
            player.sendMessage("§eStarting the game...");
        }
        else {
            player.sendMessage(Util.permissionErrorMessage);
            return true;
        }

        if (Deathswap.plugin().game.started) {
            player.sendMessage("§cGame has already started!");
            return true;
        }
        else if (!Deathswap.plugin().game.start()) {
            player.sendMessage("§cNumber of players must be bigger or equal to 2 and a multiple of 2!");
            return true;
        }

        return true;
    }
}
