package uz.asaka.creditcalculator_bot.constants;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;


@NoArgsConstructor
@Data
@Service
public class UserMessagesImpl implements UserMessages {

    @Override
    public String response(String round, String lang) {
        if (lang.equals("uz")) {
            switch (round) {
                case "0":
                    return "Muloqot tilini tanlang\n" +
                            "Выберите язык";
                case "1":
                    return "Xizmat turini tanlang:";
                case "2":
                    return "Olmoqchi bo'lgan kreditingiz miqdorini kiriting. Masalan: 10000000";
                case "2.1":
                    return "Kredit miqdori xato kiritildi, Qaytadan kiriting:";
                case "3":
                    return "Kredit foizini kiriting. Masalan: 23.4";
                case "3.1":
                    return "Foiz miqdori xato kiritildi, Qaytadan kiriting:";
                case "4":
                    return "Kredit muddatini kiriting(oylarda). Masalan: 12";
                case "4.1":
                    return "Kredit muddati xato kiritildi, Qaytadan kiriting:";
                case "5":
                    return "Fileda ko`rinishidagi jadval sizga taqdim etildi.";
                case "10":
                    return "Kerakli bo'limni tanlang:";
                case "11":
                    return "Savollaringizni yozing:";
                case "12":
                    return "Xabaringiz operatorlaga yetkazildi, sizga xizmat ko'rsatganimizdan mamnunmiz";
                case "13":
                    return "Iltimos kontaktingizni yuboring";
                case "14":
                    return "Abonent qo'ng'iroq qilishingizni so'ragan: ";
            }
        } else {
            switch (round) {
                case "0":
                    return "Muloqot tilini tanlang\n" +
                            "Выберите язык";
                case "1":
                    return "Выберите тип услуги:";

                case "2":
                    return "Введите сумму вашего кредита, который вы хотите получить:";
                case "2.1":
                    return "Сумма кредита была введена неправильно, пожалуйста, введите еще раз:";
                case "3":
                    return "Введите процент кредита. Например: 23,4";
                case "3.1":
                    return "Сумма процентов была введена неправильно, пожалуйста, введите еще раз:";
                case "4":
                    return "Введите срок кредита (в месяцах). Например: 12";
                case "4.1":
                    return "Срок кредита был введен неправильно, пожалуйста, введите еще раз:";
                case "5":
                    return "Вашему вниманию представлена таблица в формате файла.";
                case "10":
                    return "Выберите нужный раздел:";
                case "11":
                    return "Пишите свои вопросы:";
                case "12":
                    return "Ваше сообщение передано оператору, будем рады Вам помочь";
                case "13":
                    return "Пожалуйста, отправьте ваш контакт";
                case "14":
                    return "Абонент попросил позвонить:";
            }
        }


        return null;
    }

}
