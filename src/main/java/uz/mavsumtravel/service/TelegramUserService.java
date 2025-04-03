package uz.mavsumtravel.service;

import org.springframework.stereotype.Service;
import uz.mavsumtravel.model.TelegramUser;
import uz.mavsumtravel.model.enums.UserState;

@Service
public interface TelegramUserService {
    void save(TelegramUser telegramUser);

    TelegramUser getByChatId(Long chatId);

    String getLang(Long chatId);

    void setLang(Long chatId, String lang);

    UserState getState(Long chatId);

    void setState(Long chatId, UserState userState);

    void goBack(Long chatId);
}
