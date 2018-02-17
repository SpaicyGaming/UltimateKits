package com.songoda.kitpreview.Kits;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import com.sun.xml.internal.ws.util.StringUtils;
import javafx.scene.shape.Arc;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by songoda on 2/24/2017.
 */
public class Kit {
    Location location = null;
    String locationStr = null;
    public String dname = null;
    public String name = null;

    public Kit(String loc) {
        defineBlockInformation(loc);
    }

    public Kit(String loc, String str) {
        if (KitPreview.getInstance().hooks.doesKitExist(str)) {
            dname = str;
            name = Arconix.pl().format().formatText(str, true);
        }
    }

    public Kit(Block b) {
        defineBlockInformation(Arconix.pl().serialize().serializeLocation(b));
    }

    private void defineBlockInformation(String loc) {
        try {
            dname = KitPreview.getInstance().getConfig().getString("data.block." + loc);
            name = Arconix.pl().format().formatText(dname, true);

            locationStr = loc;
            String str[] = loc.split(":");
            World w = Bukkit.getServer().getWorld(str[1].substring(0, str[1].length() - 1));
            double x = Double.parseDouble(str[2].substring(0, str[2].length() - 1)) + .5;
            double y = Double.parseDouble(str[3].substring(0, str[3].length() - 1));
            double z = Double.parseDouble(str[4]) + .5;
            location = new Location(w, x, y, z);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void removeKitFromBlock(Player p) {
        try {
            removeDisplayItems();

            String kit = KitPreview.getInstance().getConfig().getString("data.block." + locationStr);
            KitPreview.getInstance().getConfig().set("data.holo." + locationStr, null);
            KitPreview.getInstance().getConfig().set("data.particles." + locationStr, null);
            KitPreview.getInstance().getConfig().set("data.displayitems." + locationStr, null);
            KitPreview.getInstance().saveConfig();
            KitPreview.getInstance().holo.updateHolograms(false);
            KitPreview.getInstance().getConfig().set("data.block." + locationStr, null);
            KitPreview.getInstance().saveConfig();
            p.sendMessage(Arconix.pl().format().formatText(KitPreview.getInstance().references.getPrefix() + "&8Kit &9" + kit + " &8unassigned from: &a" + location.getBlock().getType().toString() + "&8."));
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    public void buy(Player p) {
        try {
            if (KitPreview.getInstance().hooks.hasPermission(p, dname) && KitPreview.getInstance().getConfig().getBoolean("Kits-Free-With-Perms")) {
                give(p, false, false, false);
            } else {
                if (KitPreview.getInstance().getConfig().getString("data.kit." + name + ".link") != null) {
                    String lin = KitPreview.getInstance().getConfig().getString("data.kit." + dname + ".link");
                    p.sendMessage("");
                    p.sendMessage(KitPreview.getInstance().references.getPrefix() + Arconix.pl().format().formatText("&a" + lin));
                    p.sendMessage("");
                    p.closeInventory();
                } else if (KitPreview.getInstance().getConfig().getString("data.kit." + dname + ".eco") != null) {
                    Buy.confirmBuy(dname, p);
                } else {
                    p.sendMessage(Lang.NO_PERM.getConfigValue());
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void give(Player p, boolean key, boolean economy, boolean console) {
        try {
            if (key == true) {
                if (p.getItemInHand().getType() == Material.TRIPWIRE_HOOK) {
                    if (p.getItemInHand().hasItemMeta()) {
                        if (!p.getItemInHand().getItemMeta().getDisplayName().equals(Lang.KEY_TITLE.getConfigValue(name))) {
                            p.sendMessage(Arconix.pl().format().formatText(KitPreview.getInstance().references.getPrefix() + Lang.WRONG_KEY.getConfigValue()));
                        } else {
                            if (ChatColor.stripColor(p.getItemInHand().getItemMeta().getLore().get(0)).equals("Regular Key")) {
                                KitPreview.getInstance().hooks.givePartKit(p, dname, keyReward());
                            } else {
                                KitPreview.getInstance().hooks.giveKit(p, dname);
                            }
                            p.sendMessage(KitPreview.getInstance().references.getPrefix() + Arconix.pl().format().formatText(Lang.KEY_SUCCESS.getConfigValue(name)));
                            if (p.getInventory().getItemInHand().getAmount() != 1) {
                                ItemStack is = p.getItemInHand();
                                is.setAmount(is.getAmount() - 1);
                                p.setItemInHand(is);
                            } else {
                                p.setItemInHand(null);
                            }
                        }
                    }
                }
            } else {
                long delay = KitPreview.getInstance().hooks.getNextUse(dname, p) - System.currentTimeMillis(); // gets delay

                if (KitPreview.getInstance().hooks.getNextUse(dname, p) == -1 && !economy && !console) {
                    p.sendMessage(KitPreview.getInstance().references.getPrefix() + Arconix.pl().format().formatText(Lang.NOT_TWICE.getConfigValue(name)));
                } else if (delay <= 0 || economy || console) {
                    KitPreview.getInstance().hooks.giveKit(p, dname);
                    if (economy) {
                        p.sendMessage(KitPreview.getInstance().references.getPrefix() + Arconix.pl().format().formatText(Lang.PURCHASE_SUCCESS.getConfigValue(name)));
                    } else {
                        KitPreview.getInstance().hooks.updateDelay(p, dname);
                        p.sendMessage(KitPreview.getInstance().references.getPrefix() + Arconix.pl().format().formatText(Lang.GIVE_SUCCESS.getConfigValue(name)));
                    }
                } else {
                    p.sendMessage(KitPreview.getInstance().references.getPrefix() + Arconix.pl().format().formatText(Lang.DELAY.getConfigValue(Arconix.pl().format().readableTime(delay))));
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

    }


    public void display(Player p, boolean back) {
        try {
            if (p.hasPermission("previewkit.use") || p.hasPermission("previewkit." + dname.toLowerCase()) || p.hasPermission("kitpreview.use") || p.hasPermission("kitpreview." + dname.toLowerCase())) {
                if (dname == null)
                    p.sendMessage(KitPreview.getInstance().references.getPrefix() + Lang.KIT_DOESNT_EXIST.getConfigValue(name));
                else {
                    KitPreview.getInstance().inKit.put(p, this);
                    p.sendMessage(KitPreview.getInstance().references.getPrefix() + Lang.PREVIEWING_KIT.getConfigValue(name));
                    KitPreview plugin = KitPreview.pl();
                    KitPreview.pl().xyz.add(p.getUniqueId());
                    String guititle = Arconix.pl().format().formatTitle(Lang.PREVIEW_TITLE.getConfigValue(name));
                    if (KitPreview.getInstance().getConfig().getString("data.kit." + dname + ".title") != null) {
                        String kitTitle = KitPreview.getInstance().getConfig().getString("data.kit." + dname + ".title");
                        guititle = Lang.PREVIEW_TITLE.getConfigValue(Arconix.pl().format().formatText(kitTitle,true));
                    }

                    guititle = Arconix.pl().format().formatText(guititle);

                    List<ItemStack> list = KitPreview.getInstance().hooks.getItems(p, dname, true);

                    int amt = 0;
                    for (ItemStack is : list) {
                        if (is.getAmount() > 64) {
                            int overflow = is.getAmount() % 64;
                            int stackamt = is.getAmount() / 64;
                            int num3 = 0;
                            while (num3 != stackamt) {
                                amt++;
                                num3++;
                            }
                            if (overflow != 0) {
                                amt++;
                            }

                        } else {
                            amt++;
                        }
                    }
                    boolean buyable = false;
                    if (KitPreview.getInstance().getConfig().getString("data.kit." + dname + ".link") != null || KitPreview.getInstance().getConfig().getString("data.kit." + dname + ".eco") != null) {
                        buyable = true;
                    }
                    int min = 0;
                    if (KitPreview.getInstance().getConfig().getBoolean("glassless")) {
                        min = 9;
                        if (!buyable) {
                            min = min + 9;
                        }
                    }
                    Inventory i = Bukkit.createInventory(null, 54 - min, Arconix.pl().format().formatTitle(guititle));
                    int max = 54 - min;
                    if (amt <= 7) {
                        i = Bukkit.createInventory(null, 27 - min, Arconix.pl().format().formatTitle(guititle));
                        max = 27 - min;
                    } else if (amt <= 15) {
                        i = Bukkit.createInventory(null, 36 - min, Arconix.pl().format().formatTitle(guititle));
                        max = 36 - min;
                    } else if (amt <= 25) {
                        i = Bukkit.createInventory(null, 45 - min, Arconix.pl().format().formatTitle(guititle));
                        max = 45 - min;
                    }


                    int num = 0;
                    if (!KitPreview.getInstance().getConfig().getBoolean("glassless")) {
                        ItemStack exit = new ItemStack(Material.valueOf(KitPreview.getInstance().getConfig().getString("Exit-Icon")), 1);
                        ItemMeta exitmeta = exit.getItemMeta();
                        exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
                        exit.setItemMeta(exitmeta);
                        while (num != 10) {
                            i.setItem(num, Methods.getGlass());
                            num++;
                        }
                        int num2 = max - 10;
                        while (num2 != max) {
                            i.setItem(num2, Methods.getGlass());
                            num2++;
                        }
                        i.setItem(8, exit);



                        i.setItem(0, Methods.getBackgroundGlass(true));
                        i.setItem(1, Methods.getBackgroundGlass(true));
                        i.setItem(9, Methods.getBackgroundGlass(true));

                        i.setItem(7, Methods.getBackgroundGlass(true));
                        i.setItem(17, Methods.getBackgroundGlass(true));

                        i.setItem(max - 18, Methods.getBackgroundGlass(true));
                        i.setItem(max - 9, Methods.getBackgroundGlass(true));
                        i.setItem(max - 8, Methods.getBackgroundGlass(true));

                        i.setItem(max - 10, Methods.getBackgroundGlass(true));
                        i.setItem(max - 2, Methods.getBackgroundGlass(true));
                        i.setItem(max - 1, Methods.getBackgroundGlass(true));

                        i.setItem(2, Methods.getBackgroundGlass(false));
                        i.setItem(6, Methods.getBackgroundGlass(false));
                        i.setItem(max - 7, Methods.getBackgroundGlass(false));
                        i.setItem(max - 3, Methods.getBackgroundGlass(false));
                    }

                    if (buyable) {
                        ItemStack link = new ItemStack(Material.valueOf(KitPreview.getInstance().getConfig().getString("Buy-Icon")), 1);
                        ItemMeta linkmeta = link.getItemMeta();
                        linkmeta.setDisplayName(Lang.BUYNOW.getConfigValue());
                        ArrayList<String> lore = new ArrayList<String>();
                        if (KitPreview.getInstance().getConfig().getString("data.kit." + dname + ".link") != null) {
                            lore.add(Lang.CLICKLINK.getConfigValue());
                        } else {
                            Double cost = KitPreview.getInstance().getConfig().getDouble("data.kit." + dname + ".eco");
                            if (KitPreview.getInstance().hooks.hasPermission(p, dname) && KitPreview.getInstance().getConfig().getBoolean("Kits-Free-With-Perms")) {
                                lore.add(Lang.CLICKECO.getConfigValue("0"));
                                if (p.isOp()) {
                                    lore.add("");
                                    lore.add("§7This is free because");
                                    lore.add("§7you have perms for it.");
                                    lore.add("§7Everyone else buys");
                                    lore.add("§7this for §a$" + Arconix.pl().format().formatEconomy(cost) + "§7.");
                                }
                            } else {
                                lore.add(Lang.CLICKECO.getConfigValue(Arconix.pl().format().formatEconomy(cost)));
                            }
                            if (KitPreview.getInstance().getConfig().getString("data.kit." + dname + ".delay") != null) {
                                if (p.isOp()) {
                                    lore.add("");
                                    lore.add("§7You do not have a delay");
                                    lore.add("§7because you have perms");
                                    lore.add("§7to bypass the delay.");
                                }
                            }
                        }
                        linkmeta.setLore(lore);
                        link.setItemMeta(linkmeta);
                        i.setItem(max - 5, link);
                    }

                    for (ItemStack is : list) {
                        if (!KitPreview.getInstance().getConfig().getBoolean("glassless")) {
                            if (num == 17)
                                num++;
                            if (num == (max - 18))
                                num++;
                        }
                        if (is.getAmount() > 64) {
                            if (is.hasItemMeta()) {
                                if (is.getItemMeta().hasLore()) {
                                    ArrayList<String> lore = new ArrayList<>();
                                    for (String str : is.getItemMeta().getLore()) {
                                        str = str.replace("{PLAYER}", p.getName());
                                        str = str.replace("<PLAYER>", p.getName());
                                        lore.add(str);
                                    }
                                    is.getItemMeta().setLore(lore);
                                }
                            }
                            int overflow = is.getAmount() % 64;
                            int stackamt = is.getAmount() / 64;
                            int num3 = 0;
                            while (num3 != stackamt) {
                                ItemStack is2 = is;
                                is2.setAmount(64);
                                i.setItem(num, is2);
                                num++;
                                num3++;
                            }
                            if (overflow != 0) {
                                ItemStack is2 = is;
                                is2.setAmount(overflow);
                                i.setItem(num, is2);
                                num++;
                            }

                        } else {
                            boolean command = false;
                            if (KitPreview.getInstance().getConfig().getBoolean("Dont-Preview-Commands") && is.getType().equals(Material.PAPER) && is.getItemMeta().hasDisplayName()) {
                                if (is.getItemMeta().getDisplayName().equals(Lang.COMMAND.getConfigValue()))
                                    command = true;
                            }
                            if (!command) {
                                i.setItem(num, is);
                                num++;
                            }
                        }
                    }

                    if (back && !KitPreview.getInstance().getConfig().getBoolean("glassless")) {

                        ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                        ItemStack skull2 = head2;
                        if (!KitPreview.getInstance().v1_7)
                            skull2 = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
                        SkullMeta skull2Meta = (SkullMeta) skull2.getItemMeta();
                        if (KitPreview.getInstance().v1_7)
                            skull2Meta.setOwner("MHF_ArrowLeft");
                        skull2.setDurability((short) 3);
                        skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
                        skull2.setItemMeta(skull2Meta);
                        i.setItem(0, skull2);
                    }

                    p.openInventory(i);
                }
            } else {
                p.sendMessage(KitPreview.getInstance().references.getPrefix() + Lang.NO_PERM.getConfigValue());
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

    }

    public int keyReward() {
        int result = 0;
        try {
            int reward = KitPreview.getInstance().getConfig().getInt("data.kit." + dname + ".keyReward");
            int size = KitPreview.getInstance().hooks.kitSize(dname);

            result = reward;
            if (reward >= size) {
                result = size;
            } else if (reward == 0) {
                result = size;
            }

        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

        return result;
    }

    public void setKeyReward(int amt) {
        try {
            int size = KitPreview.getInstance().hooks.kitSize(dname);

            if (amt >= size) {
                amt = size;
            } else if (amt <= 1) {
                amt = 1;
            }

            KitPreview.getInstance().getConfig().set("data.kit." + dname + ".keyReward", amt);
            KitPreview.getInstance().saveConfig();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

    }

    public void buyWithEconomy(Player p) {
        try {
            if (KitPreview.getInstance().getServer().getPluginManager().getPlugin("Vault") != null) {
                RegisteredServiceProvider<Economy> rsp = KitPreview.getInstance().getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

                net.milkbowl.vault.economy.Economy econ = rsp.getProvider();
                Double cost = KitPreview.getInstance().getConfig().getDouble("data.kit." + dname + ".eco");
                if (econ.has(p, cost) || KitPreview.getInstance().hooks.hasPermission(p, dname)) {
                    boolean canuse = true;
                    if (KitPreview.getInstance().getConfig().getString("data.kit." + dname + ".delay") != null) {

                        long delay = KitPreview.getInstance().hooks.getNextUse(dname, p);
                        if (delay != 0)
                            delay = delay - System.currentTimeMillis();

                        if (KitPreview.getInstance().hooks.getNextUse(dname, p) == -1) {
                            p.sendMessage(KitPreview.getInstance().references.getPrefix() + Arconix.pl().format().formatText(Lang.NOT_TWICE.getConfigValue(name)));
                        } else if (delay != 0) {
                            canuse = false;
                            p.sendMessage(KitPreview.getInstance().references.getPrefix() + Arconix.pl().format().formatText(Lang.DELAY.getConfigValue(Arconix.pl().format().readableTime(delay))));
                        }
                    }
                    if (canuse) {
                        if (KitPreview.getInstance().getConfig().getString("data.kit." + name + ".delay") != null) {
                            KitPreview.getInstance().hooks.updateDelay(p, dname); //updates delay on buy
                        }
                        econ.withdrawPlayer(p, cost);
                        give(p, false, true, false);
                    }
                } else {
                    p.sendMessage(KitPreview.getInstance().references.getPrefix() + Arconix.pl().format().formatText(Lang.CANNOT_AFFORD.getConfigValue(name)));
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

    }

    public void removeDisplayItems() {
        try {
            for (Entity e : location.getChunk().getEntities()) {
                if (e.getType() == EntityType.DROPPED_ITEM) {
                    if (e.getLocation().getX() == location.getX() && e.getLocation().getZ() == location.getZ()) {
                        Item i = (Item) e;
                        i.remove();
                    }
                }
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

    }

}
