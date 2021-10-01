package me.marzeq.deathswap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.marzeq.deathswap.Deathswap;
import me.marzeq.deathswap.util.Util;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (Util.hasPermission(sender, "reload")) {
            sender.sendMessage("§aReloaded config!");
            Deathswap.plugin().reloadConfig();
        }
        else {
            sender.sendMessage(Util.permissionErrorMessage);
        }

        return true;
    }
}
