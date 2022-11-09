package uz.asaka.creditcalculator_bot.serviceInterface;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public interface BotButtonService {

    ReplyKeyboardMarkup createMarkupButtons(List<String> buttons);

    ReplyKeyboardMarkup makeReplyMarkup();

    InlineKeyboardMarkup createInlineKeyboardButton(List<String> buttons, int column);

    InlineKeyboardMarkup makeInlineMarkup();

    InlineKeyboardMarkup chooseLanguage();

    InlineKeyboardMarkup chooseType(String lang);

    ReplyKeyboardMarkup back(String lang);

    InlineKeyboardMarkup chooseConnect(String lang);


    /************************************************InlineKeyboards***************************************************/

}