package web.mvc.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 게시글 등록 / 수정 요청 DTO
// placeId는 필수 (장소 없이 게시글 등록 불가)
@Getter @Setter @NoArgsConstructor
public class BoardReqDto {

    private String title;
    private String content;
    private Long placeId;      // 선택한 장소 ID (필수)
}
