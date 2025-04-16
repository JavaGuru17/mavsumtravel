package uz.mavsumtravel.service.telegramservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import uz.mavsumtravel.model.TelegramUser;
import uz.mavsumtravel.model.User;
import uz.mavsumtravel.repository.UserRepository;
import uz.mavsumtravel.service.LangService;
import uz.mavsumtravel.service.TelegramUserService;
import uz.mavsumtravel.service.UserService;
import uz.mavsumtravel.util.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import static uz.mavsumtravel.util.LangFields.*;

@Service
@RequiredArgsConstructor
public class SendMessageService {
    private final LangService langService;
    private final UserService userService;
    private final TelegramUserService telegramUserService;

    public SendMessage firstStart(TelegramUser telegramUser) {
        Long chatId = telegramUser.getChatId();
        List<List<KeyboardButton>> buttonRows = List.of(
                List.of(
                        KeyboardUtils.button(langService.getMessage(BUTTON_RUSSIAN_LANGUAGE, chatId), false, false),
                        KeyboardUtils.button(langService.getMessage(BUTTON_UZBEK_LANGUAGE, chatId), false, false)
                )
        );
        return SendMessage.builder()
                .chatId(chatId)
                .text("Assalomu aleykum! Keling, avvaliga xizmat ko'rsatish tilini tanlab olaylik!")
                .replyMarkup(KeyboardUtils.markup(buttonRows))
                .build();
    }

    public SendMessage changeLang(Long chatId) {
        List<List<KeyboardButton>> buttonRows = List.of(
                List.of(
                        KeyboardUtils.button(langService.getMessage(BUTTON_RUSSIAN_LANGUAGE, chatId),
                                false, false),
                        KeyboardUtils.button(langService.getMessage(BUTTON_UZBEK_LANGUAGE, chatId),
                                false, false)
                ),
                List.of(
                        KeyboardUtils.button(langService.getMessage(BUTTON_BACK, chatId),
                                false, false)
                )
        );
        return SendMessage.builder()
                .chatId(chatId)
                .text(langService.getMessage(CHOOSE_LANGUAGE, chatId))
                .replyMarkup(KeyboardUtils.markup(buttonRows))
                .build();
    }

    public SendMessage welcomeUser(TelegramUser telegramUser) {
        Long chatId = telegramUser.getChatId();
        List<List<KeyboardButton>> buttonRows = new ArrayList<>(List.of(
                List.of(
                        KeyboardUtils.button(langService.getMessage(BUTTON_HOT_TOURS, chatId), false, false),
                        KeyboardUtils.button(langService.getMessage(BUTTON_SPECIAL_OFFERS, chatId), false, false)
                ),
                List.of(
                        KeyboardUtils.button(langService.getMessage(BUTTON_CHANGE_LANGUAGE, chatId), false, false)
                )
        ));

        if (userService.isAdmin(chatId))
            buttonRows.add(List.of(KeyboardUtils.button("Admin", false, false)));

        return SendMessage.builder()
                .text(langService.getMessage(WELCOME_USER, chatId))
                .replyMarkup(KeyboardUtils.markup(buttonRows))
                .chatId(chatId)
                .build();
    }

    public SendMessage setLang(String data, TelegramUser telegramUser) {
        telegramUser.setLang(data);
        telegramUserService.save(telegramUser);

        Long chatId = telegramUser.getChatId();
        List<List<KeyboardButton>> buttonRows = new ArrayList<>(List.of(
                List.of(
                        KeyboardUtils.button(langService.getMessage(BUTTON_HOT_TOURS, chatId), false, false),
                        KeyboardUtils.button(langService.getMessage(BUTTON_SPECIAL_OFFERS, chatId), false, false)
                ),
                List.of(
                        KeyboardUtils.button(langService.getMessage(BUTTON_CHANGE_LANGUAGE, chatId), false, false)
                )
        ));

        if (userService.isAdmin(chatId))
            buttonRows.add(List.of(KeyboardUtils.button("Admin", false, false)));

        return SendMessage.builder()
                .chatId(chatId)
                .text(langService.getMessage(LANGUAGE_CHANGED, chatId))
                .replyMarkup(KeyboardUtils.markup(buttonRows))
                .build();
    }

    public SendMessage unknownCommand(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(UNKNOWN_COMMAND)
                .build();
    }

    public SendMessage requestPhoneNumber(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(langService.getMessage(INPUT_PHONE_NUMBER, chatId))
                .replyMarkup(KeyboardUtils.markup(KeyboardButton.builder()
                        .text(langService.getMessage(SHARE_CONTACT, chatId))
                        .requestContact(true)
                        .build()))
                .build();
    }

    public SendMessage invalidPhoneNumber(Long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(INVALID_PHONE_NUMBER)
                .build();
    }

    public DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }
}
