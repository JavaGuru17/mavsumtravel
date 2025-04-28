package uz.mavsumtravel.dto;

import lombok.*;
import uz.mavsumtravel.model.enums.TourType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourDto {
    private Long id;

    private String description;

    private TourType type;

    private String imgPath;

    private String imgId;

    private Boolean isActive;
}
