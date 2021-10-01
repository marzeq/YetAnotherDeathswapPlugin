package me.marzeq.deathswap.events;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.marzeq.deathswap.Deathswap;

public class PlayerDeath implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!Deathswap.plugin().game.started)
            return;

        if (Deathswap.plugin().game.players.contains(event.getEntity()))
            Deathswap.plugin().game.players.remove(event.getEntity());
        
        if (Deathswap.plugin().game.players.size() == 1)
            Deathswap.plugin().game.endGame(true);
    }
}
