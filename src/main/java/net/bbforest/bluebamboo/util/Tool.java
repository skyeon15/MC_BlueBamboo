package net.bbforest.bluebamboo.util;
import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.commands.BlueBamboo;
import net.bbforest.bluebamboo.commands.Broadcast;
import net.bbforest.bluebamboo.commands.SaveInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

public class Tool {
    public static void registerCommands(){
        //명령어 등록 1명령어 1코드
        registerCommand("bluebamboo", new BlueBamboo());
        registerCommand("broadcast", new Broadcast());
        registerCommand("inventory", new SaveInventory());
    }
    private static void registerCommand(String cmd, CommandExecutor executor){
        PluginCommand command = EKE.getPlugin().getCommand(cmd);
        //plugin.yml 예외 처리
        if(command != null){
            command.setExecutor(executor);
        }
    }

    /**
     * 메시지 출력
     * @param msg 메시지 내용
     */
    public static void sendMessage(CommandSender sender, String msg){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&b파란대나무&f] " + msg));
    }

    /**
     * 오류 메시지 출력
     * <p>PERM : 권한 부족</p>
     */
    public enum Error {
        PERM;
        public void send(CommandSender sender){
            switch (this){
                case PERM ->
                        sendMessage(sender, "&c권한이 없습니다.");
            }
        }
    }
}
