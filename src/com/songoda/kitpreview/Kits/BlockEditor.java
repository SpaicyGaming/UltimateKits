package com.songoda.kitpreview.Kits;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

/**
 * Created by songoda on 3/3/2017.
 */
public class BlockEditor {

    Location location = null;
    String loc = null;

    Kit kit = null;
    Block b = null;

    Player p = null;

    public BlockEditor(Block bl, Player pl) {
        try {
            String loco = Arconix.pl().serialize().serializeLocation(bl);
            location = bl.getLocation();
            loc = loco;
            kit = new Kit(loco);
            p = pl;
            b = bl;
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private void defineInstance(String window) {
        try {
            KitPreview.getInstance().inEditor.put(p, window);
            KitPreview.getInstance().editing.put(p, b);
            KitPreview.getInstance().editingKit.put(p, kit);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void open() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatText("&8This contains &a" + Arconix.pl().format().formatTitle(kit.name)));

            Methods.fillGlass(i);

            i.setItem(0, Methods.getBackgroundGlass(true));
            i.setItem(1, Methods.getBackgroundGlass(true));
            i.setItem(2, Methods.getBackgroundGlass(false));
            i.setItem(6, Methods.getBackgroundGlass(false));
            i.setItem(7, Methods.getBackgroundGlass(true));
            i.setItem(8, Methods.getBackgroundGlass(true));
            i.setItem(9, Methods.getBackgroundGlass(true));
            i.setItem(10, Methods.getBackgroundGlass(false));
            i.setItem(16, Methods.getBackgroundGlass(false));
            i.setItem(17, Methods.getBackgroundGlass(true));
            i.setItem(18, Methods.getBackgroundGlass(true));
            i.setItem(19, Methods.getBackgroundGlass(true));
            i.setItem(20, Methods.getBackgroundGlass(false));
            i.setItem(24, Methods.getBackgroundGlass(false));
            i.setItem(25, Methods.getBackgroundGlass(true));
            i.setItem(26, Methods.getBackgroundGlass(true));

            ItemStack exit = new ItemStack(Material.valueOf(KitPreview.getInstance().getConfig().getString("Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.REDSTONE_COMPARATOR);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&5&lSwitch kit type"));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to swap this kits type."));
            lore.add("");
            if (KitPreview.getInstance().getConfig().getString("data.type." + loc) == null) {
                lore.add(Arconix.pl().format().formatText("&6Normal"));
                lore.add(Arconix.pl().format().formatText("&7Crate"));
                lore.add(Arconix.pl().format().formatText("&7Daily"));
            } else if (KitPreview.getInstance().getConfig().getString("data.type." + loc).equals("crate")) {
                lore.add(Arconix.pl().format().formatText("&7Normal"));
                lore.add(Arconix.pl().format().formatText("&6Crate"));
                lore.add(Arconix.pl().format().formatText("&7Daily"));
            } else if (KitPreview.getInstance().getConfig().getString("data.type." + loc).equals("daily")) {
                lore.add(Arconix.pl().format().formatText("&7Normal"));
                lore.add(Arconix.pl().format().formatText("&7Crate"));
                lore.add(Arconix.pl().format().formatText("&6Daily"));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(11, alli);

            alli = new ItemStack(Material.RED_ROSE);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lDecor Options"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to edit the decoration"));
            lore.add(Arconix.pl().format().formatText("&7options for this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(13, alli);

            alli = new ItemStack(Material.DIAMOND_PICKAXE);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&a&lEdit kit"));
            lore = new ArrayList<>();
            lore.add(Arconix.pl().format().formatText("&7Click to edit the kit"));
            lore.add(Arconix.pl().format().formatText("&7contained in this block."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(15, alli);

            p.openInventory(i);
            defineInstance("menu");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void decor() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatText("&8Editing decor for &a" + Arconix.pl().format().formatTitle(kit.name) + "&8."));

            Methods.fillGlass(i);

            i.setItem(0, Methods.getBackgroundGlass(true));
            i.setItem(1, Methods.getBackgroundGlass(true));
            i.setItem(2, Methods.getBackgroundGlass(false));
            i.setItem(6, Methods.getBackgroundGlass(false));
            i.setItem(7, Methods.getBackgroundGlass(true));
            i.setItem(8, Methods.getBackgroundGlass(true));
            i.setItem(9, Methods.getBackgroundGlass(true));
            i.setItem(10, Methods.getBackgroundGlass(false));
            i.setItem(16, Methods.getBackgroundGlass(false));
            i.setItem(17, Methods.getBackgroundGlass(true));
            i.setItem(18, Methods.getBackgroundGlass(true));
            i.setItem(19, Methods.getBackgroundGlass(true));
            i.setItem(20, Methods.getBackgroundGlass(false));
            i.setItem(24, Methods.getBackgroundGlass(false));
            i.setItem(25, Methods.getBackgroundGlass(true));
            i.setItem(26, Methods.getBackgroundGlass(true));

            ItemStack exit = new ItemStack(Material.valueOf(KitPreview.getInstance().getConfig().getString("Exit-Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);


            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.SIGN);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lToggle Holograms"));
            ArrayList<String> lore = new ArrayList<>();
            if (KitPreview.getInstance().getConfig().getString("data.holo." + loc) != null) {
                lore.add(Arconix.pl().format().formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently &cDisabled&7."));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(10, alli);

            alli = new ItemStack(Material.POTION);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lToggle Particles"));
            lore = new ArrayList<>();
            if (KitPreview.getInstance().getConfig().getString("data.particles." + loc) != null) {
                lore.add(Arconix.pl().format().formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently &cDisabled&7."));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(12, alli);

            alli = new ItemStack(Material.GRASS);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lToggle DisplayItems"));
            lore = new ArrayList<>();
            if (KitPreview.getInstance().getConfig().getString("data.displayitems." + loc) != null) {
                lore.add(Arconix.pl().format().formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently &cDisabled&7."));
            }
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(14, alli);

            alli = new ItemStack(Material.GLASS);
            if (KitPreview.getInstance().getConfig().getItemStack("data.kit." + kit.name + ".displayitem") != null) {
                alli = KitPreview.getInstance().getConfig().getItemStack("data.kit." + kit.name + ".displayitem");
            }
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(Arconix.pl().format().formatText("&9&lSet single DisplayItem"));
            lore = new ArrayList<>();
            if (KitPreview.getInstance().getConfig().getItemStack("data.kit." + kit.name + ".displayitem") != null) {
                ItemStack is = KitPreview.getInstance().getConfig().getItemStack("data.kit." + kit.name + ".displayitem");
                lore.add(Arconix.pl().format().formatText("&7Currently set to: &a" + is.getType().toString() + "&7."));
            } else {
                lore.add(Arconix.pl().format().formatText("&7Currently &cDisabled&7."));
            }
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7Right-Click to &9Set a"));
            lore.add(Arconix.pl().format().formatText("&9forced display item for this "));
            lore.add(Arconix.pl().format().formatText("&9kit to the item in your hand."));
            lore.add("");
            lore.add(Arconix.pl().format().formatText("&7Left-Click to &9Remove the item."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(16, alli);

            p.openInventory(i);
            defineInstance("decor");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    public void toggleHologram() {
        try {
            if (KitPreview.getInstance().getConfig().getString("data.holo." + loc) == null) {
                KitPreview.getInstance().getConfig().set("data.holo." + loc, true);
                KitPreview.getInstance().saveConfig();
            } else {
                KitPreview.getInstance().getConfig().set("data.holo." + loc, null);
                KitPreview.getInstance().saveConfig();
            }
            KitPreview.getInstance().holo.updateHolograms(false);
            decor();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void toggleParticles() {
        try {
            if (KitPreview.getInstance().getConfig().getString("data.particles." + loc) == null) {
                KitPreview.getInstance().getConfig().set("data.particles." + loc, true);
                KitPreview.getInstance().saveConfig();
            } else {
                KitPreview.getInstance().getConfig().set("data.particles." + loc, null);
                KitPreview.getInstance().saveConfig();
            }
            decor();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void toggleDisplayItems() {
        try {
            if (KitPreview.getInstance().getConfig().getString("data.displayitems." + loc) == null) {
                KitPreview.getInstance().getConfig().set("data.displayitems." + loc, true);
                KitPreview.getInstance().saveConfig();
            } else {
                KitPreview.getInstance().getConfig().set("data.displayitems." + loc, null);
                KitPreview.getInstance().saveConfig();
                kit.removeDisplayItems();
            }
            KitPreview.getInstance().holo.updateHolograms(true);
            decor();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setDisplayItem(boolean type) {
        try {
            if (type) {
                ItemStack is = p.getItemInHand();
                KitPreview.getInstance().getConfig().set("data.kit." + kit.name + ".displayitem", is);
                KitPreview.getInstance().saveConfig();
            } else {
                KitPreview.getInstance().getConfig().set("data.kit." + kit.name + ".displayitem", null);
                KitPreview.getInstance().saveConfig();
            }
            decor();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    public void changeDisplayType() {
        try {
            if (KitPreview.getInstance().getConfig().getString("data.type." + loc) == null) {
                KitPreview.getInstance().getConfig().set("data.type." + loc, "crate");
            } else if (KitPreview.getInstance().getConfig().getString("data.type." + loc).equals("crate")) {
                KitPreview.getInstance().getConfig().set("data.type." + loc, "daily");
            } else if (KitPreview.getInstance().getConfig().getString("data.type." + loc).equals("daily")) {
                KitPreview.getInstance().getConfig().set("data.type." + loc, null);
            }
            KitPreview.getInstance().saveConfig();
            KitPreview.getInstance().holo.updateHolograms(true);
            open();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

}
