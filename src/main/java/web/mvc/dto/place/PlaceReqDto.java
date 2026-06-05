package web.mvc.dto.place;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 장소 등록 / 수정 요청 DTO (관리자 전용)
@Getter @Setter @NoArgsConstructor
public class PlaceReqDto {

    private String placeName;
    private String placeImg;
    private String placeInfo;
}
