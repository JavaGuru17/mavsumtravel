package uz.mavsumtravel.mavsumtravel.service;

import org.springframework.stereotype.Service;
import uz.mavsumtravel.mavsumtravel.model.enums.LangFields;

@Service
public interface LangService {
    String getMessage(LangFields keyword, Long chatId);
}
