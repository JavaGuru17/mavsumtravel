package uz.mavsumtravel.model;

import jakarta.persistence.*;
import lombok.*;
import uz.mavsumtravel.model.enums.Role;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
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
