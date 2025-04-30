package d.zcautomessage;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class ZCAutoMessage extends JavaPlugin {

    private static ZCAutoMessage pl;
    private Set<String> messageCategories;
    private int delay;
    private String prefix;
    private boolean randomMessages;
    BukkitTask task ;

    @Override
    public void onEnable() {
        pl = this;
        saveDefaultConfig();
        loadConfig();
        getLogger().info("[ZCAutoMessage] Плагин успешно запущен!");
        task =  new MessageTask().runTaskTimer(this, 0, delay * 20L);
    }

    public static ZCAutoMessage get() {
        return pl;
    }

    public String getPrefix() {
        return prefix;
    }

    public Set<String> getMessageCategories() {
        return messageCategories;
    }

    public List<String> getMessagesForCategory(String category) {
        return getConfig().getStringList("messages." + category);
    }

    public boolean isRandomMessages() {
        return randomMessages;
    }

    public void loadConfig() {
        delay = getConfig().getInt("delay", 120);
        prefix = getConfig().getString("prefix", "");
        randomMessages = getConfig().getBoolean("random", false);
        messageCategories = getConfig().getConfigurationSection("messages").getKeys(false);
        if (task != null){
            task.cancel();
            task =  new MessageTask().runTaskTimer(this, 0, delay * 20L);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("zcautomessage") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("zcautomessage.reload")) {
                reloadConfig();
                loadConfig();
                sendMessage(sender, "reload-success");
                getLogger().info("[ZCAutoMessage] Конфигурация плагина перезагружена.");
                return true;
            } else {
                sendMessage(sender, "no-permission");
            }
        }
        return false;
    }

    public void sendMessage(CommandSender sender, String messageKey) {
        String message = getConfig().getString("messages.errors." + messageKey, "Сообщение не найдено!");
        message = ColorUtil.color(message);
        sender.sendMessage(message);
    }
}
