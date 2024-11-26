package me.jetby.cmdTimer.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.jetby.cmdTimer.Main.cfg;
import static me.jetby.cmdTimer.Main.getInstance;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender.hasPermission("cmdtimer.reload")) {
            getInstance().cfgReload();
            commandSender.sendMessage(cfg.getString("messages.reload").replace('&', '§'));
        } else {
            commandSender.sendMessage(cfg.getString("messages.noperm").replace('&', '§'));

        }
        return false;
    }
}
