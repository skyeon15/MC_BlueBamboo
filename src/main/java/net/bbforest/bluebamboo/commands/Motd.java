package net.bbforest.bluebamboo.commands;

import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.util.Permission;
import net.bbforest.bluebamboo.util.Tool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.Bukkit.getServer;

public class Motd implements CommandExecutor {
    public  boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        //권한 없으면
        if (!Permission.CMD_MOTD.hasPerm(sender)) return true;

        //아무것도 안 적으면 return
        if (args.length == 0) {
            return false;
        }

        //args -> String 변환
        StringBuilder msg = new StringBuilder();
        for (String arg : args) {
            msg.append(arg).append(" ");
        }

        //맨 뒤 공백 제거
        msg = new StringBuilder(msg.substring(0, msg.length() - 1));

        // getServer().setMotd(msg);
        Tool.sendMessage(sender, "환영 메시지를 변경했어요: " + msg);
        return true;
    }
}
