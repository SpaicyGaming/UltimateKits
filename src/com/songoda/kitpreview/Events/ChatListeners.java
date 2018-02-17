package com.songoda.kitpreview.Events;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Kits.Editor;
import com.songoda.kitpreview.Kits.Keys;
import com.songoda.kitpreview.Kits.Kit;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songoda on 2/24/2017.
 */
public class ChatListeners implements Listener {

    KitPreview plugin = KitPreview.pl();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        try {
            final Player p = e.getPlayer();
            if (KitPreview.getInstance().inEditor.containsKey(p)) {
                if (KitPreview.getInstance().inEditor.get(p) == "price") {
                    e.setCancelled(true);
                    Editor edit = new Editor(KitPreview.getInstance().editingKit.get(p).name, p);
                    String msg = e.getMessage();
                    Kit kit = KitPreview.getInstance().editingKit.get(p);

                    if (KitPreview.getInstance().getServer().getPluginManager().getPlugin("Vault") == null) {
                        p.sendMessage(KitPreview.getInstance().references.getPrefix() + Arconix.pl().format().formatText("&8You must have &aVault &8installed to utilize economy.."));
                    } else {
                        if (!Arconix.pl().doMath().isNumeric(msg)) {
                            p.sendMessage(Arconix.pl().format().formatText("&a" + msg + " &8is not a number. Please do not include a &a$&8."));
                        } else {
                            if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.dname + ".link") != null) {
                                KitPreview.getInstance().getConfig().set("data.kit." + kit.dname + ".link", null);
                                p.sendMessage(Arconix.pl().format().formatText(KitPreview.getInstance().references.getPrefix() + "&8LINK has been removed from this kit. Note you cannot have ECO & LINK set at the same time.."));
                            }
                            Double eco = Double.parseDouble(msg);
                            KitPreview.getInstance().getConfig().set("data.kit." + kit.dname + ".eco", eco);
                            KitPreview.getInstance().holo.updateHolograms(true);
                            KitPreview.getInstance().saveConfig();
                        }
                    }
                    KitPreview.getInstance().inEditor.remove(p);
                    edit.selling();
                } else if (KitPreview.getInstance().inEditor.get(p) == "link") {
                    e.setCancelled(true);
                    Editor edit = new Editor(KitPreview.getInstance().editingKit.get(p).name, p);
                    String msg = e.getMessage();
                    Kit kit = KitPreview.getInstance().editingKit.get(p);

                    if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.dname + ".eco") != null) {
                        KitPreview.getInstance().getConfig().set("data.kit." + kit.dname + ".eco", null);
                        p.sendMessage(Arconix.pl().format().formatText(KitPreview.getInstance().references.getPrefix() + "&8ECO has been removed from this kit. Note you cannot have ECO & LINK set at the same time.."));
                    }
                    String link = msg;
                    KitPreview.getInstance().getConfig().set("data.kit." + kit.dname + ".link", link);
                    KitPreview.getInstance().saveConfig();
                    KitPreview.getInstance().holo.updateHolograms(true);
                    KitPreview.getInstance().inEditor.remove(p);
                    edit.selling();
                } else if (KitPreview.getInstance().inEditor.get(p) == "title") {
                    e.setCancelled(true);
                    Editor edit = new Editor(KitPreview.getInstance().editingKit.get(p).name, p);
                    String msg = e.getMessage();
                    Kit kit = KitPreview.getInstance().editingKit.get(p);

                    KitPreview.getInstance().getConfig().set("data.kit." + kit.dname + ".title", msg);
                    KitPreview.getInstance().saveConfig();
                    KitPreview.getInstance().holo.updateHolograms(true);
                    p.sendMessage(Arconix.pl().format().formatText(KitPreview.getInstance().references.getPrefix() + "&8Title &5" + msg + "&8 added to Kit &a" + kit.name + "&8."));
                    KitPreview.getInstance().inEditor.remove(p);
                    edit.gui();
                } else if (KitPreview.getInstance().inEditor.get(p) == "command") {
                    e.setCancelled(true);
                    String msg = e.getMessage();
                    Editor edit = new Editor(KitPreview.getInstance().editingKit.get(p).name, p);

                    ItemStack parseStack = new ItemStack(Material.PAPER, 1);
                    ItemMeta meta = parseStack.getItemMeta();

                    ArrayList<String> lore = new ArrayList<>();


                    int index = 0;
                    while (index < msg.length()) {
                        lore.add("§a/" + msg.substring(index, Math.min(index + 30, msg.length())));
                        index += 30;
                    }
                    meta.setLore(lore);
                    meta.setDisplayName(Lang.COMMAND.getConfigValue());
                    parseStack.setItemMeta(meta);

                    p.sendMessage(Arconix.pl().format().formatText(KitPreview.getInstance().references.getPrefix() + "&8Command &5" + msg + "&8 saved to your inventory."));
                    KitPreview.getInstance().inEditor.remove(p);
                    edit.open(false, parseStack);
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    @EventHandler()
    public void onCommandPreprocess(PlayerChatEvent event) {
        try {
            if (event.getMessage().equalsIgnoreCase("/kits")) {
                event.setCancelled(true);
            }
            if (event.getMessage().equalsIgnoreCase("/kit")) {
                event.setCancelled(true);
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

}
