package uz.mavsumtravel.mavsumtravel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mavsumtravel.mavsumtravel.service.telegramservice.BotService;
import uz.mavsumtravel.mavsumtravel.service.telegramservice.SendMessageService;

@RestController
@RequestMapping("/api/v1/telegram")
@RequiredArgsConstructor
public class TelegramController {

    private final BotService botService;
    private final SendMessageService sendMessageService;

    @PostMapping
    public void getUpdates(@RequestBody Update update) throws TelegramApiException {
        System.out.println(update);

        botService.send(sendMessageService);

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(update.getMessage().getText());
    }

    @GetMapping("/")
    public String home() {
        return "Hello World";
    }
}
