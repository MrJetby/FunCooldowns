package me.jetby.cmdTimer;

import lombok.Getter;
import me.jetby.cmdTimer.commands.Reload;
import me.jetby.cmdTimer.listeners.EntityDamageEvent;
import me.jetby.cmdTimer.listeners.PlayerAttackEvent;
import me.jetby.cmdTimer.listeners.PlayerCommandPreprocess;
import me.jetby.cmdTimer.manager.Actions;
import me.jetby.cmdTimer.manager.Timer;
import me.jetby.cmdTimer.utils.Config;
import me.jetby.cmdTimer.utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {
    private Timer timer;
    private Actions actions;

    @Override
    public void onEnable() {
        actions = new Actions();
        timer = new Timer(this);

        new Metrics(this, 23883);

        Config cfg = new Config();
        cfg.loadYamlFile(this);

        getCommand("cmdtimer-reload").setExecutor(new Reload(this));
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocess(this), this);
        getServer().getPluginManager().registerEvents(new PlayerAttackEvent(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageEvent(this), this);



    }
}
