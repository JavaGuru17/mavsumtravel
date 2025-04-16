package uz.mavsumtravel.service.telegramservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mavsumtravel.model.TelegramUser;
import uz.mavsumtravel.model.enums.UserState;
import uz.mavsumtravel.service.LangService;
import uz.mavsumtravel.service.TelegramUserService;
import uz.mavsumtravel.service.UserService;
import uz.mavsumtravel.util.Regex;

import static uz.mavsumtravel.util.LangFields.*;

@Service
@RequiredArgsConstructor
public class TelegramService {
    private final TelegramUserService telegramUserService;
    private final SendMessageService sendMessageService;
    private final BotService botService;
    private final LangService langService;

    public void handleMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText() != null ? message.getText().trim() : null;
        TelegramUser user = telegramUserService.getByChatId(chatId);

        if (user == null) {
            user = TelegramUser.builder()
                    .chatId(chatId)
                    .lang("uz")
                    .state(UserState.START)
                    .build();
            telegramUserService.save(user);
            botService.send(sendMessageService.firstStart(user));
            return;
        }

        if (text != null && text.equalsIgnoreCase(langService.getMessage(BUTTON_BACK, chatId))) {
            telegramUserService.goBack(chatId);
            botService.send(getMessageForState(user));
            return;
        }

        if (text != null && text.equals("/start")) {
            if (user.getState() == UserState.START) {
                botService.send(sendMessageService.firstStart(user));
            } else {
                user.getStateHistory().clear();
                telegramUserService.setState(chatId, UserState.DEFAULT);
                botService.send(sendMessageService.welcomeUser(user));
            }
            return;
        }

        if (user.getState() == UserState.INPUT_PHONE_NUMBER) {
            handleHotToursPhoneInput(user, message);
            return;
        }

        if (text != null) {
            switch (user.getState()) {
                case START -> handleStartState(user, text);
                case DEFAULT -> handleDefaultState(user, text);
                case SETTINGS -> handleSettingsState(user, text);
                default -> botService.send(sendMessageService.unknownCommand(user.getChatId()));
            }
        }
    }

    private void handleStartState(TelegramUser user, String text) {
        Long chatId = user.getChatId();
        if (text.equals(langService.getMessage(BUTTON_RUSSIAN_LANGUAGE, chatId))) {
            botService.send(sendMessageService.setLang("ru", user));
            telegramUserService.setState(chatId, UserState.DEFAULT);
        } else if (text.equals(langService.getMessage(BUTTON_UZBEK_LANGUAGE, chatId))) {
            botService.send(sendMessageService.setLang("uz", user));
            telegramUserService.setState(chatId, UserState.DEFAULT);
        } else
            botService.send(new SendMessage(chatId.toString(), "Iltimos tilni tanlang!"));
    }

    private void handleDefaultState(TelegramUser user, String text) {
        Long chatId = user.getChatId();
        if (text.equals(langService.getMessage(BUTTON_CHANGE_LANGUAGE, chatId))) {
            telegramUserService.setState(chatId, UserState.SETTINGS);
            botService.send(sendMessageService.changeLang(chatId));
        } else if (text.equals(langService.getMessage(BUTTON_HOT_TOURS, chatId))) {
            telegramUserService.setState(chatId, UserState.INPUT_PHONE_NUMBER);
            botService.send(sendMessageService.requestPhoneNumber(chatId));
        } else if (text.equals(langService.getMessage(BUTTON_SPECIAL_OFFERS, chatId))) {
            // TODO
        } else {
            botService.send(sendMessageService.unknownCommand(chatId));
        }
    }

    private void handleSettingsState(TelegramUser user, String text) {
        Long chatId = user.getChatId();
        if (text.equals(langService.getMessage(BUTTON_RUSSIAN_LANGUAGE, chatId))) {
            botService.send(sendMessageService.setLang("ru", user));
            telegramUserService.setState(chatId, UserState.DEFAULT);
        } else if (text.equals(langService.getMessage(BUTTON_UZBEK_LANGUAGE, chatId))) {
            botService.send(sendMessageService.setLang("uz", user));
            telegramUserService.setState(chatId, UserState.DEFAULT);
        }
    }

    private void handleHotToursPhoneInput(TelegramUser user, Message message) {
        Long chatId = user.getChatId();

        String phoneNumber;

        if (message.hasContact()) {

            phoneNumber = message.getContact().getPhoneNumber();

            if (!phoneNumber.startsWith("+")) phoneNumber = "+" + phoneNumber;

        } else phoneNumber = "+998" + message.getText();

        if (!phoneNumber.matches(Regex.PHONE_NUMBER)) {
            telegramUserService.setState(chatId, UserState.INPUT_PHONE_NUMBER);
            botService.send(sendMessageService.invalidPhoneNumber(message.getChatId()));
            return;
        }
//                    if (!Objects.equals(user == null ? null : user.getLastPhoneNumber(), phoneNumber)) {
//                        smsService.send(telegramUser, phoneNumber);
//                        return;
//                    }
//                    smsService.savePhoneNumber(telegramUser, phoneNumber);
    }

    private SendMessage getMessageForState(TelegramUser user) {
        return switch (user.getState()) {
            case START -> sendMessageService.firstStart(user);
            case DEFAULT -> sendMessageService.welcomeUser(user);
            case SETTINGS -> sendMessageService.changeLang(user.getChatId());
            default -> sendMessageService.unknownCommand(user.getChatId());
        };
    }
}