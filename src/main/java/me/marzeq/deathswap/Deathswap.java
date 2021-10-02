package me.marzeq.deathswap;

import org.bukkit.plugin.java.JavaPlugin;

import me.marzeq.deathswap.commands.Reload;
import me.marzeq.deathswap.commands.Start;
import me.marzeq.deathswap.commands.Stop;
import me.marzeq.deathswap.events.PlayerDeath;
import me.marzeq.deathswap.game.Game;

public final class Deathswap extends JavaPlugin {

    private static Deathswap plugin;
    public Game game = new Game();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("dsstart").setExecutor(new Start());
        getCommand("dsstop").setExecutor(new Stop());
        getCommand("dsreload").setExecutor(new Reload());
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        plugin = this;
    }

    public static Deathswap plugin() {
        return plugin;
    }
}
