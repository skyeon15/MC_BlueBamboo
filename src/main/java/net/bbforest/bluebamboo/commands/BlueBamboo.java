package net.bbforest.bluebamboo.commands;
import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.util.Tool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlueBamboo implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0){
            Tool.sendMessage(sender, "\"하늘판다가 먹는 파란대나무\"\n도움말은 &e/bb help &f를 입력해보세요!" +
                    "\n제작자 : &b에케(@skyeon15)&f\nhttps://bbforest.net");
            return true;
        }
        switch (args[0]){
            case "reload" -> {
                if(sender.hasPermission("eke.admin")) {
                    EKE.getPlugin().registerConfig();
                    EKE.getPlugin().reloadConfig();
                    Tool.sendMessage(sender, "설정을 다시 불러왔어요!");
                }else{
                    Tool.Error.PERM.send(sender);
                    return true;
                }
            }
        }
        return true;
    }
}
