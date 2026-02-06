package dev.xf3d3.ultimateteams.commands.subCommands;

import de.themoep.minedown.adventure.MineDown;
import dev.xf3d3.ultimateteams.UltimateTeams;
import dev.xf3d3.ultimateteams.utils.Utils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TeamListSubCommand {

    private final UltimateTeams plugin;

    public TeamListSubCommand(@NotNull UltimateTeams plugin) {
        this.plugin = plugin;
    }

    public void teamListSubCommand(CommandSender sender) {

        if (plugin.getTeamStorageUtil().getTeams().isEmpty()) {
            sender.sendMessage(
                    MineDown.parse(plugin.getMessages().getNoTeamsToList())
            );
            return;
        }

        StringBuilder builder = new StringBuilder();

        // Header
        builder.append(Utils.Color(plugin.getMessages().getTeamsListHeader()))
                .append("\n");

        plugin.getTeamStorageUtil()
                .getTeams()
                .forEach(team -> {
                    int online = team.getOnlineMembers().size();
                    int total = team.getMembers().size();

                    String prefix = team.getPrefix();
                    if (prefix == null) prefix = "";

                    builder.append(Utils.Color(
                            "#07DBF2 • &f" + team.getName()
                                    + (prefix.isEmpty() ? "" : " &7(" + prefix + "&7)")
                                    + " &7(" + online + "/" + total + ")"
                    )).append("\n");

                });

        // Footer
        builder.append(Utils.Color(plugin.getMessages().getTeamsListFooter()));

        sender.sendMessage(builder.toString());
    }
}
