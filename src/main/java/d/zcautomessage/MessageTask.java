package d.zcautomessage;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MessageTask extends BukkitRunnable {

    private int currentCategoryIndex = 0;
    private final boolean usePlaceholders;

    public MessageTask() {
        Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        this.usePlaceholders = (papi != null && papi.isEnabled());
    }

    @Override
    public void run() {
        Set<String> categories = ZCAutoMessage.get().getMessageCategories();
        List<String> categoryList = new ArrayList<>(categories);

        if (categoryList.isEmpty()) return;

        String selectedCategory;

        if (ZCAutoMessage.get().isRandomMessages()) {
            Random rand = new Random();
            selectedCategory = categoryList.get(rand.nextInt(categoryList.size()));
        } else {
            selectedCategory = categoryList.get(currentCategoryIndex);
            currentCategoryIndex = (currentCategoryIndex + 1) % categoryList.size();
        }

        List<String> messages = ZCAutoMessage.get().getMessagesForCategory(selectedCategory);
        if (messages.isEmpty()) return;

        String prefix = ZCAutoMessage.get().getPrefix();
        Player samplePlayer = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);

        StringBuilder combinedMessage = new StringBuilder();
        for (String line : messages) {
            String fullLine = (!prefix.isEmpty() ? prefix : "") + line;
            fullLine = ColorUtil.color(fullLine);

            if (usePlaceholders && samplePlayer != null) {
                fullLine = PlaceholderAPI.setPlaceholders(samplePlayer, fullLine);
            }

            combinedMessage.append(fullLine).append("\n");
        }

        Bukkit.broadcastMessage(combinedMessage.toString().trim());
    }
}