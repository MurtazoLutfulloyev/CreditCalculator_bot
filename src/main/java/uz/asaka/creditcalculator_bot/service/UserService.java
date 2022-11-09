package uz.asaka.creditcalculator_bot.service;


import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.asaka.creditcalculator_bot.model.User;
import uz.asaka.creditcalculator_bot.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long chatId) {

        return userRepository.findByChatId(chatId);
    }

    public void saveUser(Update update) {
        User byChatId = userRepository.findByChatId(update.getMessage().getChatId());
        if (byChatId == null) {
            User user = new User();
            user.setChatId(update.getMessage().getChatId());
            user.setFirstName(update.getMessage().getFrom().getFirstName());
            user.setLastName(update.getMessage().getFrom().getLastName());
            user.setUserName(update.getMessage().getFrom().getUserName());
            userRepository.save(user);
        }
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
