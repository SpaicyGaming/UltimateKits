package com.songoda.kitpreview.Events;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.KitPreview;
import com.songoda.kitpreview.Kits.BlockEditor;
import com.songoda.kitpreview.Kits.Kit;
import com.songoda.kitpreview.Lang;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;

/**
 * Created by songoda on 2/24/2017.
 */
public class BlockListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        try {
            if (!event.isCancelled()) {
                Block b = event.getBlock();
                String loc = Arconix.pl().serialize().serializeLocation(b);
                if (KitPreview.getInstance().getConfig().getString("data.block." + loc) != null) {
                    Kit kit = new Kit(b);
                    Player p = event.getPlayer();
                    kit.removeKitFromBlock(p);
                }
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockPlaceEvent(BlockPlaceEvent e) {
        try {
            if (!e.isCancelled()) {
                Block b = e.getBlockAgainst();
                    String loc = Arconix.pl().serialize().serializeLocation(b);
                    if (KitPreview.getInstance().getConfig().getString("data.block." + loc) != null) {
                        e.setCancelled(true);
                }
            }
        } catch (Exception ee) {
            Debugger.runReport(ee);
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        try {
            boolean chand = true;
            if (!KitPreview.getInstance().v1_7 && !KitPreview.getInstance().v1_8) {
                if (e.getHand() != EquipmentSlot.HAND) {
                    chand = false;
                }
            }
            Block b = e.getClickedBlock();
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (chand) {
                    String loc = Arconix.pl().serialize().serializeLocation(b);
                    if (KitPreview.getInstance().getConfig().getString("data.block." + loc) != null) {
                        Player p = e.getPlayer();

                        String type = "";
                        if (KitPreview.getInstance().getConfig().getString("data.type." + loc) != null) {
                            type = KitPreview.getInstance().getConfig().getString("data.type." + loc);
                        }

                        if (!p.isSneaking()) {
                            e.setCancelled(true);
                            Kit kit = new Kit(loc);

                            if (type.equals("crate") && e.isCancelled()) {
                                boolean haskey = false;
                                if (p.getItemInHand() != null) {
                                    if (p.getItemInHand().getType() != null) {
                                        if (p.getItemInHand().getType() == Material.TRIPWIRE_HOOK) {
                                            haskey = true;
                                        }
                                    }
                                }
                                if (haskey) {
                                    e.setCancelled(true);
                                    kit.give(p, true, false, false);
                                } else {
                                    p.sendMessage(Arconix.pl().format().formatText(KitPreview.getInstance().references.getPrefix() + Lang.NOT_KEY.getConfigValue()));
                                }
                            } else if (type.equals("daily")) {
                                if (!p.hasPermission("essentials.kits." + kit.dname.toLowerCase())) {
                                    p.sendMessage(KitPreview.getInstance().references.getPrefix() + Lang.NO_PERM.getConfigValue());
                                } else {
                                    if (KitPreview.getInstance().hooks.isReady(p, kit.dname)) {
                                        kit.give(p, false, false, false);
                                        KitPreview.getInstance().hooks.updateDelay(p, kit.dname);
                                    } else {
                                        long time = KitPreview.getInstance().hooks.getNextUse(kit.dname, p);
                                        long now = System.currentTimeMillis();

                                        long then = time - now;
                                        p.sendMessage(KitPreview.getInstance().references.getPrefix() + Lang.NOT_YET.getConfigValue(Arconix.pl().format().readableTime(then)));
                                    }
                                }
                            } else if (KitPreview.getInstance().getConfig().getString("data.kit." + kit.dname + ".link") != null && e.isCancelled() || KitPreview.getInstance().getConfig().getString("data.kit." + kit.dname + ".eco") != null && e.isCancelled()) {
                                kit.buy(p);
                            } else {
                                kit.display(p, false);
                            }
                        }
                    }
                }
            }
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                String loc = Arconix.pl().serialize().serializeLocation(b);
                if (KitPreview.getInstance().getConfig().getString("data.block." + loc) != null) {
                    if (b.getState() instanceof InventoryHolder || b.getType() == Material.ENDER_CHEST) {
                        e.setCancelled(true);
                    }
                }
                if (chand) {
                    Player p = e.getPlayer();
                    if (KitPreview.getInstance().getConfig().getString("data.block." + loc) != null) {
                        if (p.isSneaking() && p.hasPermission("kitpreview.admin")) {
                            BlockEditor edit = new BlockEditor(b, p);
                            edit.open();
                        } else {
                            Kit kit = new Kit(loc);
                            boolean haskey = false;
                            if (p.getItemInHand() != null) {
                                if (p.getItemInHand().getType() != null) {
                                    if (p.getItemInHand().getType() == Material.TRIPWIRE_HOOK) {
                                        haskey = true;
                                    }
                                }
                            }
                            if (haskey) {
                                e.setCancelled(true);
                                kit.give(p, true, false, false);
                            } else {
                                kit.display(p, false);
                            }
                        }
                    }
                }
            }
        } catch (Exception x) {
            Debugger.runReport(x);
        }
    }
}
