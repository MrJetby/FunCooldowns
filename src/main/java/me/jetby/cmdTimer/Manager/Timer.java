package me.jetby.cmdTimer.Manager;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.jetby.cmdTimer.Main.*;
import static me.jetby.cmdTimer.Manager.Listeners.Actions;

public class Timer {

    public static Map<Player, BukkitRunnable> activeTimers = new HashMap<>();
    public static Map<Player, BossBar> playerBossBars = new HashMap<>();
    public static Map<Player, Integer> playerCountdowns = new HashMap<>();

    public static void startTimer(Player player, String command, int time) {
        BossBar bossBar = Bukkit.createBossBar(
                cfg.getString("BossBar.countdown").replace('&', '§'),
                BarColor.valueOf(cfg.getString("BossBar.Color")),
                BarStyle.valueOf(cfg.getString("BossBar.Style")),
                BarFlag.PLAY_BOSS_MUSIC
        );
        bossBar.addPlayer(player);
        playerBossBars.put(player, bossBar);

//        int countdown = time;
        playerCountdowns.put(player, time); // вместо time было countdown

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                int currentCountdown = playerCountdowns.get(player);
                if (currentCountdown > 0) {
                    double progress = currentCountdown / (double) time;
                    bossBar.setProgress(progress);
                    bossBar.setTitle(cfg.getString("BossBar.countdown").replace('&', '§').replace("{timer}", String.valueOf(currentCountdown)));
                    playerCountdowns.put(player, currentCountdown - 1);

                } else {
                    bossBar.removeAll();
                    activeTimers.remove(player);
                    playerBossBars.remove(player);
                    playerCountdowns.remove(player);
                    Bukkit.dispatchCommand(player, command);
                    cancel();
                    List<String> actions = cfg.getStringList("actions.end");
                    for (String action : actions) {
                        Actions(player, action.replace("%time%", String.valueOf(time)));
                    }
                }
            }
        };
        task.runTaskTimer(getInstance(), 0, 20);
        activeTimers.put(player, task);
    }

    public static void cancelTimer(Player player) {
        if (activeTimers.containsKey(player)) {
            activeTimers.get(player).cancel();
            activeTimers.remove(player);

            if (playerBossBars.containsKey(player)) {
                playerBossBars.get(player).removeAll();
                playerBossBars.remove(player);
            }

            playerCountdowns.remove(player);

            List<String> actions = cfg.getStringList("actions.cancel");
            for (String action : actions) {
                Actions(player, action);
            }

        }
    }
}
