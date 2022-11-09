package uz.asaka.creditcalculator_bot.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.asaka.creditcalculator_bot.serviceInterface.BotService;

@Service
public class BotServiceImpl implements BotService {


    @Override
    public Long getUserChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        }
        else return update.getCallbackQuery().getMessage().getChatId();
    }

    @Override
    public String getUserResponse(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText())
            return update.getMessage().getText();
        }
        else return "Nothing not found!";
        return update.getCallbackQuery().getData();
    }

}
