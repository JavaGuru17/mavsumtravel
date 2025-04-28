package uz.mavsumtravel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.mavsumtravel.dto.TourDto;
import uz.mavsumtravel.exception.NotFoundException;
import uz.mavsumtravel.mapper.TourMapper;
import uz.mavsumtravel.model.Tour;
import uz.mavsumtravel.model.enums.TourType;
import uz.mavsumtravel.repository.TourRepository;
import uz.mavsumtravel.service.TourService;
import uz.mavsumtravel.util.Validations;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;

    @Override
    public TourDto create(TourDto dto) {
        return TourMapper.toDto(tourRepository.save(TourMapper.toTour(dto)));
    }

    @Override
    public TourDto update(TourDto dto) {
        Tour tour = tourRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Tour"));

        return TourMapper.toDto(tourRepository.save(Tour.builder()
                .id(dto.getId())
                .description(Validations.requireNonNullElse(dto.getDescription(), tour.getDescription()))
                .type(Validations.requireNonNullElse(dto.getType(), tour.getType()))
                .imgPath(Validations.requireNonNullElse(dto.getImgPath(), tour.getImgPath()))
                .imgId(Validations.requireNonNullElse(dto.getImgId(), tour.getImgId()))
                .isActive(Validations.requireNonNullElse(dto.getIsActive(), tour.getIsActive()))
                .build()));

    }

    @Override
    public TourDto getById(Long id) {
//        return TourMapper.toDto(tourRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Tour")));
        //TODO remove
        if (id == 1L) {
            return TourDto.builder()
                    .id(1L)
                    .description("Тур #1: Поездка в Самарканд")
                    .type(TourType.HOT_TOUR)
                    .imgPath("https://picsum.photos/200")
                    .build();
        } else if (id == 2L) {
            return TourDto.builder()
                    .id(2L)
                    .description("Тур #2: Экскурсия по Ташкенту")
                    .type(TourType.HOT_TOUR)
                    .imgPath("https://picsum.photos/200")
                    .build();
        }
        throw new RuntimeException("Tour not found");
    }

    @Override
    public void delete(Long id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tour"));

        tour.setIsActive(false);

        tourRepository.save(tour);
    }

    @Override
    public List<TourDto> getAll() {
        return tourRepository.findAll()
                .stream()
                .map(TourMapper::toDto)
                .toList();
    }

    @Override
    public List<TourDto> getAllByType(TourType type) {
        return tourRepository.findAllByType(type)
                .stream()
                .map(TourMapper::toDto)
                .toList();
    }

    @Override
    public List<Long> getAllIdsByType(TourType type) {
        //return tourRepository.findAllIdsByType(type);
        return Arrays.asList(1L, 2L);
    }
}
