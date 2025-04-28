package d.zcautomessage;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MessageTask extends BukkitRunnable {

    private int currentCategoryIndex = 0;

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
            currentCategoryIndex = (currentCategoryIndex + 1);
            if (currentCategoryIndex >= categoryList.size()) currentCategoryIndex = 0;
        }

        List<String> messages = ZCAutoMessage.get().getMessagesForCategory(selectedCategory);

        if (messages.isEmpty()) return;

        String prefix = ZCAutoMessage.get().getPrefix();
        StringBuilder combinedMessage = new StringBuilder();



        for (String line : messages) {
            if (!prefix.isEmpty()) {
                combinedMessage.append(ColorUtil.color(prefix));
            }
            combinedMessage.append(ColorUtil.color(line)).append("\n");
        }

        Bukkit.broadcastMessage(combinedMessage.toString().trim());
    }
}
