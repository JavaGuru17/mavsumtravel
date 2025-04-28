package uz.mavsumtravel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mavsumtravel.service.telegramservice.BotService;
import uz.mavsumtravel.service.TelegramUserService;
import uz.mavsumtravel.service.telegramservice.SendMessageService;
import uz.mavsumtravel.service.telegramservice.TelegramService;

@RestController
@RequestMapping("/api/v1/telegram")
@RequiredArgsConstructor
public class TelegramController {
    private final BotService botService;
    private final SendMessageService sendMessageService;
    private final TelegramUserService telegramUserService;
    private final TelegramService telegramService;

    @PostMapping
    public void getUpdates(@RequestBody Update update) throws TelegramApiException {
        System.out.println(update);
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.getText() != null || message.getContact() != null) {
                telegramService.handleMessage(message);
            }
        }

         else if (update.hasCallbackQuery()) {
             telegramService.handleCallbackQuery(update.getCallbackQuery());
         }
    }
}
