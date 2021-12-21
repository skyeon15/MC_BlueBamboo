package net.bbforest.bluebamboo.commands;
import net.bbforest.bluebamboo.util.Tool;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Broadcast implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //권한 없으면
        if(!sender.hasPermission("eke.broadcast")){
            Tool.Error.PERM.send(sender);
            return true;
        }
        //아무것도 안 적으면 return
        if(args.length == 0){
            return false;
        }

        //args -> String 변환
        StringBuilder msg = new StringBuilder();
        for(String arg : args){
            msg.append(arg).append(" ");
        }

        //맨 뒤 공백 제거
        msg = new StringBuilder(msg.substring(0, msg.length() - 1));

        //색 권한 있으면
        if(sender.hasPermission("eke.color")){
            //컬러코드 색으로 변환
            Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(msg.toString()));
        }else{
            //컬러코드 변환 안 함
            Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(msg.toString()));
        }
        return true;
    }
}
