package dev.xf3d3.ultimateteams.handlers;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.common.location.ApolloBlockLocation;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import com.lunarclient.apollo.module.waypoint.WaypointModule;
import com.lunarclient.apollo.player.ApolloPlayer;
import dev.xf3d3.ultimateteams.UltimateTeams;
import dev.xf3d3.ultimateteams.api.events.*;
import dev.xf3d3.ultimateteams.models.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.util.Optional;

public class PlayerWaypointHandler implements Listener {

    private final UltimateTeams plugin;
    private static final String WAYPOINT_NAME = "Team";

    public PlayerWaypointHandler(UltimateTeams plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        handleWaypointPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        handleWaypointPlayer(event.getPlayer());
    }

    @EventHandler
    public void onTeamHomeCreate(TeamHomeCreateEvent event) {
        Team team = event.getTeam();
        handleWaypointTeamPlayers(team);
    }

    @EventHandler
    public void onTeamHomeDelete(TeamHomeDeleteEvent event) {
        Team team = event.getTeam();
        handleWaypointTeamPlayers(team);
    }

    @EventHandler
    public void onTeamDisband(TeamDisbandEvent event) {
        Team team = event.getTeam();
        handleWaypointTeamPlayers(team);
    }

    @EventHandler
    public void onTeamMemberJoin(TeamMemberJoinEvent event) {
        Player player = event.getNewMember();
        handleWaypointPlayer(player);
    }

    @EventHandler
    public void onTeamMemberLeave(TeamMemberLeaveEvent event) {
        Player player = Bukkit.getPlayer(event.getOldMember());
        if (player != null && player.isOnline()) {
            handleWaypointPlayer(player);
        }
    }

    private void handleWaypointTeamPlayers(Team team) {
        for (Player player : team.getOnlineMembers()) {
            handleWaypointPlayer(player);
        }
    }

    private void handleWaypointPlayer(Player viewer) {
        Optional<Team> team = plugin.getTeamStorageUtil().findTeamByMember(viewer.getUniqueId());
        Location teamHomeLocation = team.get().getHome().getLocation();

        Optional<ApolloPlayer> apolloPlayerOpt = Apollo.getPlayerManager().getPlayer(viewer.getUniqueId());
        WaypointModule waypointModule = Apollo.getModuleManager().getModule(WaypointModule.class);
        apolloPlayerOpt.ifPresent(apolloPlayer -> {
            if (teamHomeLocation == null) {
                waypointModule.removeWaypoint(apolloPlayer, WAYPOINT_NAME);
                return;
            }
            waypointModule.displayWaypoint(apolloPlayer, Waypoint.builder()
                    .name(WAYPOINT_NAME)
                    .location(ApolloBlockLocation.builder()
                            .world(teamHomeLocation.getWorld().getName())
                            .x(teamHomeLocation.getBlockX())
                            .y(teamHomeLocation.getBlockY())
                            .z(teamHomeLocation.getBlockZ())
                            .build()
                    )
                    .color(Color.GREEN)
                    .preventRemoval(false)
                    .hidden(false)
                    .build());
        });
    }
}
