package me.jetby.cmdTimer.manager;

import lombok.Getter;
import me.jetby.cmdTimer.Main;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.jetby.cmdTimer.utils.Config.CFG;

@Getter
public class Timer {

    private final Map<UUID, BukkitRunnable> activeTimers = new HashMap<>();
    private final Map<UUID, BossBar> playerBossBars = new HashMap<>();
    private final Map<UUID, Integer> playerCountdowns = new HashMap<>();


    private final Main plugin;
    public Timer(Main plugin) {
        this.plugin = plugin;
    }
    public void startTimer(Player player, String command, int time) {
        BossBar bossBar = Bukkit.createBossBar(
                CFG().getString("BossBar.countdown").replace('&', '§'),
                BarColor.valueOf(CFG().getString("BossBar.Color")),
                BarStyle.valueOf(CFG().getString("BossBar.Style"))
        );
        bossBar.addPlayer(player);
        playerBossBars.put(player.getUniqueId(), bossBar);

        playerCountdowns.put(player.getUniqueId(), time);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                int currentCountdown = playerCountdowns.get(player.getUniqueId());
                if (currentCountdown > 0) {
                    double progress = currentCountdown / (double) time;
                    bossBar.setProgress(progress);
                    bossBar.setTitle(CFG().getString("BossBar.countdown").replace('&', '§').replace("{timer}", String.valueOf(currentCountdown)));
                    playerCountdowns.put(player.getUniqueId(), currentCountdown - 1);

                } else {
                    bossBar.removeAll();
                    activeTimers.remove(player.getUniqueId());
                    playerBossBars.remove(player.getUniqueId());
                    playerCountdowns.remove(player.getUniqueId());
                    Bukkit.dispatchCommand(player, command);
                    cancel();
                    List<String> actions = CFG().getStringList("actions.end");
                    for (String action : actions) {
                        plugin.getActions().execute(player, action.replace("%time%", String.valueOf(time)));
                    }
                }
            }
        };
        task.runTaskTimer(plugin, 0, 20);
        activeTimers.put(player.getUniqueId(), task);
    }

    public void cancelTimer(Player player) {
        if (activeTimers.containsKey(player.getUniqueId())) {
            activeTimers.get(player.getUniqueId()).cancel();
            activeTimers.remove(player.getUniqueId());

            if (playerBossBars.containsKey(player.getUniqueId())) {
                playerBossBars.get(player.getUniqueId()).removeAll();
                playerBossBars.remove(player.getUniqueId());
            }

            playerCountdowns.remove(player.getUniqueId());

            List<String> actions = CFG().getStringList("actions.cancel");
            for (String action : actions) {
                plugin.getActions().execute(player, action);
            }

        }
    }
}