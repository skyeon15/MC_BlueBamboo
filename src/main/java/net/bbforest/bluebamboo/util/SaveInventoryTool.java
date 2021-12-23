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
        //중복 확인
        boolean same = false;

        //시스템 시간을 문자열로 변환
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
                    Tool.Log.WARN.send("폴더 생성 실패: " + folder.getName());
                }
            }

            //마지막 yml 파일 확인
            File[] files = folder.listFiles();
            //현재 시간의 새로운 yml 파일 생성
            File file = new File(EKE.getPlugin().getDataFolder() + "/InventoryData/"
                    + player.getUniqueId() + "/" + date + ".yml");

            //파일이 없으면 생성 -> 실패시 오류
            if(!file.exists()){
                try {
                    if(!file.createNewFile()){
                        Tool.Log.WARN.send("파일 생성 실패: " + file.getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //생성한 yml 불러오기
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            try {
                yamlConfiguration.load(file);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }

            //인벤토리 크기만큼 반복
            for(int i = 0;i < inventory.getSize();i++){
                //아이템 저장
                ItemStack itemStack = inventory.getItem(i);
                //비어있는 슬롯에는 공기 저장
                if(itemStack == null){
                    itemStack = new ItemStack(Material.AIR);
                }
                //yml에 아이템 추가
                yamlConfiguration.set(i + "", itemStack);
            }

            try {
                //yml 저장
                yamlConfiguration.save(file);
                if(files != null && files.length > 0){
                    File latestFile = files[files.length - 1];
                    if(latestFile.length() == file.length()){
                        same = true;
                        latestFile.delete();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(same) Tool.sendMessage(Bukkit.getConsoleSender(), "중복을 제외한 인벤토리 저장 완료! " + date);
        else Tool.sendMessage(Bukkit.getConsoleSender(), "인벤토리 저장 완료! " + date);
        updateList();
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
        },interval * 20 * 60L, interval * 20 * 60L);
    }
}
