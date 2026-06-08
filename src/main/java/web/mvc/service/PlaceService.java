package web.mvc.service;

import web.mvc.dto.place.PlaceReqDto;
import web.mvc.dto.place.PlaceResDto;

import java.util.List;

public interface PlaceService {

    List<PlaceResDto> getAllPlaces();
    List<PlaceResDto> searchPlaces(String keyword);

    PlaceResDto getPlace(Long placeId);

    PlaceResDto addPlace(PlaceReqDto placeReqDto);

    PlaceResDto updatePlace(PlaceReqDto placeReqDto, Long placeId);

    void deletePlace(Long placeId);
}
