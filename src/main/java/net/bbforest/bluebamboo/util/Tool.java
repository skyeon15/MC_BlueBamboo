package net.bbforest.bluebamboo.util;
import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.commands.CommandTest2;
import net.bbforest.bluebamboo.commands.Test;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

public class Tool {
    public static void registerCommands(){
        //명령어 등록 1명령어 1코드
        registerCommand("test", new Test());
        registerCommand("test2", new CommandTest2());
    }
    private static void registerCommand(String cmd, CommandExecutor executor){
        PluginCommand command = EKE.getPlugin().getCommand(cmd);
        //plugin.yml 예외 처리
        if(command != null){
            command.setExecutor(executor);
        }
    }
}
