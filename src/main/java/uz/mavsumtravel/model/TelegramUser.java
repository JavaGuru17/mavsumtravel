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

    @Transient
    private List<UserState> stateHistory = new ArrayList<>();

    @Transient
    private List<Long> hotTourIds = new ArrayList<>();

    private int currentHotTourIndex;

    @Transient
    private List<Long> specialOfferIds = new ArrayList<>();

    private int currentSpecialOfferIndex;

    private String lang;
}
