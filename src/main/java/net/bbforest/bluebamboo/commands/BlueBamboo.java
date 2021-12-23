package net.bbforest.bluebamboo.commands;
import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.util.Tool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlueBamboo implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0){
            Tool.sendMessageOri(sender, "======= &f[&b파란대나무&f] =======\n" +
                    "\"하늘판다가 먹는 파란대나무\"\n도움말은 &e/bb help &f를 입력해보세요!" +
                    "\n제작자 : &b에케(@skyeon15)&f\nhttps://bbforest.net");
            return true;
        }
        switch (args[0]){
            case "help" -> {
                Tool.sendMessageOri(sender, "======= &f[&b파란대나무&f 도움말] =======\n" +
                        "&e/bb help &f파란대나무에게 도움을 받아요.\n" +
                        "&e/bb reload &f플러그인 설정을 다시 불러와요.\n" +
                        "&e/bbc <할말> &f서버 전체에 공지를 보내요.\n" +
                        "&e/binv <save|view> &f인벤토리 스냅샷을 저장하고 보여줘요.");
            }
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
