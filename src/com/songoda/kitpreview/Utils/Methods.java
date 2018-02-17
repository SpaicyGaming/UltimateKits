package com.songoda.kitpreview.Utils;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by songoda on 2/24/2017.
 */
public class Methods {
    
    public static ItemStack getGlass() {
        KitPreview plugin = KitPreview.pl();
        return Arconix.pl().getGUI().getGlass(plugin.getConfig().getBoolean("Rainbow-Glass"), plugin.getConfig().getInt("Glass-Type-1"));
    }

    public static ItemStack getBackgroundGlass(boolean type) {
        KitPreview plugin = KitPreview.pl();
        if (type)
            return Arconix.pl().getGUI().getGlass(false, plugin.getConfig().getInt("Glass-Type-2"));
        else
            return Arconix.pl().getGUI().getGlass(false, plugin.getConfig().getInt("Glass-Type-3"));
    }

    public static void fillGlass(Inventory i) {
        int nu = 0;
        while (nu != 27) {
            ItemStack glass = getGlass();
            ItemMeta glassmeta = glass.getItemMeta();
            glassmeta.setDisplayName("§5");
            glass.setItemMeta(glassmeta);
            i.setItem(nu, glass);
            nu++;
        }
    }

}
