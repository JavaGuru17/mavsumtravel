package uz.mavsumtravel.service;

import org.springframework.stereotype.Service;

@Service
public interface LangService {
    String getMessage(String keyword, Long chatId);
}
