package me.symi.newyear.listeners;

import me.symi.newyear.OwnNewYearEve;
import me.symi.newyear.utils.XMaterial;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.text.DecimalFormat;
import java.util.Random;

public class PlayerListeners implements Listener {

    private final OwnNewYearEve plugin;

    public PlayerListeners(OwnNewYearEve plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if (event.getItem() == null)
            {
                return;
            }
            if(plugin.getplayerCooltime().istimeout(event.getPlayer().getName())) {
            	if ((event.getItem().getType() == XMaterial.GOLDEN_HORSE_ARMOR.parseMaterial())
                        && (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getConfigManager().getRocket_gun_name())))
                {
                    final FireworkEffect.Builder fb = FireworkEffect.builder();
                    final Random r = new Random();
                    FireworkEffect f = null;
                    fb.withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                    final FireworkEffect.Type[] type = FireworkEffect.Type.values();
                    fb.with(type[r.nextInt(type.length)]);
                    if (r.nextInt(3) == 0) {
                        fb.withTrail();
                    }
                    if (r.nextInt(2) == 0) {
                        fb.withFade(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
                    }
                    if (r.nextInt(3) == 0) {
                        fb.withFlicker();
                    }
                    f = fb.build();
                    final Player player = event.getPlayer();
                    final Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                    final FireworkMeta fwm = fw.getFireworkMeta();
                    fwm.clearEffects();
                    fwm.addEffect(f);
                    fwm.setPower(0);
                    fw.setFireworkMeta(fwm);
                    fw.setVelocity(player.getLocation().getDirection().multiply(0.5));
                    plugin.getplayerCooltime().update(event.getPlayer().getName());
                }
            }else
            {
            	Long time = plugin.getplayerCooltime().waittime(event.getPlayer().getName());
            	if(time != -1) {
            		DecimalFormat df = new DecimalFormat("######0.00");  
            		event.getPlayer().sendMessage(plugin.getConfigManager().getcooltime_lore().replaceAll("&", "§").replaceAll("%time%", df.format(plugin.getConfigManager().getcooltime() - ((double)time/1000)) + ""));
            	}else 
            	{
            		event.getPlayer().sendMessage("§6§lOwnNewYearEve §8» §csystem Error| data = §f" + time);
            	}
            }
        }
    }

}
