package uz.mavsumtravel.service.telegramservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mavsumtravel.model.TelegramUser;
import uz.mavsumtravel.model.User;
import uz.mavsumtravel.model.enums.Role;
import uz.mavsumtravel.model.enums.UserState;
import uz.mavsumtravel.service.LangService;
import uz.mavsumtravel.service.TelegramUserService;
import uz.mavsumtravel.service.UserService;

import static uz.mavsumtravel.util.LangFields.*;

@Service
@RequiredArgsConstructor
public class TelegramService {
    private final TelegramUserService telegramUserService;
    private final SendMessageService sendMessageService;
    private final BotService botService;
    private final LangService langService;
    private final UserService userService;

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

        if (text != null) {
            switch (user.getState()) {
                case START -> handleStartState(user, text);
                case DEFAULT -> handleDefaultState(user, text);
                case SETTINGS -> handleSettingsState(user, text);
                default -> botService.send(new SendMessage(chatId.toString(), "Неизвестное состояние."));
            }
        }
    }

    private void handleStartState(TelegramUser user, String text) {
        Long chatId = user.getChatId();
        if (text.equals("/start")) {
            user.getStateHistory().clear();
            telegramUserService.setState(chatId, UserState.DEFAULT);
            botService.send(sendMessageService.welcomeUser(user));
        } else if (text.equals(langService.getMessage(BUTTON_RUSSIAN_LANGUAGE, chatId))) {
            botService.send(sendMessageService.setLang("ru", user));
            telegramUserService.setState(chatId, UserState.DEFAULT);
        } else if (text.equals(langService.getMessage(BUTTON_UZBEK_LANGUAGE, chatId))) {
            botService.send(sendMessageService.setLang("uz", user));
            telegramUserService.setState(chatId, UserState.DEFAULT);
        }
    }

    private void handleDefaultState(TelegramUser user, String text) {
        Long chatId = user.getChatId();
        if (text.equals(langService.getMessage(BUTTON_CHANGE_LANGUAGE, chatId))) {
            telegramUserService.setState(chatId, UserState.SETTINGS);
            botService.send(sendMessageService.changeLang(chatId));
        } else if (text.equals(langService.getMessage(BUTTON_HOT_TOURS, chatId))) {
            //TODO
        } else if (text.equals(langService.getMessage(BUTTON_SPECIAL_OFFERS, chatId))) {
            //TODO
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

    private SendMessage getMessageForState(TelegramUser user) {
        return switch (user.getState()) {
            case START -> sendMessageService.firstStart(user);
            case DEFAULT -> sendMessageService.welcomeUser(user);
            case SETTINGS -> sendMessageService.changeLang(user.getChatId());
            default -> new SendMessage(user.getChatId().toString(), "Неизвестное состояние.");
        };
    }

//public void handleCallbackQuery(CallbackQuery callbackQuery) {
//
//    String data = callbackQuery.getData();
//
//    TelegramUser telegramUser = telegramUserService.getByChatId(callbackQuery.getMessage().getChatId());
//
//    Callback callback = Callback.of(data);
//        switch (callback) {}
//            case CHANGE_LANGUAGE ->
//                    botService.send(sendMessageService.changeLang(telegramUser));
//
//            case BACK_TO_MAIN_MENU -> {
//                botService.send(sendMessageService.deleteMessage(telegramUser.getChatId(), callbackQuery.getMessage().getMessageId()));
//                User user = userService.getByChatId(callbackQuery.getMessage().getChatId());
//                if (user != null) {
//                    if (user.getRole().equals(Role.ADMIN)) {
//                        botService.send(sendMessageService.welcomeAdmin(telegramUser));
//                        telegramUserService.setState(callbackQuery.getMessage().getChatId(), UserState.START);
//                    } else {
//                        botService.send(sendMessageService.welcomeUser(telegramUser));
//                        telegramUserService.setState(telegramUser.getChatId(), UserState.DEFAULT);
//                }
//}

    public void handleInput(Message message) {

        TelegramUser telegramUser = telegramUserService.getByChatId(message.getChatId());
        User user = userService.getByChatId(message.getChatId());

        if (user == null) {

            user = User.builder()
                    .chatId(telegramUser.getChatId())
                    .name(message.getFrom().getFirstName())
                    .username(message.getFrom().getUserName())
                    .phoneNumber("")
                    .role(Role.USER)
                    .build();

            userService.save(user);

        }
        if (message.hasText()) {

            String text = message.getText();

        }
    }
}