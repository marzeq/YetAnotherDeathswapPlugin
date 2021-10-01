package me.marzeq.deathswap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import me.marzeq.deathswap.Deathswap;
import me.marzeq.deathswap.util.Util;

public class GetConfig implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cPlease provide the key.");
            return true;
        }
        if (Util.hasPermission(sender, "getconfig")) {
            sender.sendMessage(String.valueOf(Deathswap.plugin().getConfig().get(args[0])));
        }
        else {
            sender.sendMessage(Util.permissionErrorMessage);
        }

        return true;
    }
}
