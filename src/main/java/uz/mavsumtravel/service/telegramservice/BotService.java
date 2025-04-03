package uz.mavsumtravel.service.telegramservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import uz.mavsumtravel.dto.TelegramResultDto;
import org.telegram.telegrambots.meta.api.objects.WebhookInfo;

import java.io.Serializable;

@Service
public class BotService {

    private static final String BASE_URL = "https://api.telegram.org/bot";
    private static final RestTemplate restTemplate = new RestTemplate();

    private final String token;
    private final String url;

    public BotService(@Value("${telegram.token}") String token, @Value("${telegram.url}") String url) {
        this.token = token;
        this.url = url;
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> void send(Method method) {
        restTemplate.postForObject(BASE_URL + token + "/" + method.getMethod(), method, TelegramResultDto.class);
    }

    public String getWebhookUrl() {
        BotService.WebHookResult resultDto = restTemplate.getForObject(BASE_URL + token + "/getWebhookInfo", BotService.WebHookResult.class);
        if (resultDto.getResult().getUrl() != null) {
            return resultDto.getResult().getUrl();
        }
        return "";
    }

    @AllArgsConstructor
    @Getter
    private static class WebHookResult {
        private boolean ok;
        private WebhookInfo result;
    }

}
