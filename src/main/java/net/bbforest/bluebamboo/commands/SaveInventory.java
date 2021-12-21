package net.bbforest.bluebamboo.commands;

import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.util.SaveInventoryTool;
import net.bbforest.bluebamboo.util.Tool;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SaveInventory implements CommandExecutor, TabCompleter {

    public static HashMap<String, List<String>> files = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 0){
            return false;
        }
        switch (args[0]){
            case "save" -> {
                Tool.sendMessage(sender, "인벤토리를 저장합니다.");
                SaveInventoryTool.saveInventoryData();
            }
            case "view" -> {
                if(!(sender instanceof Player player)) {    //콘솔인지 확인
                    Tool.sendMessage(sender, "인벤 확인은 플레이어만 사용 가능해요.");
                    return true;
                }
                if(args.length == 1){
                    String uuid = player.getUniqueId().toString();
                    List<String> timeStamps = files.get(uuid);
                    if(timeStamps == null || timeStamps.isEmpty()){  //UUID 폴더가 없으면
                        Tool.sendMessage(sender, "저장된 데이터가 없습니다!");
                        return true;
                    }
                    viewInventory(player, new File(EKE.getPlugin().getDataFolder() + "/InventoryData/" + player.getUniqueId()
                    + "/" + timeStamps.get(timeStamps.size() - 1)));
                }else if(args.length == 2){
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(args[1]));
                    List<String> timeStamps = files.get(offlinePlayer.getUniqueId().toString());
                    if(timeStamps == null || timeStamps.isEmpty()){  //UUID 폴더가 없으면
                        Tool.sendMessage(sender, "저장된 데이터가 없습니다!");
                        return true;
                    }
                    viewInventory(player, new File(EKE.getPlugin().getDataFolder() + "/InventoryData/" + offlinePlayer.getUniqueId()
                            + "/" + timeStamps.get(timeStamps.size() - 1)));
                }else if(args.length == 3){
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(args[1]));
                    List<String> timeStamps = files.get(offlinePlayer.getUniqueId().toString());
                    if(timeStamps == null || timeStamps.isEmpty()){  //UUID 폴더가 없으면
                        Tool.sendMessage(sender, "저장된 데이터가 없습니다!");
                        return true;
                    }
                    viewInventory(player, new File(EKE.getPlugin().getDataFolder() + "/InventoryData/" + offlinePlayer.getUniqueId()
                            + "/" + args[2]));
                }
            }
       }
        return false;
    }

    private void viewInventory(Player player, File file){
        if(!file.exists() || file.isFile()){
            player.sendMessage("없다");
            return;
        }
        String name = file.getName();
        if(!name.endsWith(".yml")){
            player.sendMessage("YML이 아닌데요?");
            return;
        }
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try {
            yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        String uuid = file.getParentFile().getName();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
        //오프라인 사용자 닉네임 가져옴
        String PlayerName = offlinePlayer.getName() != null ? offlinePlayer.getName() : uuid;

        Inventory inventory = Bukkit.createInventory(null,45, Component.text(PlayerName + "/" + file.getName()));
        for(int i = 0; i < 41; i++){
            ItemStack itemStack = yamlConfiguration.getItemStack(i + "");
            inventory.setItem(i, itemStack);
        }
        player.openInventory(inventory);
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
