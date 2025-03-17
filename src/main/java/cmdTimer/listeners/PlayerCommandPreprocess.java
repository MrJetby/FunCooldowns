package cmdTimer.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

import static me.jetby.cmdTimer.manager.Actions.execute;
import static me.jetby.cmdTimer.manager.Timer.*;
import static me.jetby.cmdTimer.utils.Config.CFG;

public class PlayerCommandPreprocess implements Listener {
    private List<String> commands;

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().substring(1).toLowerCase(); // убираем слэш для чистоты строки
        Player p = e.getPlayer();


        if (command.equalsIgnoreCase(CFG().getString("cancel-command", "cancelteleport"))) {
            if (activeTimers.containsKey(p.getUniqueId())) {
                cancelTimer(p);
                return;
            } else {
                p.sendMessage(CFG().getString("messages.nothing").replace('&', '§'));

            }
            return;
        }


        commands = CFG().getStringList("commands");

        if (command.equalsIgnoreCase(CFG().getString("cancel-command"))) {
            cancelTimer(p);
            return;
        }
        boolean shouldDelay = commands.stream().anyMatch(configCommand -> {
            if (configCommand.endsWith("!")) {
                return command.startsWith(configCommand.replace("!", "")); // проверка на команду с аргументом
            }
            return command.equals(configCommand);
        });


        if (shouldDelay) {

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

