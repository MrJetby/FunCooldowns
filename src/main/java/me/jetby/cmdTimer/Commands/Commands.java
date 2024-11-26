package me.jetby.cmdTimer.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.jetby.cmdTimer.Main.cfg;
import static me.jetby.cmdTimer.Manager.Timer.*;
import static me.jetby.cmdTimer.Manager.Timer.activeTimers;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (strings.length==0) {
                if (activeTimers.containsKey(player)) {
                    cancelTimer(player);
                    return true;
                } else {
                    sender.sendMessage(cfg.getString("messages.nothing").replace('&', '§'));

                }

            }

        }
        return false;
    }
}
