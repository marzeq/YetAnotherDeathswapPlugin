package me.marzeq.deathswap;

import org.bukkit.plugin.java.JavaPlugin;

import me.marzeq.deathswap.game.Game;

public final class Deathswap extends JavaPlugin {

    private static Deathswap plugin;
    public Game game = new Game();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
    }

    public static Deathswap plugin() {
        return plugin;
    }
}
