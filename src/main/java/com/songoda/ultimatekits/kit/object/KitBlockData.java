package com.songoda.ultimatekits.kit.object;

import com.songoda.ultimatekits.UltimateKits;
import org.bukkit.Location;

public class KitBlockData {

    private boolean hologram, particles, items;

    private boolean itemOverride;

    private KitType type;

    private final Kit kit;

    private final Location location;

    public KitBlockData(Kit kit, Location location, KitType type, boolean hologram, boolean particles,  boolean items, boolean itemOverride) {
        this.kit = kit;
        this.location = location;
        this.hologram = hologram;
        this.particles = particles;
        this.items = items;
        this.itemOverride = itemOverride;
        this.type = type;
    }

    public void reset() {
        setShowHologram(false);
        setDisplayingItems(false);
        setHasParticles(false);
        UltimateKits.getInstance().displayitem.displayItem(this);
        UltimateKits.getInstance().holo.updateHolograms();
    }

    public KitBlockData(Kit kit, Location location) {
        this(kit, location, KitType.PREVIEW, false, false, false, false);
    }

    public Kit getKit() {
        return kit;
    }

    public Location getLocation() {
        return location.clone();
    }

    public boolean showHologram() {
        return hologram;
    }

    public void setShowHologram(boolean hologram) {
        this.hologram = hologram;
    }

    public boolean hasParticles() {
        return particles;
    }

    public void setHasParticles(boolean particles) {
        this.particles = particles;
    }

    public boolean isDisplayingItems() {
        return items;
    }

    public void setDisplayingItems(boolean items) {
        this.items = items;
    }

    public boolean isItemOverride() {
        return itemOverride;
    }

    public void setItemOverride(boolean itemOverride) {
        this.itemOverride = itemOverride;
    }

    public KitType getType() {
        return type;
    }

    public void setType(KitType type) {
        this.type = type;
    }

}