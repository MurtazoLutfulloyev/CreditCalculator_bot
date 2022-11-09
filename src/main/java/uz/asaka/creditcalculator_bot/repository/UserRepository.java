package uz.asaka.creditcalculator_bot.repository;;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.asaka.creditcalculator_bot.model.User;


import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByChatId(Long chatId);


}


