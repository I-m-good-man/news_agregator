package Telegram;

import JSON.Config;
import JSON.ReadJSON;
import VariousUtils.VariousUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;

public class TelegramUtils {
    private TelegramBot telegramBot;
    private Integer lastTimeOfRequest = -1;
    private Config config;
    public TelegramUtils(Config config) {
        this.telegramBot = new TelegramBot(config.botToken);
        this.config = config;
    }
    public void sendMessage(Object chatId, String messageText) throws IOException, TelegramException {
        Integer currentTimestampSeconds = VariousUtils.getCurrentTimestampSeconds();
        Integer timeDelta = VariousUtils.calculateTimeDelta(this.config.requestLimitPerDay);

        while (true){
            if (this.lastTimeOfRequest == -1){
                break;
            }
            if ((currentTimestampSeconds - this.lastTimeOfRequest) * 1000 > timeDelta){
                break;
            }
            currentTimestampSeconds = VariousUtils.getCurrentTimestampSeconds();
        }
        this.lastTimeOfRequest = VariousUtils.getCurrentTimestampSeconds();
        this.telegramBot.execute(new SendMessage(chatId, messageText).parseMode(ParseMode.HTML));

    }

    public void searchChatId() {
        this.telegramBot.setUpdatesListener(updates ->{
            updates.forEach(System.out::println);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

//    public static void main(String[] args) throws TelegramException, IOException {
//        ReadJSON readJSON = new ReadJSON();
//        Config config = readJSON.getConfig();
//        TelegramUtils telegramUtils = new TelegramUtils(config);
//
//        telegramUtils.sendMessage(-1001842501205L, "hello");
//        telegramUtils.sendMessage(-1001842501205L, "hello");
//
//        for (int i=0; i<10; i++){
//        }
//
//        telegramUtils.telegramBot.setUpdatesListener(updates ->{
//            updates.forEach(System.out::println);
//            return UpdatesListener.CONFIRMED_UPDATES_ALL;
//        });
//    }

}
