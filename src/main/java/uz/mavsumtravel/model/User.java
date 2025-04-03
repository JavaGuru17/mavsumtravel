package uz.mavsumtravel.mavsumtravel.model;

import jakarta.persistence.*;
import lombok.*;
import uz.mavsumtravel.mavsumtravel.model.enums.Role;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    private String phoneNumber;

    private Long chatId;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
}
