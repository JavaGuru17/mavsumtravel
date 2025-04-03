package uz.mavsumtravel.mavsumtravel.service.telegramservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.mavsumtravel.mavsumtravel.model.TelegramUser;
import uz.mavsumtravel.mavsumtravel.service.LangService;
import uz.mavsumtravel.mavsumtravel.service.TelegramUserService;
import uz.mavsumtravel.mavsumtravel.service.UserService;

@Service
@RequiredArgsConstructor
public class SendMessageService {
    private final LangService langService;
    private final UserService userService;
    private final TelegramUserService telegramUserService;

    public SendMessage firstStart(TelegramUser telegramUser) {
        Long chatId = telegramUser.getChatId();
        return SendMessage.builder()
                .chatId(chatId)
                .text("Assalomu aleykum! Keling, avvaliga xizmat ko'rsatish tilini tanlab olaylik!")
                .replyMarkup(KeyboardUtils.inlineMarkup(
                        KeyboardUtils.inlineButton(langService.getMessage(LangFields.BUTTON_RUSSIAN_LANGUAGE, chatId), Callback.LANG_RU.getCallback()),
                        KeyboardUtils.inlineButton(langService.getMessage(LangFields.BUTTON_UZBEK_LANGUAGE, chatId), Callback.LANG_UZ.getCallback()),
                        KeyboardUtils.inlineButton(langService.getMessage(LangFields.BUTTON_ENGLISH_LANGUAGE, chatId), Callback.LANG_EN.getCallback())
                ))
                .build();
    }
}
