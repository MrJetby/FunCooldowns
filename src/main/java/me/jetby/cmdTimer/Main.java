package me.jetby.cmdTimer;

import me.jetby.cmdTimer.Commands.Commands;
import me.jetby.cmdTimer.Commands.Reload;
import me.jetby.cmdTimer.Events.EntityDamageEvent;
import me.jetby.cmdTimer.Events.PlayerAttackEvent;
import me.jetby.cmdTimer.Events.PlayerCommandPreprocess;
import me.jetby.cmdTimer.Utils.Metrics;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;


public final class Main extends JavaPlugin {
    public static YamlConfiguration cfg;
    public static Main instance;

    public static Main getInstance() {
        return instance;
    }

    public void cfgReload() {
        File file = new File(getDataFolder().getAbsolutePath() + "/config.yml");
        if (file.exists()) {
            getLogger().log(Level.INFO, "Конфиг успешно загружен. (config.yml)");
            cfg = YamlConfiguration.loadConfiguration(file);
        } else {
            saveResource("config.yml", false);
            cfg = YamlConfiguration.loadConfiguration(file);
        }
    }
    
    @Override
    public void onEnable() {
        instance = this;
        cfgReload();

        int pluginId = 23883;
        Metrics metrics = new Metrics(this, pluginId);

        getCommand("cancelteleport").setExecutor(new Commands());
        getCommand("cmdtimer-reload").setExecutor(new Reload());
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocess(), this);
        getServer().getPluginManager().registerEvents(new PlayerAttackEvent(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageEvent(), this);



    }
}
