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
        //권한 없으면
        if(!sender.hasPermission("eke.inventory")){
            Tool.Error.PERM.send(sender);
            return true;
        }
        if(args.length == 0){
            return false;
        }
        switch (args[0]){
            case "save" -> {
                //권한 없으면
                if(!sender.hasPermission("eke.inventory.save")){
                    Tool.Error.PERM.send(sender);
                    return true;
                }
                Tool.sendMessage(sender, "인벤토리를 저장합니다.");
                SaveInventoryTool.saveInventoryData();
                //SaveInventoryTool.updateList();
            }
            case "view" -> {
                //권한 없으면
                if(!sender.hasPermission("eke.inventory.view")){
                    Tool.Error.PERM.send(sender);
                    return true;
                }
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
                    + "/" + timeStamps.get(timeStamps.size() - 1) + ".yml"));
                }else if(args.length == 2){
                    OfflinePlayer offlinePlayer = Tool.getOfflinePlayer(args[1]);
                    if(offlinePlayer == null){
                        Tool.sendMessage(sender, "알 수 없는 플레이어에요.");
                    }
                    List<String> timeStamps = files.get(offlinePlayer.getUniqueId().toString());
                    if(timeStamps == null || timeStamps.isEmpty()){  //UUID 폴더가 없으면
                        Tool.sendMessage(sender, "저장된 데이터가 없습니다!");
                        return true;
                    }
                    viewInventory(player, new File(EKE.getPlugin().getDataFolder() + "/InventoryData/" + offlinePlayer.getUniqueId()
                            + "/" + timeStamps.get(timeStamps.size() - 1) + ".yml"));
                }else if(args.length == 3){
                    OfflinePlayer offlinePlayer = Tool.getOfflinePlayer(args[1]);
                    if(offlinePlayer == null){
                        Tool.sendMessage(sender, "알 수 없는 플레이어에요.");
                    }
                    List<String> timeStamps = files.get(offlinePlayer.getUniqueId().toString());
                    if(timeStamps == null || timeStamps.isEmpty()){  //UUID 폴더가 없으면
                        Tool.sendMessage(sender, "저장된 데이터가 없습니다!");
                        return true;
                    }
                    viewInventory(player, new File(EKE.getPlugin().getDataFolder() + "/InventoryData/" + offlinePlayer.getUniqueId()
                            + "/" + args[2] + ".yml"));
                }
            }
       }
        return true;
    }

    private void viewInventory(Player player, File file){
        if(!file.exists()){
            player.sendMessage("파일이 없어요.");
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
        //권한 없으면
        if(!sender.hasPermission("eke.inventory")){
            return Collections.emptyList();
        }

        if(args.length == 1){
            return Arrays.asList("save", "view");
        }
        if(args.length == 2){
            OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
            List<String> list = new ArrayList<>(files.keySet());
            for(OfflinePlayer offlinePlayer : offlinePlayers){
                String name = offlinePlayer.getName();
                if(name != null){
                    list.add(name);
                }
            }
            return list;
        }
        if(args.length == 3){
            OfflinePlayer offlinePlayer = Tool.getOfflinePlayer(args[1]);
            if(offlinePlayer == null){
                return Collections.singletonList("그런 사람은 없어요.");
            }
            UUID uuid = offlinePlayer.getUniqueId();
            List<String> filelist = files.get(uuid.toString());
            if(filelist == null || filelist.isEmpty()){
                return Collections.singletonList("그 사람의 데이터가 없어요.");
            }
            return filelist;
        }
        return Collections.emptyList();
    }
}
