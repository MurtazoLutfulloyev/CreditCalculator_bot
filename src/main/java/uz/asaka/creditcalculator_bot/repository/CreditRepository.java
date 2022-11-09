package uz.asaka.creditcalculator_bot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.asaka.creditcalculator_bot.model.Credit;
import uz.asaka.creditcalculator_bot.model.User;

@Repository
public interface CreditRepository extends CrudRepository<Credit, Integer> {

    Credit findByUser(User user);

}
