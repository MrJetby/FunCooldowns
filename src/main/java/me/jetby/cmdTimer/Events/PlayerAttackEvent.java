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

public class PlayerAttackEvent implements Listener {

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        // Проверяем, что атакующий - игрок
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            List<String> DisabledWorlds = cfg.getStringList("Damage-Disabled-Worlds");

            World world = player.getWorld();
            if (!(DisabledWorlds.contains(world.getName()))) {
                if (cfg.getBoolean("CancelOnPlayerDamage")) {
                    if (event.getEntity().getType() == EntityType.PLAYER) {
                        cancelTimer(player);
                    }
                }

                if (cfg.getBoolean("CancelOnMobDamage")) {
                    if (event.getEntity().getType().isAlive()) {
                        cancelTimer(player);
                    }
                }
            }
        }
    }
}
