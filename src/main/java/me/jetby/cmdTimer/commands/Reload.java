package me.jetby.cmdTimer.commands;

import me.jetby.cmdTimer.utils.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static me.jetby.cmdTimer.Main.getInstance;
import static me.jetby.cmdTimer.utils.Config.CFG;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender.hasPermission("cmdtimer.reload")) {
            Config cfg = new Config();
            cfg.reloadCfg(getInstance());
            commandSender.sendMessage(CFG().getString("messages.reload").replace('&', '§'));
        } else {
            commandSender.sendMessage(CFG().getString("messages.noperm").replace('&', '§'));

        }
        return false;
    }
}
