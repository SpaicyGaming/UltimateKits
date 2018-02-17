package com.songoda.kitpreview;

import com.songoda.arconix.Arconix;
import com.songoda.kitpreview.API.*;
import com.songoda.kitpreview.Handlers.*;
import com.songoda.kitpreview.Events.*;
import com.songoda.kitpreview.Hooks.EssentialsHook;
import com.songoda.kitpreview.Hooks.Hooks;
import com.songoda.kitpreview.Hooks.UltimateCoreHook;
import com.songoda.kitpreview.Kits.Kit;
import com.songoda.kitpreview.Utils.ConfigWrapper;
import com.songoda.kitpreview.Utils.Debugger;
import com.songoda.kitpreview.Utils.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class KitPreview extends JavaPlugin {
    public static CommandSender console = Bukkit.getConsoleSender();

    public ConfigWrapper langFile = new ConfigWrapper(this, "", "lang.yml");

    public References references = null;

    public Hooks hooks = null;
    public HologramHandler holo = null;
    public SettingsManager sm;

    public boolean v1_7 = Bukkit.getServer().getClass().getPackage().getName().contains("1_7");
    public boolean v1_8 = Bukkit.getServer().getClass().getPackage().getName().contains("1_8");

    private static KitPreview INSTANCE;

    public Map<Player, String> inEditor = new HashMap<>();
    public Map<Player, Block> editing = new HashMap<>();
    public Map<Player, Kit> editingKit = new HashMap<>();

    public Map<Player, Integer> page = new HashMap<>();

    public Map<Player, String> buy = new HashMap<>();
    public Map<String, String> kits = new HashMap<>();

    public Map<Player, Kit> inKit = new HashMap<>();

    public List<UUID> xyz = new ArrayList<>();
    public List<UUID> xyz2 = new ArrayList<>();
    public List<UUID> xyz3 = new ArrayList<>();

    public void onEnable() {
        INSTANCE = this;
        console.sendMessage(Arconix.pl().format().formatText("&a============================="));
        console.sendMessage(Arconix.pl().format().formatText("&7KitPreview" + this.getDescription().getVersion()  + " by &5Brianna <3!"));
        console.sendMessage(Arconix.pl().format().formatText("&7Action: &aEnabling&7..."));
        setupConfig();
        langFile.createNewFile("Loading language file", "KitPreview language file");
        loadLanguageFile();

        hook();

        references = new References();

        holo = new HologramHandler();
        new ParticleHandler();
        new DisplayItemHandler();
        new DailyHandler();
        sm = new SettingsManager();
        sm.updateSettings();

        try {
            Update update = new Update(this);
            update.start();
            Bukkit.getLogger().info( "MCUpdate enabled and loaded");
        } catch (IOException e) {
            Bukkit.getLogger().info("Failed initialize MCUpdate");
        }


        try {
            this.getCommand("Kits").setExecutor(new CommandHandler());
            this.getCommand("Kit").setExecutor(new CommandHandler());
        } catch (Exception e) {
            console.sendMessage(Arconix.pl().format().formatText("&7The &a/kit&7 and &a/kits &7features have been &cdisabled&7."));
        }
        console.sendMessage(Arconix.pl().format().formatText("&a============================="));

        this.getCommand("KitPreview").setExecutor(new CommandHandler());
        this.getCommand("PreviewKit").setExecutor(new CommandHandler());

        getServer().getPluginManager().registerEvents(new BlockListeners(), this);
        getServer().getPluginManager().registerEvents(new ChatListeners(), this);
        if (!v1_7 ) {
            getServer().getPluginManager().registerEvents(new InteractListeners(), this);
        }
        getServer().getPluginManager().registerEvents(new InventoryListeners(), this);
        getServer().getPluginManager().registerEvents(new QuitListeners(), this);
        getServer().getPluginManager().registerEvents(new SignListeners(), this);
    }

    public void hook() {
        if (getServer().getPluginManager().getPlugin("Essentials") != null) {
            hooks = new EssentialsHook();
        } else if (getServer().getPluginManager().getPlugin("UltimateCore") != null) {
            hooks = new UltimateCoreHook();
        }
    }

    public void onDisable() {
        xyz.clear();
        xyz2.clear();
        xyz3.clear();
        console.sendMessage(Arconix.pl().format().formatText("&a============================="));
        console.sendMessage(Arconix.pl().format().formatText("&7KitPreview " + this.getDescription().getVersion()  + " by &5Brianna <3!"));
        console.sendMessage(Arconix.pl().format().formatText("&7Action: &cDisabling&7..."));
        console.sendMessage(Arconix.pl().format().formatText("&a============================="));
    }

    private void setupConfig() {
        try {
            sm.updateSettings();
            getConfig().options().copyDefaults(true);
            saveConfig();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

    }

    public void loadLanguageFile() {
        try {
            Lang.setFile(langFile.getConfig());

            for (final Lang value : Lang.values()) {
                langFile.getConfig().addDefault(value.getPath(), value.getDefault());
            }

            langFile.getConfig().options().copyDefaults(true);
            langFile.saveConfig();
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

    }

    public void reload() {
        try {
            langFile.createNewFile("Loading language file", "KitPreview language file");
            loadLanguageFile();
            references = new References();
            reloadConfig();
            saveConfig();
            holo.updateHolograms(true);
        } catch (Exception ex) {
            Debugger.runReport(ex);
        }

    }

    public static KitPreview pl() {
        return INSTANCE;
    }
    public static KitPreview getInstance() {
        return INSTANCE;
    }

}

