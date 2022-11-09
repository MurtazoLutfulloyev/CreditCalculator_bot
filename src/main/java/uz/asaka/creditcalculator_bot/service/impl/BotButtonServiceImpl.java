package uz.asaka.creditcalculator_bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.asaka.creditcalculator_bot.constants.UserMessagesImpl;
import uz.asaka.creditcalculator_bot.model.User;
import uz.asaka.creditcalculator_bot.serviceInterface.BotButtonService;

import java.util.ArrayList;
import java.util.List;

@Service
public class BotButtonServiceImpl implements BotButtonService {

    @Autowired
    User user;

    @Autowired
    UserMessagesImpl userMessages;

    @Override
    public ReplyKeyboardMarkup createMarkupButtons(List<String> buttons) {
        ReplyKeyboardMarkup replyKeyboardMarkup = makeReplyMarkup();

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        int rowCount = 2;

        for (int i = 0; i < buttons.size(); i++) {
            keyboardButtons.add(buttons.get(i));
            rowCount--;

            if ((rowCount == 0 || i == buttons.size() - 1)) {
                rowList.add(keyboardButtons);
                keyboardButtons = new KeyboardRow();
                rowCount = 2;
            }
        }
        replyKeyboardMarkup.setKeyboard(rowList);
        return replyKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboardButton(List<String> buttons, int column) {
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        int rowCount = column;
        for (int i = 0; i < buttons.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttons.get(i));
            button.setCallbackData(buttons.get(i));
            buttonRow.add(button);
            rowCount--;
            if ((rowCount == 0 || i == buttons.size() - 1)) {
                rowList.add(buttonRow);
                buttonRow = new ArrayList<>();
                rowCount = column;
            }
        }
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    @Override
    public ReplyKeyboardMarkup makeReplyMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        return replyKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup makeInlineMarkup() {
        return null;
    }

    @Override
    public InlineKeyboardMarkup chooseLanguage() {
        return createInlineKeyboardButton(List.of("\uD83C\uDDFA\uD83C\uDDFF O'zbekcha",
                "\uD83C\uDDF7\uD83C\uDDFA Русский"), 2);
    }

    @Override
    public InlineKeyboardMarkup chooseType(String lang) {
    return  (lang.equals("uz"))? createInlineKeyboardButton(List.of("Kalkulyator", "Valyuta kursi", "Aloqa","Ortga"), 3) :
            createInlineKeyboardButton(List.of("Калкулятор", "Курс валюты", "Связь", "Назад"), 3);
    }

    @Override
    public ReplyKeyboardMarkup back(String lang) {
        return  (lang.equals("uz"))? createMarkupButtons(List.of("Ortga")) :
                createMarkupButtons(List.of( "Назад"));
    }

}

