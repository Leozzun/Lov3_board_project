package web.mvc.service.impl;

import web.mvc.dto.place.PlaceReqDto;
import web.mvc.dto.place.PlaceResDto;
import web.mvc.service.PlaceService;

import java.util.List;

public class PlaceServiceImpl implements PlaceService {


    @Override
    public List<PlaceResDto> getAllPlaces() {
        return List.of();
    }

    @Override
    public List<PlaceResDto> searchPlaces(String keyword) {
        return List.of();
    }

    @Override
    public PlaceResDto getPlace(Long placeId) {
        return null;
    }

    @Override
    public PlaceResDto addPlace(PlaceReqDto placeReqDto) {
        return null;
    }

    @Override
    public PlaceResDto updatePlace(PlaceReqDto placeReqDto, Long placeId) {
        return null;
    }

    @Override
    public void deletePlace(Long placeId) {

    }
}
