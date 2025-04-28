package uz.mavsumtravel.service.telegramservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import uz.mavsumtravel.dto.TourDto;
import uz.mavsumtravel.model.TelegramUser;
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
                .text(langService.getMessage(UNKNOWN_COMMAND, chatId))
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
                .text(langService.getMessage(INVALID_PHONE_NUMBER, chatId))
                .replyMarkup(KeyboardUtils.markup(KeyboardButton.builder()
                        .text(langService.getMessage(SHARE_CONTACT, chatId))
                        .requestContact(true)
                        .build()))
                .build();
    }

    public SendMessage hotToursNotAvailable(Long chatId) {
        List<List<KeyboardButton>> buttonRows = new ArrayList<>(List.of(
                List.of(
                        KeyboardUtils.button(langService.getMessage(BUTTON_HOT_TOURS, chatId), false, false),
                        KeyboardUtils.button(langService.getMessage(BUTTON_SPECIAL_OFFERS, chatId), false, false)
                ),
                List.of(
                        KeyboardUtils.button(langService.getMessage(BUTTON_CHANGE_LANGUAGE, chatId), false, false)
                )
        ));

        return SendMessage.builder()
                .chatId(chatId)
                .text(langService.getMessage(HOT_TOURS_NOT_AVAILABLE, chatId))
                .replyMarkup(KeyboardUtils.markup(buttonRows))
                .build();
    }

    public SendPhoto sendTourPage(TelegramUser user, TourDto tour, int currentIndex, int totalTours) {
        Long chatId = user.getChatId();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId.toString());
        sendPhoto.setPhoto(new InputFile(tour.getImgPath()));
        sendPhoto.setCaption(tour.getDescription());

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        if (currentIndex > 0) {
            buttons.add(KeyboardUtils.inlineButton("⬅\uFE0F", "prev_" + currentIndex));
        }
        if (currentIndex < totalTours - 1) {
            buttons.add(KeyboardUtils.inlineButton("➡\uFE0F", "next_" + currentIndex));
        }
        buttons.add(KeyboardUtils.inlineButton(langService.getMessage(BUTTON_BACK, chatId), "back"));

        sendPhoto.setReplyMarkup(KeyboardUtils.inlineMarkup(buttons));

        System.out.println("Creating tour page for chatId: " + chatId + ", tour: " + tour.getId() + ", buttons: " + buttons);
        return sendPhoto;
    }

    public EditMessageMedia editTourPage(Long chatId, Integer messageId, TourDto tour, int currentIndex, int totalTours) {
        EditMessageMedia editMessageMedia = new EditMessageMedia();
        editMessageMedia.setChatId(chatId.toString());
        editMessageMedia.setMessageId(messageId);
        InputMediaPhoto media = new InputMediaPhoto();
        media.setMedia(tour.getImgPath());
        media.setCaption(tour.getDescription());
        editMessageMedia.setMedia(media);

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        if (currentIndex > 0) {
            buttons.add(KeyboardUtils.inlineButton("⬅\uFE0F", "prev_" + currentIndex));
        }
        if (currentIndex < totalTours - 1) {
            buttons.add(KeyboardUtils.inlineButton("➡\uFE0F", "next_" + currentIndex));
        }
        buttons.add(KeyboardUtils.inlineButton(langService.getMessage(BUTTON_BACK, chatId), "back"));

        editMessageMedia.setReplyMarkup(KeyboardUtils.inlineMarkup(buttons));

        System.out.println("Editing tour page for chatId: " + chatId + ", tour: " + tour.getId() + ", buttons: " + buttons);
        return editMessageMedia;
    }

    public DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }
}
