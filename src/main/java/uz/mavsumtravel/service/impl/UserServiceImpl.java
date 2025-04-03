package uz.mavsumtravel.mavsumtravel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.mavsumtravel.mavsumtravel.exception.NotFoundException;
import uz.mavsumtravel.mavsumtravel.model.User;
import uz.mavsumtravel.mavsumtravel.model.enums.Role;
import uz.mavsumtravel.mavsumtravel.repository.UserRepository;
import uz.mavsumtravel.mavsumtravel.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getByChatId(Long chatId) {
        return userRepository.findByChatId(chatId).orElse(null);
    }

    @Override
    public void setAdminByPhoneNumber(String phoneNumber) {
        if (!(phoneNumber.startsWith("+998"))){
            phoneNumber = "+998" + phoneNumber;
        }
        List<User> byPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);
        if (byPhoneNumber.isEmpty()) {
            throw new NotFoundException("User");
        }
        for (User user : byPhoneNumber) {
            if (!(user.getRole() == Role.ADMIN)){
                user.setRole(Role.ADMIN);
            }
            userRepository.save(user);
        }
    }

    @Override
    public void setAdminByUsername(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            throw new NotFoundException("User");
        }
        User user = byUsername.get();
        if (!(user.getRole() == Role.ADMIN)){
            user.setRole(Role.ADMIN);
        }
        userRepository.save(user);
    }

    @Override
    public void removeAdminByPhoneNumber(String phoneNumber) {
        if (!(phoneNumber.startsWith("+998"))){
            phoneNumber = "+998" + phoneNumber;
        }
        List<User> byPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);
        if (byPhoneNumber.isEmpty()) {
            throw new NotFoundException("User");
        }
        for (User user : byPhoneNumber) {
            if (user.getRole() == Role.ADMIN){
                user.setRole(Role.USER);
            }
            userRepository.save(user);
        }
    }

    @Override
    public void removeAdminByUsername(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            throw new NotFoundException("User");
        }
        User user = byUsername.get();
        if (user.getRole() == Role.ADMIN){
            user.setRole(Role.USER);
        }
        userRepository.save(user);
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        return byUsername.orElse(null);
    }

    @Override
    public List<User> getByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }
}
