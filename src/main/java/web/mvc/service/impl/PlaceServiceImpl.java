package web.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Place;
import web.mvc.dto.place.PlaceReqDto;
import web.mvc.dto.place.PlaceResDto;
import web.mvc.exception.PlaceException;
import web.mvc.repository.PlaceRepository;
import web.mvc.service.PlaceService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;


    @Override
    public List<PlaceResDto> getAllPlaces() {

        return placeRepository.findAll().stream().map(p -> new PlaceResDto(p)).toList();
    }

    @Override
    public List<PlaceResDto> searchPlaces(String keyword) {

        return placeRepository.findByPlaceNameContaining(keyword).stream().map(p -> new PlaceResDto(p)).toList();
    }

    @Override
    public PlaceResDto getPlace(Long placeId) {

        Place  place = placeRepository.findById(placeId).orElseThrow(() -> new PlaceException("장소 없음"));

        return new PlaceResDto(place);
    }


    /////////////// 관리자용 장소 관리 /////////////

    @Override
    public PlaceResDto addPlace(PlaceReqDto placeReqDto) {

        Place place = new Place();
        place.setPlaceName(placeReqDto.getPlaceName());
        place.setPlaceImg(placeReqDto.getPlaceImg());
        place.setPlaceInfo(placeReqDto.getPlaceInfo());

        Place saved = placeRepository.save(place);

        return new PlaceResDto(saved);
    }

    @Override
    public PlaceResDto updatePlace(PlaceReqDto placeReqDto, Long placeId) {

        Place place = placeRepository.findById(placeId).orElseThrow(() -> new PlaceException("장소 수정 오류"));
        place.setPlaceName(placeReqDto.getPlaceName());
        place.setPlaceImg(placeReqDto.getPlaceImg());
        place.setPlaceInfo(placeReqDto.getPlaceInfo());

        return new PlaceResDto(place);
    }

    @Override
    public void deletePlace(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new PlaceException("장소 삭제 오류"));

        placeRepository.delete(place);
    }
}
