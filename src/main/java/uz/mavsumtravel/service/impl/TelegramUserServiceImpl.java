package uz.mavsumtravel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.mavsumtravel.model.TelegramUser;
import uz.mavsumtravel.model.enums.UserState;
import uz.mavsumtravel.repository.TelegramUserRepository;
import uz.mavsumtravel.service.TelegramUserService;

import java.util.Optional;

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
        return telegramUserRepository.findByChatId(chatId).orElse(null);
    }

    @Override
    public String getLang(Long chatId) {
        TelegramUser user = getOrCreateUser(chatId);
        return user.getLang();
    }

    @Override
    public void setLang(Long chatId, String lang) {
        TelegramUser user = getOrCreateUser(chatId);
        user.setLang(lang);
        save(user);
    }

    @Override
    public UserState getState(Long chatId) {
        TelegramUser user = getOrCreateUser(chatId);
        return user.getState();
    }

    @Override
    public void setState(Long chatId, UserState newState) {
        TelegramUser user = getOrCreateUser(chatId);
        if (user.getState() != null)
            user.getStateHistory().add(user.getState());

        user.setState(newState);
        save(user);
    }

    @Override
    public void goBack(Long chatId) {
        TelegramUser user = getByChatId(chatId);
        if (user != null && !user.getStateHistory().isEmpty()) {
            UserState previousState = user.getStateHistory().remove(user.getStateHistory().size() - 1);
            user.setState(previousState);
            save(user);
        } else if (user != null) {
            user.setState(UserState.DEFAULT);
            save(user);
        }
    }

    private TelegramUser getOrCreateUser(Long chatId) {
        Optional<TelegramUser> userOpt = telegramUserRepository.findByChatId(chatId);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        TelegramUser newUser = TelegramUser.builder()
                .chatId(chatId)
                .lang("uz")
                .state(UserState.DEFAULT)
                .build();
        save(newUser);
        return newUser;
    }
}
