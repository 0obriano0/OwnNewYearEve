package me.symi.newyear;

import me.symi.newyear.commands.NewYearCommand;
import me.symi.newyear.commands.SetFireworkCommand;
import me.symi.newyear.config.ConfigManager;
import me.symi.newyear.cooltime.CoolTime;
import me.symi.newyear.listeners.PlayerListeners;
import me.symi.newyear.manager.LocationDataManager;
import me.symi.newyear.metrics.MetricsLite;
import me.symi.newyear.utils.UpdateChecker;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;

public class OwnNewYearEve extends JavaPlugin {

    private static OwnNewYearEve INSTANCE;
    private LocationDataManager locationDataManager;
    private ConfigManager configManager;
    private CoolTime playerCooltime;

    @Override
    public void onLoad()
    {
        INSTANCE = this;
    }

    @Override
    public void onEnable()
    {
        configManager = new ConfigManager(this);
        locationDataManager = new LocationDataManager(this);
        playerCooltime = new CoolTime(this);
        
        getCommand("setfirework").setExecutor(new SetFireworkCommand(this));
        getCommand("newyear").setExecutor(new NewYearCommand(this));

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListeners(this), this);
        Logger logger = this.getLogger();

        new UpdateChecker(this, 87065).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("There is not a new update available.");
            } else {
                logger.info("There is a new update available.");
            }
        });
        
        timer();
        
        if(configManager.isMetrics())
        {
            setupMetrics();
        }
    }

    @Override
    public void onDisable()
    {
        if(locationDataManager != null)
            locationDataManager.onDisable();
    }

    public static OwnNewYearEve getInstance()
    {
        return INSTANCE;
    }

    private void setupMetrics()
    {
        new MetricsLite(this, 9751);
    }

    public LocationDataManager getLocationDataManager()
    {
        return locationDataManager;
    }

    public ConfigManager getConfigManager()
    {
        return configManager;
    }
    
    public CoolTime getplayerCooltime()
    {
        return playerCooltime;
    }
    
    public void timer()
    {
    	BukkitScheduler scheduler=getServer().getScheduler();
    	scheduler.scheduleSyncRepeatingTask(this,new Runnable()
    			{
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerCooltime.clearCache();
					}
    			},20*configManager.getcooltime(),20*configManager.getcooltime());
    }
    
}
