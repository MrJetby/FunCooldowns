package cmdTimer.listeners;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

import static me.jetby.cmdTimer.manager.Timer.cancelTimer;
import static me.jetby.cmdTimer.utils.Config.CFG;

public class PlayerAttackEvent implements Listener {

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            List<String> DisabledWorlds = CFG().getStringList("Damage-Disabled-Worlds");

            World world = player.getWorld();
            if (!(DisabledWorlds.contains(world.getName()))) {
                if (CFG().getBoolean("CancelOnPlayerDamage")) {
                    if (event.getEntity().getType() == EntityType.PLAYER) {
                        if (event.getDamage()!=0) {
                            cancelTimer(player);
                        }
                    }
                }

                if (CFG().getBoolean("CancelOnMobDamage")) {
                    if (event.getEntity().getType().isAlive()) {
                        if (event.getDamage()!=0) {
                            cancelTimer(player);
                        }
                    }
                }
            }
        }
    }
}
