package net.bbforest.bluebamboo.util;

import net.bbforest.bluebamboo.EKE;
import net.bbforest.bluebamboo.commands.SaveInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SaveInventoryTool {
    public static void updateList(){
        SaveInventory.files.clear();

        //인벤토리 데이터 폴더 지정
        File mainFolder = new File(EKE.getPlugin().getDataFolder() + "/InventoryData");

        //폴더가 없거나 폴더가 아니면 리턴
        if(!mainFolder.exists() || !mainFolder.isDirectory()){
            return;
        }

        File[] files = mainFolder.listFiles();
        //파일들이 비어있으면 리턴
        if(files == null || files.length == 0){
            return;
        }

        for(File file : files){
            String uuid = file.getName();
            //디렉토리 목록 불러오기
            if(file.isDirectory()){
                File[] datas = file.listFiles();
                //저장된 게 없으면 넘기기
                if(datas == null || datas.length == 0){
                    continue;
                }
                List<String> timeStamps = new ArrayList<>();
                for(File data : datas){
                    String name = data.getName();
                    timeStamps.add(name.substring(0, name.length() - 4));
                }
                SaveInventory.files.put(uuid, timeStamps);
            }
        }
    }
    public static void saveInventoryData(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String date = dateFormat.format(calendar.getTime());

        //모든 온라인 사용자의 인벤토리 확보
        for(Player player : Bukkit.getOnlinePlayers()){
            Inventory inventory = player.getInventory();

            //사용자 폴더 생성
            File folder = new File(EKE.getPlugin().getDataFolder() + "/InventoryData/" + player.getUniqueId());
            //폴더 없으면 생성 -> 실패시 오류
            if(!folder.exists() || !folder.isDirectory()){
                if(!folder.mkdirs()){
                    System.err.println("폴더 생성 실패: " + folder.getName());
                }
            }

            //현재 시간의 새로운 파일 생성
            File file = new File(EKE.getPlugin().getDataFolder() + "/InventoryData/"
                    + player.getUniqueId() + "/" + date + ".yml");

            //파일이 없으면 생성 -> 실패시 오류
            if(!file.exists()){
                try {
                    if(!file.createNewFile()){
                        System.err.println("파일 생성 실패: " + file.getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            try {
                yamlConfiguration.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            for(int i = 0;i < inventory.getSize();i++){
                ItemStack itemStack = inventory.getItem(i);
                if(itemStack==null){
                    itemStack = new ItemStack(Material.AIR);
                }
                yamlConfiguration.set(i + "", itemStack);
            }

            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Tool.sendMessage(Bukkit.getConsoleSender(), "인벤토리 저장 완료! " + date);
    }

    public static void autoSaveInventoryData(){
        //config 가져오기
        FileConfiguration config = EKE.getPlugin().getConfig();
        int interval = config.getInt("inventory-snapshot.time");
        //숫자가 1 미만이면 1로 올림
        if(interval <= 0) interval = 1;
        Bukkit.getScheduler().runTaskTimer(EKE.getPlugin(), () -> {
            if(!config.getBoolean("inventory-snapshot.enabled")){
                return;
            }
            Tool.sendMessage(Bukkit.getConsoleSender(), "자동 저장 중...");
            saveInventoryData();
        },interval * 20 * 60, interval * 20 * 60);
    }
}
