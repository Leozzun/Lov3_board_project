package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.Place;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    // 장소명으로 검색
    List<Place> findByPlaceNameContaining(String keyword);
}
