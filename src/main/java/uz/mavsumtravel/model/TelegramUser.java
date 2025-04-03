package uz.mavsumtravel.mavsumtravel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import uz.mavsumtravel.mavsumtravel.model.enums.UserState;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUser {
    @Id
    private Long chatId;

    @Enumerated(EnumType.STRING)
    private UserState state;

    private String lang;
}
