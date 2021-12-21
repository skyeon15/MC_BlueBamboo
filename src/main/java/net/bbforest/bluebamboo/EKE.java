package net.bbforest.bluebamboo;
import net.bbforest.bluebamboo.util.Tool;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class EKE extends JavaPlugin {

    private static EKE plugin;

    public static EKE getPlugin(){
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        Bukkit.getConsoleSender().sendMessage("§b파란대나무 활성화!");

        //명령어 등록 호출
        Tool.registerCommands();
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§b파란대나무 활성화!");
    }
}
