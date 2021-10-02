package me.marzeq.deathswap.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.marzeq.deathswap.Deathswap;
import me.marzeq.deathswap.util.PlayerUtils;

public class GetConfig implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cPlease provide the key.");
            return true;
        }
        if (PlayerUtils.hasPermission(sender, "getconfig")) {
            sender.sendMessage(String.valueOf(Deathswap.plugin().getConfig().get(args[0])));
        } else {
            sender.sendMessage(PlayerUtils.permissionErrorMessage);
        }

        return true;
    }
}
