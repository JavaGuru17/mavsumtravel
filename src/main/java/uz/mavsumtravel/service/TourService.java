package uz.mavsumtravel.service;

import org.jvnet.hk2.annotations.Service;
import uz.mavsumtravel.dto.TourDto;
import uz.mavsumtravel.model.enums.TourType;

import java.util.List;

@Service
public interface TourService {
    TourDto create(TourDto dto);

    TourDto update(TourDto dto);

    TourDto getById(Long id);

    void delete(Long id);

    List<TourDto> getAll();

    List<TourDto> getAllByType(TourType type);

    List<Long> getAllIdsByType(TourType type);
}
