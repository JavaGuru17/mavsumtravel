package uz.mavsumtravel.mavsumtravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.mavsumtravel.mavsumtravel.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(Long chatId);

    List<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByUsername(String username);
}
