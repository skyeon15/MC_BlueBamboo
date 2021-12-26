package net.bbforest.bluebamboo.util;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public enum Permission {
    CMD_BLUEBAMBOO("admin"),
    CMD_RELOAD("reload"),
    CMD_BROADCAST("broadcast"),
    CMD_INVENTORY("inventory"),
    COLOR("color");

    private final String permission;

    Permission(String permission) {
        this.permission = "bluebamboo." + permission;
    }

    public boolean hasPerm(@NotNull CommandSender sender) {
        if (!sender.hasPermission(permission)) Tool.Error.PERM.send(sender);
        return sender.hasPermission(permission);
    }
}
