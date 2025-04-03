package uz.mavsumtravel.mavsumtravel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.mavsumtravel.mavsumtravel.model.TelegramUser;
import uz.mavsumtravel.mavsumtravel.model.enums.UserState;
import uz.mavsumtravel.mavsumtravel.repository.TelegramUserRepository;
import uz.mavsumtravel.mavsumtravel.service.TelegramUserService;

@Service
@RequiredArgsConstructor
public class TelegramUserServiceImpl implements TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;

    @Override
    public void save(TelegramUser telegramUser) {
        if (telegramUser.getLang() == null)
            telegramUser.setLang("uz");
        telegramUserRepository.save(telegramUser);
    }

    @Override
    public TelegramUser getByChatId(Long chatId) {
        return telegramUserRepository.findByChatId(chatId).isPresent() ?
                telegramUserRepository.findByChatId(chatId).get() : null;
    }

    @Override
    public String getLang(Long chatId) {
        if (telegramUserRepository.findByChatId(chatId).isEmpty())
            setLang(chatId,"uz");
        return telegramUserRepository.findByChatId(chatId).get().getLang();
    }

    @Override
    public void setLang(Long chatId, String lang) {
        TelegramUser user = telegramUserRepository.findByChatId(chatId).orElseGet(()->
                TelegramUser.builder()
                        .chatId(chatId)
                        .lang(lang)
                        .build()
        );
        user.setLang(lang);
        telegramUserRepository.save(user);
    }

    @Override
    public UserState getState(Long chatId) {
        if (telegramUserRepository.findByChatId(chatId).isEmpty())
            setState(chatId,UserState.DEFAULT);
        return telegramUserRepository.findByChatId(chatId).get().getState();
    }

    @Override
    public void setState(Long chatId, UserState userState) {
        TelegramUser user = telegramUserRepository.findByChatId(chatId).orElseGet(()->
                TelegramUser.builder()
                        .chatId(chatId)
                        .lang("uz")
                        .build()
        );
        user.setState(userState);
        telegramUserRepository.save(user);
    }
}
