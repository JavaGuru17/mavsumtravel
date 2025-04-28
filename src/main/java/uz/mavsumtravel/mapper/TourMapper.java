package uz.mavsumtravel.mapper;

import uz.mavsumtravel.dto.TourDto;
import uz.mavsumtravel.model.Tour;

public interface TourMapper {
    static Tour toTour(TourDto dto) {
        return Tour.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .type(dto.getType())
                .imgPath(dto.getImgPath())
                .imgId(dto.getImgId())
                .isActive(true)
                .build();
    }

    static TourDto toDto(Tour tour) {
        return TourDto.builder()
                .id(tour.getId())
                .description(tour.getDescription())
                .type(tour.getType())
                .imgPath(tour.getImgPath())
                .imgId(tour.getImgId())
                .isActive(true)
                .build();
    }
}
