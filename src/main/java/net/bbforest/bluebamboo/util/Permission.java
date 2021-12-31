package net.bbforest.bluebamboo.util;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public enum Permission {
    ADMIN("admin"),
    CMD_RELOAD("reload"),
    CMD_BROADCAST("broadcast"),
    CMD_BROADCASTSET("broadcast.set"),
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
        //관리자 권한이 있으면 권한 확인 넘기기
        if (sender.hasPermission(Permission.ADMIN.permission)) return true;

        boolean has = sender.hasPermission(permission);
        //권한이 없고 피드백이 있으면
        if (!has && sendFeedback) {
            Tool.Error.PERM.send(sender);
        }
        return has;
    }
}
