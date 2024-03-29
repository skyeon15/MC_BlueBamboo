package net.bbforest.bluebamboo.util;
import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.commands.BlueBamboo;
import net.bbforest.bluebamboo.commands.Broadcast;
import net.bbforest.bluebamboo.commands.Motd;
import net.bbforest.bluebamboo.commands.SaveInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.logging.Logger;

public class Tool {
    public static void registerCommands(){
        //명령어 등록 1명령어 1코드
        registerCommand("bluebamboo", new BlueBamboo());
        registerCommand("broadcast", new Broadcast());
        registerCommand("inventory", new SaveInventory());
        registerCommand("motd", new Motd());
    }
    private static void registerCommand(String cmd, Object object){
        PluginCommand command = EKE.getPlugin().getCommand(cmd);
        //plugin.yml 예외 처리
        if(command != null){
            if(object instanceof CommandExecutor executor)
                command.setExecutor(executor);
            if(object instanceof TabCompleter tabCompleter)
                command.setTabCompleter(tabCompleter);
        }
    }

    /**
     * 메시지 출력
     * @param msg 메시지 내용
     */
    public static void sendMessage(CommandSender sender, String msg){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&r[&b파란대나무&r] " + msg));
    }

    /**
     * 메시지 출력
     * @param msg 메시지 내용
     */
    public static void sendMessageOri(CommandSender sender, String msg){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', (sender instanceof Player ? "" : "파란대나무") + msg));
    }

    /**
     * 콘솔 로그 출력
     * <p>INFO : 정보</p>
     * <p>WARN : 경고</p>
     * <p>ERROR : 오류</p>
     */
    public enum Log{
        MSG, INFO, WARN, ERROR;
        public void send(String msg){
            Logger logger = EKE.getPlugin().getLogger();
            switch(this){
                case MSG -> sendMessage(Bukkit.getConsoleSender(), msg);
                case INFO -> logger.info(msg);
                case WARN -> logger.warning(msg);
                case ERROR -> logger.severe(msg);
            }
        }
    }

    /**
     * 오류 메시지 출력
     * <p>PERM : 권한 부족</p>
     */
    public enum Error {
        PERM;
        public void send(CommandSender sender){
            switch(this){
                case PERM ->
                        sendMessage(sender, "&c권한이 없습니다.");
            }
        }
    }

    /**
     * 닉네임 -> UUID 변환
     * @param input 닉네임
     */
    @Nullable
    public static OfflinePlayer getOfflinePlayerUUID(@NotNull String input){
        if(isUUID(input)){
            return Bukkit.getOfflinePlayer(UUID.fromString(input));
        }
        return Bukkit.getOfflinePlayerIfCached(input);
    }

    /**
     * 입력한 값이 UUID인지 확인
     * @param uuid
     */
    @SuppressWarnings("all")
    public static boolean isUUID(@NotNull String uuid){
        try {
            UUID.fromString(uuid);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
