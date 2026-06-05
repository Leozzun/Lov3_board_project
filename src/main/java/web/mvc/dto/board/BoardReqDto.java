package web.mvc.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 등록/수정 요청 DTO (클라이언트 → 서버)
 * POST /boards, PUT /boards/{id} 요청 시 Body에 담아 전달
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardReqDto {

    private String title;       // 게시글 제목
    private String content;     // 게시글 내용

    // memberNo는 JWT 토큰에서 추출하므로 클라이언트에서 전달하지 않음
    // (보안상 이유: 클라이언트가 임의로 다른 회원번호를 넣을 수 없게)

    // =========================================================
    // TODO: 필요한 필드 추가 (예: 카테고리ID 등)
    // =========================================================
}
