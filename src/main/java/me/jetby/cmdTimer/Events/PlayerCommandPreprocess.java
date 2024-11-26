package me.jetby.cmdTimer.Events;



import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerCommandPreprocessEvent;


import java.util.List;

import static me.jetby.cmdTimer.Main.cfg;
import static me.jetby.cmdTimer.Manager.Listeners.Actions;
import static me.jetby.cmdTimer.Manager.Timer.*;

public class PlayerCommandPreprocess implements Listener {
    private List<String> commands;

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {

        Player p = e.getPlayer();

            commands = cfg.getStringList("commands");
            String command = e.getMessage().substring(1).toLowerCase(); // убираем слэш для чистоты строки

            // Проверяем, есть ли команда в конфиге
            boolean shouldDelay = commands.stream().anyMatch(configCommand -> {
                if (configCommand.endsWith("!")) {
                    return command.startsWith(configCommand.replace("!", "")); // проверка на команду с аргументом
                }
                return command.equals(configCommand);
            });


            if (shouldDelay) {
                e.setCancelled(true); // отменяем команду и запускаем отсчёт

                // Если у игрока уже есть активный таймер, отменяем его и перезапускаем
                if (activeTimers.containsKey(e.getPlayer())) {
                    cancelTimer(e.getPlayer());
                }
                int time = cfg.getInt("Timer.default");
                for (String perm : cfg.getConfigurationSection("Timer").getKeys(false)) {
                    if (p.hasPermission("cmdtimer." + perm)) {
                        time = cfg.getInt("Timer." + perm);
                        break;
                    }
                }
                // Получаем действия из конфига и выполняем их
                List<String> actions = cfg.getStringList("actions.start");
                for (String action : actions) {
                    Actions(p, action.replace("%time%", String.valueOf(time)));
                }
                startTimer(e.getPlayer(), command, time);
            }
        }
    }

