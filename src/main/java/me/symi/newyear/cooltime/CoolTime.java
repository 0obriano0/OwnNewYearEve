package me.symi.newyear.cooltime;

import java.util.HashMap;
import java.util.Map;

import me.symi.newyear.OwnNewYearEve;

public class CoolTime {
	private final OwnNewYearEve plugin;
	
	public CoolTime(OwnNewYearEve plugin)
    {
        this.plugin = plugin;
    }
	
	private Map<String,Long> playerCooltime = new HashMap<String,Long>();

	public boolean update(String player)
	{
		return update(player,System.currentTimeMillis());
	}
	
	public boolean update(String player,Long time)
	{
		playerCooltime.put(player, time);
		return true;
	}
	
	public boolean clearCache()
	{
		if(playerCooltime.size() > 0) {
			Long time = System.currentTimeMillis();
			for(Map.Entry<String, Long> entry : playerCooltime.entrySet())
			{
				if(time - entry.getValue() >= plugin.getConfigManager().getcooltime()) {
					playerCooltime.remove(entry.getKey());
				}
			}
		}
		return true;
	}
	
	public boolean istimeout(String player) {
		if(playerCooltime.containsKey(player) && (System.currentTimeMillis() - playerCooltime.get(player))/1000 >= plugin.getConfigManager().getcooltime())
			return true;
		else if (!playerCooltime.containsKey(player))
			return true;
		return false;
	}
	
	public long waittime(String player) {
		if(playerCooltime.containsKey(player))
		{
			return System.currentTimeMillis() - playerCooltime.get(player);
		}
		return -1;
	}
}
