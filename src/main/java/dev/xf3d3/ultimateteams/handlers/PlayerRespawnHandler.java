package dev.xf3d3.ultimateteams.handlers;

import dev.xf3d3.ultimateteams.UltimateTeams;
import dev.xf3d3.ultimateteams.models.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayerRespawnHandler implements Listener {

    private final UltimateTeams plugin;

    public PlayerRespawnHandler(@NotNull UltimateTeams plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRespawn(PlayerRespawnEvent event) {
        boolean respawnEnabled = plugin.getSettings().isTeamHomeRespawnEnabled();
        if (!respawnEnabled) return;

        Player player = event.getPlayer();
        Optional<Team> team = plugin.getTeamStorageUtil().findTeamByMember(player.getUniqueId());
        if (team.isPresent()) {
            Location location = team.get().getHome().getLocation();
            event.setRespawnLocation(location);
        }
    }
}
