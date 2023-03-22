package net.bbforest.bluebamboo.commands;

import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.util.Permission;
import net.bbforest.bluebamboo.util.Tool;
import net.bbforest.bluebamboo.util.Updater;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BlueBamboo implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            Tool.sendMessageOri(sender, "======= &f[&b파란대나무&f] =======\n" +
                    "\"하늘판다가 먹는 파란대나무\"\n도움말은 &e/bb help &f를 입력해보세요!" +
                    "\n제작자 : &b에케(@skyeon15)&f\nhttps://bbforest.net");
            return true;
        }
        switch (args[0]) {
            case "help" -> {
                Tool.sendMessageOri(sender, "======= &f[&b파란대나무&f 도움말] =======\n" +
                        "&e/bb help &f파란대나무에게 도움을 받아요.\n" +
                        "&e/bb reload &f플러그인 설정을 다시 불러와요.\n" +
                        "&e/bbc <할말> &f서버 전체에 공지를 보내요.\n" +
                        "&e/binv <save|view> &f인벤토리 스냅샷을 저장하고 보여줘요.");
            }
            case "reload", "r" -> {
                if (Permission.CMD_RELOAD.hasPerm(sender)) {
                    EKE.getPlugin().registerConfig();
                    EKE.getPlugin().reloadConfig();
                    Tool.sendMessage(sender, "설정을 다시 불러왔어요!");
                }
            }
            case "update", "u" -> {
                if(Permission.ADMIN.hasPerm(sender)){
                    try{
                        Updater.Version ver = null;
                        ver = Updater.defaultUpdater.getLatestVersion();

                        if(args.length >= 2 && (args[1].equals("force")||args[1].equals("f"))){
                            ver.download();
                            ver.update();
                            Tool.sendMessage(sender, "강제로 털었어요.");
                            return true;
                        }

                        if(ver != null){
                            if(ver.name.equals(EKE.getPlugin().getDescription().getVersion())){
                                Tool.sendMessage(sender, "이미 최신버전이에요.");
                            }else{
                                Tool.sendMessage(sender,"업데이트를 진행할게요. 최신버전 : " + ver.name);
                                ver.download();
                                ver.update();
                            }
                        }
                    }catch(Exception e){
                        Tool.sendMessage(sender, "업데이트하다가 어쨌든 오류가 났어요.");
                    }
                }
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("help", "reload", "update");
        }

        return Collections.emptyList();
    }
}
