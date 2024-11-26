package me.jetby.cmdTimer.Events;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

import static me.jetby.cmdTimer.Main.cfg;
import static me.jetby.cmdTimer.Manager.Timer.cancelTimer;

public class EntityDamageEvent implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        // Проверяем, что пострадавший - игрок
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            List<String> DisabledWorlds = cfg.getStringList("Damage-Disabled-Worlds");

            World world = player.getWorld();
            if (!(DisabledWorlds.contains(world.getName()))) {
                if (cfg.getBoolean("CancelOnPlayerDamage")) {
                    if (event.getDamager().getType() == EntityType.PLAYER) {
                        cancelTimer(player);
                    }
                }

                if (cfg.getBoolean("CancelOnMobDamage")) {
                    if (event.getDamager().getType().isAlive()) {
                        cancelTimer(player);
                    }
                }
            }

        }
    }
}
