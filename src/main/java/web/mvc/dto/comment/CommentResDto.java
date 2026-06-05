package web.mvc.dto.comment;

import lombok.Getter;
import web.mvc.domain.Comment;
import web.mvc.dto.member.MemberResDto;

import java.time.LocalDateTime;

/**
 * 댓글 응답 DTO (서버 → 클라이언트)
 */
@Getter
public class CommentResDto {

    private Long commentId;             // 댓글 번호
    private String content;             // 댓글 내용
    private MemberResDto member;        // 작성자 정보
    private Long boardId;               // 어느 게시글의 댓글인지
    private LocalDateTime regDate;      // 작성일
    private LocalDateTime updateDate;   // 수정일

    /**
     * Entity → DTO 변환 생성자
     */
    public CommentResDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.member = new MemberResDto(comment.getMember());
        this.boardId = comment.getBoard().getBoardId();
        this.regDate = comment.getRegDate();
        this.updateDate = comment.getUpdateDate();
    }
}
