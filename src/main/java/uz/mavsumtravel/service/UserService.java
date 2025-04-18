package uz.mavsumtravel.service;

import org.springframework.stereotype.Service;
import uz.mavsumtravel.model.User;

import java.util.List;

@Service
public interface UserService {

    void save(User user);

    List<User> getAll();

    User getByChatId(Long chatId);

    boolean isAdmin(Long chatId);

    void setAdminByPhoneNumber(String phoneNumber);

    void setAdminByUsername(String username);

    void removeAdminByPhoneNumber(String phoneNumber);

    void removeAdminByUsername(String username);

    User getByUsername(String username);

    List<User> getByPhoneNumber(String phoneNumber);
}
