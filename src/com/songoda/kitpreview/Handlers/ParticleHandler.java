package com.songoda.kitpreview.Handlers;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Created by songoda on 2/24/2017.
 */
public class ParticleHandler {

    public boolean v1_7 = KitPreview.getInstance().getServer().getClass().getPackage().getName().contains("1_7");
    public boolean v1_8 = KitPreview.getInstance().getServer().getClass().getPackage().getName().contains("1_8");


    public ParticleHandler() {
        checkDefaults();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(KitPreview.getInstance(), new Runnable() {
            public void run() {
                applyParticles();
            }
        }, 5L, 10L);
    }

    @SuppressWarnings("all")
    public void applyParticles() {
        try {
            if (KitPreview.getInstance().getConfig().getString("data.particles") != null) {
                int amt = KitPreview.getInstance().getConfig().getInt("data.particlesettings.ammount");
                String type = "";
                type = KitPreview.getInstance().getConfig().getString("data.particlesettings.type");

                ConfigurationSection section = KitPreview.getInstance().getConfig().getConfigurationSection("data.particles");
                for (String loc : section.getKeys(false)) {
                    String str[] = loc.split(":");
                    String worldName = str[1].substring(0, str[1].length() - 1);
                    if (Bukkit.getServer().getWorld(worldName) != null) {
                        Location location = Arconix.pl().serialize().unserializeLocation(loc);
                        location.add(.5,0,.5);
                    /*
                    String typ = "";
                    if (KitPreview.getInstance().getConfig().getString("data.type." + loc) != null) {
                        typ = KitPreview.getInstance().getConfig().getString("data.type." + loc);
                    }

                    if (typ.equals("daily")) {
                        String kit = KitPreview.getInstance().getConfig().getString("data.block." + loc);
                        for (Player p : KitPreview.getInstance().getServer().getOnlinePlayers()) {
                            if (KitPreview.getInstance().hoooks.isReady(p, kit)) {
                                if (v1_8 || v1_7) {
                                    //p.playEffect(location, org.bukkit.Effect.valueOf(type), 1, 0, (float) 0.25, (float) 0.25, (float) 0.25, 1, amt, 100);
                                } else {
                                    p.spawnParticle(org.bukkit.Particle.valueOf(type), location, amt, 0.25, 0.25, 0.25);
                                }
                            } else {
                                p.sendBlockChange(location, Material.AIR, (byte) 0);
                            }
                        }
                    } else { */
                        if (v1_8 || v1_7) {
                            location.getWorld().spigot().playEffect(location, org.bukkit.Effect.valueOf(type), 1, 0, (float) 0.25, (float) 0.25, (float) 0.25, 1, amt, 100);
                        } else {
                            location.getWorld().spawnParticle(org.bukkit.Particle.valueOf(type), location, amt, 0.25, 0.25, 0.25);
                        }
                        //}
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private void checkDefaults() {
        try {
            if (KitPreview.getInstance().getConfig().getInt("data.particlesettings.ammount") == 0) {
                KitPreview.getInstance().getConfig().set("data.particlesettings.ammount", 25);
                KitPreview.getInstance().saveConfig();
            }
            if (KitPreview.getInstance().getConfig().getString("data.particlesettings.type") == null) {
                if (v1_7 || v1_8) {
                    KitPreview.getInstance().getConfig().set("data.particlesettings.type", "WITCH_MAGIC");
                } else {
                    KitPreview.getInstance().getConfig().set("data.particlesettings.type", "SPELL_WITCH");
                }
                KitPreview.getInstance().saveConfig();
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

}
