package com.songoda.kitpreview.Handlers;

import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Created by songoda on 3/5/2017.
 */
public class DailyHandler {


    public DailyHandler() {
            /*Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                public void run() {
                    checkDailys();
                }
            }, 100L, 100L); */
    }

    @SuppressWarnings("all")
    public void checkDailys() {
        try {
            if (KitPreview.getInstance().getConfig().getString("data.type") != null) {

                ConfigurationSection section = KitPreview.getInstance().getConfig().getConfigurationSection("data.type");
                for (String loc : section.getKeys(false)) {
                    if (KitPreview.getInstance().getConfig().getString("data.type." + loc).equals("daily")) {
                        String str[] = loc.split(":");
                        String worldName = str[1].substring(0, str[1].length() - 1);
                        if (Bukkit.getServer().getWorld(worldName) != null) {
                            World w = Bukkit.getServer().getWorld(worldName);
                            double x = Double.parseDouble(str[2].substring(0, str[2].length() - 1)) + .5;
                            double y = Double.parseDouble(str[3].substring(0, str[3].length() - 1));
                            double z = Double.parseDouble(str[4]) + .5;
                            Location location = new Location(w, x, y, z);
                            String kit = KitPreview.getInstance().getConfig().getString("data.block." + loc);
                            for (Player p : KitPreview.getInstance().getServer().getOnlinePlayers()) {
                                if (KitPreview.getInstance().hooks.isReady(p, kit)) {
                                    p.sendBlockChange(location, location.getBlock().getType(), location.getBlock().getData());
                                } else {
                                    p.sendBlockChange(location, Material.BARRIER, (byte) 0);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

}
