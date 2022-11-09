package uz.asaka.creditcalculator_bot.service;

import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.asaka.creditcalculator_bot.constants.UserMessages;
import uz.asaka.creditcalculator_bot.constants.UserMessagesImpl;
import uz.asaka.creditcalculator_bot.model.User;
import uz.asaka.creditcalculator_bot.service.impl.BotButtonServiceImpl;
import uz.asaka.creditcalculator_bot.serviceInterface.BotButtonService;
import uz.asaka.creditcalculator_bot.serviceInterface.BotService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainBotService extends TelegramLongPollingBot {
    @Autowired
    User user;
    @Autowired
    UserMessages userMessages;
    private final BotButtonService buttonService;
    private final BotService service;
    private final UserService userService;
    private final CreditService creditService;
    private final Map<Long, Integer> back = new HashMap<>();
    private final Map<Long, String> lang = new HashMap<>();
    private final Map<Long, String> round = new HashMap<>();
    private final HashMap<Long, HashMap<String, String>> globalMap = new HashMap<Long, HashMap<String, String>>();
    private final HashMap<String, String> map = new HashMap<String, String>();
    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;
    @Value("${telegram.bot.groupId}")
    private String groupId;

    @Override
    public String getBotUsername() {
        return this.username;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        user.setChatId(service.getUserChatId(update));
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = service.getUserChatId(update);
            if ("/start".equals(message.getText())) {
                userService.saveUser(update);
                round.put(chatId, "0");
                executeButtons(null, buttonService.chooseLanguage(), "uz");
                round.put(chatId, "1");
            }
            if (update.hasMessage() && message.hasText()) {
                if (message.getText().equals("Main menu")) {
                    message.setText("/start");
                    onUpdateReceived(update);
                }
                if (message.getText().equals("Ortga")) {
                    switch (round.get(chatId)) {
                        case "2":
                            round.put(chatId, "1");
                            onUpdateReceived(update);
                            break;
                    }
                }
                switch (round.get(chatId)) {
                    case "3":
                        if (userService.isNumeric(message.getText())) {
                            map.put("totalAmount", message.getText());
                            globalMap.put(chatId, map);
                            executeButtons(buttonService.back(lang.get(chatId)), null, lang.get(chatId));
                            round.put(chatId, "4");
                            back.put(chatId, 1);
                        } else {
                            round.put(chatId, "2.1");
                            CallbackQuery callbackQuery = new CallbackQuery();
                            callbackQuery.setData("Kalkulyator");
                            update.setCallbackQuery(callbackQuery);
                            update.getMessage().setText(null);
                            onUpdateReceived(update);
                        }
                        break;
                    case "4":
                        if (userService.isNumeric(message.getText())) {
                            map.put("percentage", message.getText());
                            globalMap.put(chatId, map);
                            executeButtons(buttonService.back(lang.get(chatId)), null, lang.get(chatId));
                            round.put(chatId, "5");
                            back.put(chatId, 1);
                        } else {
                            round.put(chatId, "3.1");
                            executeButtons(buttonService.back(lang.get(chatId)), null, lang.get(chatId));
                            update.getMessage().setText(null);
                            round.put(chatId, "4");
                            onUpdateReceived(update);
                        }
                        break;
                    case "5":
                        if (userService.isNumeric(message.getText())) {
                            map.put("duration", message.getText());
                            globalMap.put(chatId, map);
                            HashMap<String, String> map = globalMap.get(chatId);
                            executeDoc(update, map, lang.get(chatId));
                            executeDocGroup(update, lang.get(chatId));
                            round.put(chatId, "6");
                            back.put(chatId, 1);
                        } else {
                            round.put(chatId, "4.1");
                            executeButtons(buttonService.back(lang.get(chatId)), null, lang.get(chatId));
                            update.getMessage().setText(null);
                            round.put(chatId, "5");
                            onUpdateReceived(update);
                        }
                        break;
                    case "6":

                        break;

                    default:
                        return;
                }

            }


        }
        /*************************************************InlineKeyboards**********************************************/
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            if (round.get(user.getChatId()).equals("1")) {
                switch (callbackQuery.getData()) {
                    case "\uD83C\uDDFA\uD83C\uDDFF O'zbekcha":
//                        shareContact(null);
                        lang.put(user.getChatId(), "uz");
                        executeDeleteMessage(update);
                        executeButtons(null, buttonService.chooseType(lang.get(user.getChatId())), lang.get(user.getChatId()));
                        round.put(user.getChatId(), "2");
                        globalMap.put(user.getChatId(), map);
                        break;
                    case "\uD83C\uDDF7\uD83C\uDDFA Русский":
//                        shareContact(null);
                        lang.put(user.getChatId(), "ru");
                        executeDeleteMessage(update);
                        executeButtons(null, buttonService.chooseType(lang.get(user.getChatId())), lang.get(user.getChatId()));
                        round.put(user.getChatId(), "2");
                        globalMap.put(user.getChatId(), map);
                        break;
                    default:
                        return;

                }

            } else if (round.get(user.getChatId()).equals("2") || round.get(user.getChatId()).equals("2.1")) {
                switch (callbackQuery.getData()) {
                    case "Kalkulyator":
                        lang.put(user.getChatId(), "uz");
                        executeButtons(buttonService.back(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.put(user.getChatId(), "3");
                        globalMap.put(user.getChatId(), map);
                        break;
                    case "Калкулятор":
                        lang.put(user.getChatId(), "ru");
                        executeButtons(buttonService.back(lang.get(user.getChatId())), null, lang.get(user.getChatId()));
                        round.put(user.getChatId(), "3");
                        globalMap.put(user.getChatId(), map);
                        break;

                }
            }
        }


    }

    private void executeDeleteMessage(Update update) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        deleteMessage.setChatId(String.valueOf(user.getChatId()));
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeDoc(Update update, HashMap<String, String> map, String lang) throws DocumentException, IOException {
        String totalAmount = map.get("totalAmount");
        String percentage = map.get("percentage");
        String duration = map.get("duration");
        User getUser = userService.getUser(user.getChatId());
        String firstName = getUser.getFirstName();
        String lastName = getUser.getLastName();
        String userName = getUser.getUserName();
        SendDocument document = new SendDocument();
        InputFile file = new InputFile(new File(creditService.creditCalculator(Long.valueOf(totalAmount), Integer.valueOf(duration), Double.parseDouble(percentage), getUser, lang)));
        document.setChatId(String.valueOf(user.getChatId()));
        document.setDocument(file);
        if (lang.equals("uz")) {
            document.setCaption("Ism: " + firstName + "\n" +
                    "Familiya: " + lastName + "\n" +
                    "Username: " + "@" + userName + "\n" +
                    "Umumiy miqdor: " + totalAmount + "\n" +
                    "Foiz: " + percentage + "\n" +
                    "Muddat: " + duration + "\n");

            try {
                execute(document);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            document.setCaption("Имя: " + firstName + "\n" +
                    "Фамилия: " + lastName + "\n" +
                    "Имя пользователя: " + "@" + userName + "\n" +
                    "Итого: " + totalAmount + "\n" +
                    "Процент: " + percentage + "\n" +
                    "Продолжительность: " + duration + "\n");

            try {
                execute(document);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void executeDocGroup(Update update, String lang) throws DocumentException, IOException {

        String totalAmount = map.get("totalAmount");
        String percentage = map.get("percentage");
        String duration = map.get("duration");
        User getUser = userService.getUser(user.getChatId());
        String firstName = getUser.getFirstName();
        String lastName = getUser.getLastName();
        String userName = getUser.getUserName();
        SendDocument document = new SendDocument();
        InputFile file = new InputFile(new File(creditService.creditCalculator(Long.valueOf(totalAmount), Integer.valueOf(duration), Double.parseDouble(percentage), getUser, lang)));
        document.setChatId(groupId);
        document.setDocument(file);
        document.setCaption("first_name: " + firstName + "\n" +
                "last_name: " + lastName + "\n" +
                "username: " + userName + "\n" +
                "total_amount: " + totalAmount + "\n" +
                "percentage: " + percentage + "\n" +
                "duration: " + duration + "\n");
        try {
            execute(document);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }


    private void executeButtons(ReplyKeyboardMarkup replyKeyboardMarkup, InlineKeyboardMarkup inlineKeyboardMarkup, String lang) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getChatId()));
        sendMessage.setText(userMessages.response(round.get(user.getChatId()).toString(), lang));
        sendMessage.enableHtml(true);
        if (replyKeyboardMarkup != null)
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        else if (inlineKeyboardMarkup != null)
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void shareContact(String lang) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getChatId()));
        sendMessage.setText(userMessages.response(round.get(user.getChatId()).toString(), lang));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText(userMessages.response(String.valueOf(round.get(user.getChatId())), lang));
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
