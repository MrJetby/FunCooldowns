package me.jetby.cmdTimer.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

import static me.jetby.cmdTimer.manager.Actions.execute;
import static me.jetby.cmdTimer.manager.Timer.*;
import static me.jetby.cmdTimer.utils.Config.CFG;

public class PlayerCommandPreprocess implements Listener {
    private List<String> commands = CFG().getStringList("commands");

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().substring(1).toLowerCase(); // убираем слэш для чистоты строки
        Player p = e.getPlayer();


        if (command.equalsIgnoreCase(CFG().getString("cancel-command", "/cancelteleport").substring(1).toLowerCase())) {
            if (activeTimers.containsKey(p.getUniqueId())) {
                cancelTimer(p);
            } else {
                p.sendMessage(CFG().getString("messages.nothing").replace('&', '§'));
            }
            e.setCancelled(true);
            return;
        }

        if (command.equalsIgnoreCase(CFG().getString("cancel-command"))) {
            cancelTimer(p);
            return;
        }

        String[] commandParts = command.split(" ");
        String baseCommand = commandParts[0];

        boolean shouldDelay = commands.stream().anyMatch(configCommand -> {
            if (configCommand.endsWith("!")) {
                return baseCommand.equalsIgnoreCase(configCommand.replace("!", ""));
            }
            return baseCommand.equalsIgnoreCase(configCommand);
        });



        if (shouldDelay) {


            List<String> DisabledWorlds = CFG().getStringList("disabled-worlds");
            if (!DisabledWorlds.isEmpty()) {
                for (String world : DisabledWorlds) {
                    if (p.getWorld().getName().equalsIgnoreCase(world)) {
                        return;
                    }
                }
            }



            // Если у игрока уже есть активный таймер, отменяем его и перезапускаем
            if (activeTimers.containsKey(e.getPlayer().getUniqueId())) {
                cancelTimer(e.getPlayer());
            }
            int time = CFG().getInt("Timer.default");
            for (String perm : CFG().getConfigurationSection("Timer").getKeys(false)) {
                if (p.hasPermission("funcooldowns." + perm)) {
                    time = CFG().getInt("Timer." + perm);
                    break;
                }
            }
            if (time!=0) {
                e.setCancelled(true);
                List<String> actions = CFG().getStringList("actions.start");
                for (String action : actions) {
                    execute(p, action.replace("%time%", String.valueOf(time)));
                }
                startTimer(e.getPlayer(), command, time);
            }
        }
    }

    }

