package com.songoda.kitpreview.kits;

import com.songoda.arconix.Arconix;
import com.songoda.arconix.method.formatting.TextComponent;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.utils.Debugger;
import com.songoda.kitpreview.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by songoda on 3/2/2017.
 */
public class Editor {

    Kit kit = null;

    Player p = null;

    public Editor(String kitt, Player pl) {
        try {
            kit = new Kit(kitt.toLowerCase());
            p = pl;
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    private void defineInstance(String window) {
        try {
            KitPreview.getInstance().inEditor.put(p.getUniqueId(), window);
            KitPreview.getInstance().editingKit.put(p.getUniqueId(), kit);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void open(boolean backb, ItemStack command) {
        try {
            Inventory i = Bukkit.createInventory(null, 54, Arconix.pl().format().formatTitle("&8You are editing kit: &9" + kit.getShowableName() + "&8."));


            ItemStack exit = new ItemStack(Material.valueOf(KitPreview.getInstance().getConfig().getString("Interfaces.Exit Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!KitPreview.getInstance().v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (KitPreview.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);


            ItemStack it = new ItemStack(Material.CHEST, 1);
            ItemMeta itmeta = it.getItemMeta();
            itmeta.setDisplayName(TextComponent.formatText("&5&l" + kit.getShowableName()));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&fPermissions:"));
            lore.add(TextComponent.formatText("&7essentials.kits." + kit.getName().toLowerCase()));
            itmeta.setLore(lore);
            it.setItemMeta(itmeta);

            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
            ItemMeta glassmeta = glass.getItemMeta();
            glassmeta.setDisplayName(TextComponent.formatText("&" + kit.getShowableName().replaceAll(".(?!$)", "$0&")));
            glass.setItemMeta(glassmeta);

            if (backb)
                i.setItem(0, back);
            i.setItem(4, it);
            i.setItem(8, exit);

            int num = 10;
            List<ItemStack> list = new ArrayList<>();
            if (KitPreview.getInstance().hooks.doesKitExist(kit.getName()))
                list = KitPreview.getInstance().hooks.getItems(p, kit.getName(), true);
            for (ItemStack is : list) {
                if (num == 17 || num == 36)
                    num ++;
                if (is.getAmount() > 64) {
                    int overflow = is.getAmount() % 64;
                    int stackamt = (int) ((long) (is.getAmount() / 64));
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
                    i.setItem(num, is);
                    num++;
                }
            }
            if (command != null)
                i.setItem(num, command);

            i.setItem(3, Methods.getGlass());
            i.setItem(5, Methods.getGlass());

            i.setItem(0, Methods.getBackgroundGlass(true));
            i.setItem(1, Methods.getBackgroundGlass(true));
            i.setItem(9, Methods.getBackgroundGlass(true));

            i.setItem(7, Methods.getBackgroundGlass(true));
            i.setItem(17, Methods.getBackgroundGlass(true));

            i.setItem(54 - 18, Methods.getBackgroundGlass(true));
            i.setItem(54 - 9, Methods.getBackgroundGlass(true));
            i.setItem(54 - 8, Methods.getBackgroundGlass(true));

            i.setItem(54 - 10, Methods.getBackgroundGlass(true));
            i.setItem(54 - 2, Methods.getBackgroundGlass(true));
            i.setItem(54 - 1, Methods.getBackgroundGlass(true));

            i.setItem(2, Methods.getBackgroundGlass(false));
            i.setItem(6, Methods.getBackgroundGlass(false));
            i.setItem(54 - 7, Methods.getBackgroundGlass(false));
            i.setItem(54 - 3, Methods.getBackgroundGlass(false));

            ItemStack alli = new ItemStack(Material.EMERALD, 1);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9Selling Options"));
            lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Click to edit adjust"));
            lore.add(TextComponent.formatText("&7selling options."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(47, alli);

            alli = new ItemStack(Material.TRIPWIRE_HOOK, 1);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&bKey Options"));
            lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Click to edit adjust"));
            lore.add(TextComponent.formatText("&7kit key options."));
            lore.add("");
            lore.add(TextComponent.formatText("&7You can give players a key"));
            lore.add(TextComponent.formatText("&7that they can use to receive"));
            lore.add(TextComponent.formatText("&7all or part of a kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(48, alli);

            alli = new ItemStack(Material.ITEM_FRAME, 1);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&5GUI Options"));
            lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Click to edit GUI options"));
            lore.add(TextComponent.formatText("&7for this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(49, alli);

            alli = new ItemStack(Material.PAPER, 1);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&fAdd Command"));
            lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Click to add a command"));
            lore.add(TextComponent.formatText("&7to this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(50, alli);

            alli = new ItemStack(Material.REDSTONE, 1);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&aSave Changes"));
            lore = new ArrayList<>();
            gui();
            lore.add(TextComponent.formatText("&7Click to save all changes."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(51, alli);

            p.openInventory(i);
            defineInstance("editor");
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    public void keys() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle("&8Key Options for &a" + kit.getShowableName() + "&8."));

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

            ItemStack exit = new ItemStack(Material.valueOf(KitPreview.getInstance().getConfig().getString("Interfaces.Exit Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);


            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!KitPreview.getInstance().v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (KitPreview.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&c&lAdd a &e&lRegular &c&lkey to inventory"));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Clicking this option will"));
            lore.add(TextComponent.formatText("&7place a normal key into your"));
            lore.add(TextComponent.formatText("&7inventory."));
            lore.add("");
            lore.add(TextComponent.formatText("&7A regular key will follow the"));
            lore.add(TextComponent.formatText("&7options defined in this GUI."));
            lore.add("");
            lore.add(TextComponent.formatText("&7Right clicking this will"));
            lore.add(TextComponent.formatText("&7result in a stack."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(11, alli);

            int keyReward = kit.keyReward();

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&c&lAdd a &5&lUltra &c&lkey to inventory"));
            lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Clicking this option will"));
            lore.add(TextComponent.formatText("&7place a ultra key into your"));
            lore.add(TextComponent.formatText("&7inventory."));
            lore.add("");
            lore.add(TextComponent.formatText("&7A ultra key will disregard the"));
            lore.add(TextComponent.formatText("&7options defined in this GUI"));
            lore.add(TextComponent.formatText("&7and reward the player with"));
            lore.add(TextComponent.formatText("&7all the items in the kit."));
            lore.add("");
            lore.add(TextComponent.formatText("&7Right clicking this will"));
            lore.add(TextComponent.formatText("&7result in a stack."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(13, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&a&lChange Reward"));
            lore = new ArrayList<>();
            lore.add(TextComponent.formatText("&7Current reward amount: " + keyReward));
            lore.add("");
            lore.add(TextComponent.formatText("&7Clicking this option will allow"));
            lore.add(TextComponent.formatText("&7you to change the amount of"));
            lore.add(TextComponent.formatText("&7items a player will receive"));
            lore.add(TextComponent.formatText("&7when using a key on this kit."));
            lore.add("");
            lore.add(TextComponent.formatText("&7Left clicking this will"));
            lore.add(TextComponent.formatText("&7increase the amount as"));
            lore.add(TextComponent.formatText("&7right clicking will decrease."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(15, alli);

            p.openInventory(i);
            defineInstance("keys");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


    public void selling() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle("&8Selling Options for &a" + kit.getShowableName() + "&8."));

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

            ItemStack exit = new ItemStack(Material.valueOf(KitPreview.getInstance().getConfig().getString("Interfaces.Exit Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!KitPreview.getInstance().v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (KitPreview.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&c&lSet not for sale"));
            ArrayList<String> lore = new ArrayList<>();

            if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".eco") != null ||
                    KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".link") != null)
                lore.add(TextComponent.formatText("&7Currently &aFor Sale&7."));
            else
                lore.add(TextComponent.formatText("&7Currently &cNot For Sale&7."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Clicking this option will"));
            lore.add(TextComponent.formatText("&7remove this kit from sale."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(10, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&a&lSet kit link"));
            lore = new ArrayList<>();
            if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".link") != null)
                lore.add(TextComponent.formatText("&7Currently: &a" + KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".link") +"&7."));
            else
                lore.add(TextComponent.formatText("&7Currently: &cNot set&7."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Clicking this option will"));
            lore.add(TextComponent.formatText("&7allow you to set a link"));
            lore.add(TextComponent.formatText("&7that players will receive"));
            lore.add(TextComponent.formatText("&7when attempting to purchase"));
            lore.add(TextComponent.formatText("&7this kit."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(12, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&a&lSet kit price"));
            lore = new ArrayList<>();
            if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".eco") != null)
                lore.add(TextComponent.formatText("&7Currently: &a$" + Arconix.pl().format().formatEconomy(KitPreview.getInstance().getConfig().getDouble("data.kit." + kit.getName() + ".eco")) +"&7."));
            else
                lore.add(TextComponent.formatText("&7Currently: &cNot set&7."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Clicking this option will"));
            lore.add(TextComponent.formatText("&7allow you to set a price"));
            lore.add(TextComponent.formatText("&7that players will be able to"));
            lore.add(TextComponent.formatText("&7purchase this kit for"));
            lore.add(TextComponent.formatText("&7requires &aVault&7."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(14, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&a&lToggle Delay"));
            lore = new ArrayList<>();
            if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".delay") != null) {
                lore.add(TextComponent.formatText("&7Currently: &aEnabled&7."));
            } else {
                lore.add(TextComponent.formatText("&7Currently &cDisabled&7."));
            }
            lore.add("");
            lore.add(TextComponent.formatText("&7Clicking this option will"));
            lore.add(TextComponent.formatText("&7allow you to enable or disable"));
            lore.add(TextComponent.formatText("&7the kits delay set in the"));
            lore.add(TextComponent.formatText("&7essentials config."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(16, alli);

            p.openInventory(i);
            defineInstance("selling");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void gui() {
        try {
            Inventory i = Bukkit.createInventory(null, 27, Arconix.pl().format().formatTitle("&8GUI Options for &a" + kit.getShowableName() + "&8."));

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

            ItemStack exit = new ItemStack(Material.valueOf(KitPreview.getInstance().getConfig().getString("Interfaces.Exit Icon")), 1);
            ItemMeta exitmeta = exit.getItemMeta();
            exitmeta.setDisplayName(Lang.EXIT.getConfigValue());
            exit.setItemMeta(exitmeta);

            ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            ItemStack back = head2;
            if (!KitPreview.getInstance().v1_7)
                back = Arconix.pl().getGUI().addTexture(head2, "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23");
            SkullMeta skull2Meta = (SkullMeta) back.getItemMeta();
            if (KitPreview.getInstance().v1_7)
                skull2Meta.setOwner("MHF_ArrowLeft");
            back.setDurability((short) 3);
            skull2Meta.setDisplayName(Lang.BACK.getConfigValue());
            back.setItemMeta(skull2Meta);

            i.setItem(0, back);
            i.setItem(8, exit);

            ItemStack alli = new ItemStack(Material.DIAMOND_HELMET);
            ItemMeta allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lSet Title"));
            ArrayList<String> lore = new ArrayList<>();
            if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".title") != null)
                lore.add(TextComponent.formatText("&7Currently: &a" + KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".title") +"&7."));
            else
                lore.add(TextComponent.formatText("&7Currently: &cNot set&7."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Left-Click: &9to set"));
            lore.add(TextComponent.formatText("&9the kit title for holograms"));
            lore.add(TextComponent.formatText("&9and the kit / kits GUIs."));
            lore.add(TextComponent.formatText(""));
            lore.add(TextComponent.formatText("&7Right-Click: &9to reset."));
            allmeta.setLore(lore);

            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(11, alli);

            alli = new ItemStack(Material.DIAMOND_HELMET);
            if (KitPreview.getInstance().getConfig().getItemStack("data.kit." + kit.getName() + ".displayitemkits") != null) {
                alli = KitPreview.getInstance().getConfig().getItemStack("data.kit." + kit.getName() + ".displayitemkits");
            }
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lSet /kits DisplayItem"));
            lore = new ArrayList<>();
            if (KitPreview.getInstance().getConfig().getItemStack("data.kit." + kit.getName() + ".displayitemkits") != null) {
                ItemStack is = KitPreview.getInstance().getConfig().getItemStack("data.kit." + kit.getName() + ".displayitemkits");
                lore.add(TextComponent.formatText("&7Currently set to: &a" + is.getType().toString() + "&7."));
            } else {
                lore.add(TextComponent.formatText("&7Currently &cDisabled&7."));
            }
            lore.add("");
            lore.add(TextComponent.formatText("&7Right-Click to: &9Set a"));
            lore.add(TextComponent.formatText("&9display item for this kit"));
            lore.add(TextComponent.formatText("&9to the item in your hand."));
            lore.add("");
            lore.add(TextComponent.formatText("&7Left-Click to: &9Remove the item."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(13, alli);

            alli = new ItemStack(Material.COAL);
            allmeta = alli.getItemMeta();
            allmeta.setDisplayName(TextComponent.formatText("&9&lBlacklist kit"));
            lore = new ArrayList<>();
            if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".blacklisted") != null) {
                lore.add(TextComponent.formatText("&7Currently: &aBlacklisted&7."));
            } else {
                lore.add(TextComponent.formatText("&7Currently: &cNot blacklisted&7."));
            }
            lore.add("");
            lore.add(TextComponent.formatText("&7A blacklisted kit will not"));
            lore.add(TextComponent.formatText("&7show up in the /kits gui."));
            lore.add(TextComponent.formatText("&7This is usually optimal for"));
            lore.add(TextComponent.formatText("&7preventing players from seeing"));
            lore.add(TextComponent.formatText("&7non obtainable kits or starter kits."));
            allmeta.setLore(lore);
            alli.setItemMeta(allmeta);

            i.setItem(15, alli);

            p.openInventory(i);
            defineInstance("gui");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setKitsDisplayItem(boolean type) {
        try {
            gui();
            if (type) {
                ItemStack is = p.getItemInHand();
                if (is == null || is.getType() == Material.AIR) {
                    p.sendMessage(TextComponent.formatText(KitPreview.getInstance().references.getPrefix() + "&8You must be holding an item to use this function."));
                    return;
                }
                KitPreview.getInstance().getConfig().set("data.kit." + kit.getName() + ".displayitemkits", is);
                KitPreview.getInstance().saveConfig();
                p.sendMessage(TextComponent.formatText(KitPreview.getInstance().references.getPrefix() + "&8Custom Item Display set for kit &a" + kit.getShowableName() + "&8."));
            } else {
                KitPreview.getInstance().getConfig().set("data.kit." + kit.getName() + ".displayitemkits", null);
                KitPreview.getInstance().saveConfig();
                p.sendMessage(TextComponent.formatText(KitPreview.getInstance().references.getPrefix() + "&8Custom Item Display removed from kit &a" + kit.getShowableName() + "&8."));
            }

        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void createCommand() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(KitPreview.getInstance(), () -> {
                if (KitPreview.getInstance().inEditor.containsKey(p.getUniqueId())) {
                    if (KitPreview.getInstance().inEditor.get(p.getUniqueId()) == "command") {
                        p.sendMessage(TextComponent.formatText(KitPreview.getInstance().references.getPrefix() + "Editing Timed out."));
                        KitPreview.getInstance().inEditor.remove(p.getUniqueId());
                    }
                }
            }, 500L);
            p.closeInventory();
            defineInstance("command");
            p.sendMessage("");
            p.sendMessage(TextComponent.formatText("Please type a command. Example: &aeco give {player} 1000"));
            p.sendMessage(TextComponent.formatText("do not include a &a/"));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void saveKit(Player p, Inventory i) {
        try {
            ItemStack[] items = i.getContents();
            int num = 0;
            for (ItemStack item : items) {
                if (num < 10 || num == 17 || num == 36) {
                    items[num] = null;
                }
                num++;
            }

            items = Arrays.copyOf(items, items.length - 10);

            KitPreview.getInstance().hooks.saveKit(p, kit.getName(), items);
            p.sendMessage(TextComponent.formatText(KitPreview.getInstance().references.getPrefix() + "&8Changes to &a" + kit.getShowableName() + " &8saved successfully."));
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void blacklist() {
        try {
            if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".blacklisted") == null) {
                KitPreview.getInstance().getConfig().set("data.kit." + kit.getName() + ".blacklisted", true);
                KitPreview.getInstance().saveConfig();
            } else {
                KitPreview.getInstance().getConfig().set("data.kit." + kit.getName() + ".blacklisted", null);
                KitPreview.getInstance().saveConfig();
            }
            gui();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void toggleDelay() {
        try {
            if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.getName() + ".delay") == null) {
                KitPreview.getInstance().getConfig().set("data.kit." + kit.getName() + ".delay", true);
                KitPreview.getInstance().saveConfig();
            } else {
                KitPreview.getInstance().getConfig().set("data.kit." + kit.getName() + ".delay", null);
                KitPreview.getInstance().saveConfig();
            }
            selling();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setNoSale() {
        try {
            KitPreview.getInstance().getConfig().set("data.kit." + kit.getName() + ".eco", null);
            KitPreview.getInstance().getConfig().set("data.kit." + kit.getName() + ".link", null);
            KitPreview.getInstance().saveConfig();
            KitPreview.getInstance().holo.updateHolograms();
            selling();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void setTitle(boolean type) {
        try {
            if (type) {
                p.closeInventory();
                Bukkit.getScheduler().scheduleSyncDelayedTask(KitPreview.getInstance(), () -> {
                    if (KitPreview.getInstance().inEditor.containsKey(p.getUniqueId())) {
                        if (KitPreview.getInstance().inEditor.get(p.getUniqueId()) == "title") {
                            p.sendMessage(TextComponent.formatText(KitPreview.getInstance().references.getPrefix() + "Editing Timed out."));
                            KitPreview.getInstance().inEditor.remove(p.getUniqueId());
                        }
                    }
                }, 200L);
                defineInstance("title");
                p.sendMessage("");
                p.sendMessage(TextComponent.formatText("Type a title for the GUI. Example: &aThe Cool Kids Kit"));
                p.sendMessage("");
            } else {
                KitPreview.getInstance().getConfig().set("data.kit." + kit.getName() + ".title", null);
                KitPreview.getInstance().saveConfig();
                KitPreview.getInstance().holo.updateHolograms();
                gui();
            }
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void editPrice() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(KitPreview.getInstance(), () -> {
                if (KitPreview.getInstance().inEditor.containsKey(p.getUniqueId())) {
                    if (KitPreview.getInstance().inEditor.get(p.getUniqueId()) == "price") {
                        p.sendMessage(TextComponent.formatText(KitPreview.getInstance().references.getPrefix() + "Editing Timed out."));
                        KitPreview.getInstance().inEditor.remove(p.getUniqueId());
                    }
                }
            }, 200L);
            p.closeInventory();
            defineInstance("price");
            p.sendMessage("");
            p.sendMessage(TextComponent.formatText("Please type a price. Example: &a50000"));
            p.sendMessage(TextComponent.formatText("&cUse only numbers."));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }

    public void editLink() {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(KitPreview.getInstance(), () -> {
                if (KitPreview.getInstance().inEditor.containsKey(p.getUniqueId())) {
                    if (KitPreview.getInstance().inEditor.get(p.getUniqueId()) == "link") {
                        p.sendMessage(TextComponent.formatText(KitPreview.getInstance().references.getPrefix() + "Editing Timed out."));
                        KitPreview.getInstance().inEditor.remove(p.getUniqueId());
                    }
                }
            }, 200L);
            p.closeInventory();
            defineInstance("link");
            p.sendMessage("");
            p.sendMessage(TextComponent.formatText("Please type a link. Example: &ahttp://buy.viscernity.com/"));
            p.sendMessage("");
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }
    }


}
