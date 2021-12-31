package net.bbforest.bluebamboo.commands;

import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.util.Permission;
import net.bbforest.bluebamboo.util.Tool;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Broadcast implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //권한 없으면
        if (!Permission.CMD_BROADCAST.hasPerm(sender)) return true;

        //아무것도 안 적으면 return
        if (args.length == 0) {
            return false;
        }

        //설정에서 접두사 불러오기
        FileConfiguration config = EKE.getPlugin().getConfig();

        switch (args[0]){
            case "set" -> {
                //권한 없으면
                if (!Permission.CMD_BROADCASTSET.hasPerm(sender)) return true;
                StringBuilder prefix = new StringBuilder();
                for (int i = 1; i <= args.length; i++) {
                    prefix.append(args[i-1]).append(" ");
                }
                //맨 뒤 공백 제거
                prefix = new StringBuilder(prefix.substring(0, prefix.length() - 1));
                config.set("broadcast-prefix", prefix);

                Tool.sendMessage(sender, "접두사를 변경했어요: " + prefix);
                return true;
            }
        }

        //args -> String 변환
        StringBuilder msg = new StringBuilder();
        for (String arg : args) {
            msg.append(arg).append(" ");
        }

        //맨 뒤 공백 제거
        msg = new StringBuilder(msg.substring(0, msg.length() - 1));

        //설정에서 불러온 접두사 추가
        String prefix_msg = config.getString("broadcast-prefix") + msg;

        //색 권한 있으면
        if (Permission.COLOR.hasPerm(sender)) {
            //컬러코드 색으로 변환
            Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(prefix_msg));
        } else {
            //컬러코드 변환 안 함
            Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(prefix_msg));
        }
        return true;
    }
}
