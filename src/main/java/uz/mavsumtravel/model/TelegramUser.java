package uz.mavsumtravel.model;

import jakarta.persistence.*;
import lombok.*;
import uz.mavsumtravel.model.enums.UserState;

import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection
    @Builder.Default
    private List<UserState> stateHistory = new ArrayList<>();

    private String lang;
}
