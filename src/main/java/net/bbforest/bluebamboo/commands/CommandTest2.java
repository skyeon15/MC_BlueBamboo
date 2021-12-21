package net.bbforest.bluebamboo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandTest2 implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] strings) {
        if(sender instanceof Player)
        {
            sender.sendMessage("플레이어임");
        }else{
            sender.sendMessage("콘솔임");
        }
        return true;
    }
}
