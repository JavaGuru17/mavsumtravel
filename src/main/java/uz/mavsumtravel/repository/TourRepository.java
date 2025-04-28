package uz.mavsumtravel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.mavsumtravel.model.Tour;
import uz.mavsumtravel.model.enums.TourType;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    @Query("SELECT t FROM Tour t WHERE t.type = :tourType")
    List<Tour> findAllByType(@Param("tourType") TourType tourType);

    @Query("SELECT t.id FROM Tour t WHERE t.type = :tourType")
    List<Long> findAllIdsByType(@Param("tourType") TourType tourType);
}
