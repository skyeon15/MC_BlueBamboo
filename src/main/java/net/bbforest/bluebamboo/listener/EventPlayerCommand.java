package net.bbforest.bluebamboo.listener;

import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.util.Permission;
import net.bbforest.bluebamboo.util.Tool;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class EventPlayerCommand implements Listener {
    //플레이어가 명령어를 입력하면
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage(); // /bb help
        String[] split = message.split(" ");
        String label = split[0]; // /bb
        label = label.substring(1); // tp
        Player player = event.getPlayer();

        //config 가져오기
        FileConfiguration config = EKE.getPlugin().getConfig();
        boolean enabled = config.getBoolean("blockPluginCommand.enabled");

        if (enabled) {
            List<String> plugins = config.getStringList("blockPluginCommand.list");
            if (!Permission.ADMIN.hasPerm(player, false)) {
                PluginCommand pluginCommand = Bukkit.getPluginCommand(label);
                //없는 명령어면 리턴
                if (pluginCommand == null) return;
                //명령어의 플러그인 이름 확인
                Plugin plugin = pluginCommand.getPlugin();
                //config에서 불러온 list에 일치하는 플러그인 이름이 있으면 event 취소
                if (plugins.contains(plugin.getName())) {
                    Bukkit.dispatchCommand(player, "넌못지나간다");
                    event.setCancelled(true);
                }
            }
        }
    }
}
