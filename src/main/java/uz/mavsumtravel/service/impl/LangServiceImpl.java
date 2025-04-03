package uz.mavsumtravel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import uz.mavsumtravel.service.LangService;
import uz.mavsumtravel.service.TelegramUserService;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class LangServiceImpl implements LangService {
    private final TelegramUserService telegramUserService;
    private final ResourceBundleMessageSource messageSource;

    @Override
    public String getMessage(String keyword, Long chatId) {
        try {
            return messageSource.getMessage(keyword,null, new Locale(telegramUserService.getLang(chatId)));
        }catch (Exception e){
            return messageSource.getMessage(keyword,null, new Locale("uz"));
        }
    }
}
