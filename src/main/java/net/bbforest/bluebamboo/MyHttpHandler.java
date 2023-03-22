package net.bbforest.bluebamboo;
import org.bukkit.Bukkit;
import spark.Request;
import spark.Response;
import static spark.Spark.*;

public class MyHttpHandler {
    public static void init() {
        get("/whitelist", (req, res) -> {
            String name = req.queryParams("name");
            Bukkit.getScheduler().runTaskLater(EKE.getPlugin(), () ->
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + name), 0L);
            return "환영합니다, " + name + "!";
        });
    }
}
