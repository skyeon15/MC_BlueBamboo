package net.bbforest.bluebamboo.util;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public enum Permission {
    ADMIN("admin"),
    CMD_RELOAD("reload"),
    CMD_BROADCAST("broadcast"),
    CMD_INVENTORY("inventory"),
    COLOR("color"),
    ;

    private final String permission;

    Permission(String permission) {
        this.permission = "bluebamboo." + permission;
    }

    public boolean hasPerm(@NotNull CommandSender sender) {
        return hasPerm(sender, true);
    }

    public boolean hasPerm(@NotNull CommandSender sender, boolean sendFeedback) {
        boolean has = sender.hasPermission(permission);
        //권한이 없고 피드백이 있으면
        if (!has && sendFeedback) {
            Tool.Error.PERM.send(sender);
        }
        return has;
    }
}
