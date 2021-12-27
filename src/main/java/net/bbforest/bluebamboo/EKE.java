package net.bbforest.bluebamboo;
import net.bbforest.bluebamboo.listener.EventPlayerCommand;
import net.bbforest.bluebamboo.util.SaveInventoryTool;
import net.bbforest.bluebamboo.util.Tool;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EKE extends JavaPlugin {

    private static EKE plugin;

    public static EKE getPlugin(){
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        Tool.Log.MSG.send("파란대나무숲에 들어왔어요!");
        registerConfig();
        //명령어 등록 호출
        Tool.registerCommands();
        SaveInventoryTool.autoSaveInventoryData();

        //이벤트 리스너 등록
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new EventPlayerCommand(), this);
    }

    public void onDisable() {
        Tool.Log.MSG.send("파란대나무숲에서 나갔어요...");
    }

    public void registerConfig(){
        File file = new File(getDataFolder() + "/config.yml");
        if(!file.exists()){
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }
    }
}
