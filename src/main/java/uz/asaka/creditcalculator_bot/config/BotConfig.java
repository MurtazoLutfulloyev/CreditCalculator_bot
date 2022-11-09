package uz.asaka.creditcalculator_bot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.asaka.creditcalculator_bot.service.MainBotService;

import javax.annotation.PostConstruct;

@Component
public class BotConfig{
    @Autowired
    private MainBotService botService;

    @PostConstruct
    protected void init(){

        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(botService);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
