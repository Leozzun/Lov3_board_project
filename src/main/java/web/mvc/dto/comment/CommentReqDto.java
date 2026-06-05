package web.mvc.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 등록/수정 요청 DTO (클라이언트 → 서버)
 * POST /boards/{boardId}/comments, PUT /boards/{boardId}/comments/{commentId}
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentReqDto {

    private String content;     // 댓글 내용

    // boardId, memberNo는 URL경로 또는 JWT 토큰에서 추출
}
