package web.mvc.dto.place;

import lombok.Getter;
import web.mvc.domain.Place;

// 장소 응답 DTO
@Getter
public class PlaceResDto {

    private Long placeId;
    private String placeName;
    private String placeImg;
    private String placeInfo;

    public PlaceResDto(Place place) {
        this.placeId = place.getPlaceId();
        this.placeName = place.getPlaceName();
        this.placeImg = place.getPlaceImg();
        this.placeInfo = place.getPlaceInfo();
    }
}
