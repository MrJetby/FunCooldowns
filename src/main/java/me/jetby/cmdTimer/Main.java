package me.jetby.cmdTimer;

import me.jetby.cmdTimer.commands.Reload;
import me.jetby.cmdTimer.listeners.EntityDamageEvent;
import me.jetby.cmdTimer.listeners.PlayerAttackEvent;
import me.jetby.cmdTimer.listeners.PlayerCommandPreprocess;
import me.jetby.cmdTimer.utils.Config;
import me.jetby.cmdTimer.utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;


public final class Main extends JavaPlugin {
    public static Main INSTANCE;

    public static Main getInstance() {
        return INSTANCE;
    }


    @Override
    public void onEnable() {
        INSTANCE = this;

        int pluginId = 23883;
        Metrics metrics = new Metrics(this, pluginId);

        Config cfg = new Config();
        cfg.loadYamlFile(this);

        getCommand("cmdtimer-reload").setExecutor(new Reload());
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocess(), this);
        getServer().getPluginManager().registerEvents(new PlayerAttackEvent(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageEvent(), this);



    }
}
