package me.marzeq.deathswap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.marzeq.deathswap.Deathswap;
import me.marzeq.deathswap.util.PlayerUtils;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (PlayerUtils.hasPermission(sender, "reload")) {
            sender.sendMessage("§aReloaded config!");
            Deathswap.plugin().reloadConfig();
        }
        else {
            sender.sendMessage(PlayerUtils.permissionErrorMessage);
        }

        return true;
    }
}
