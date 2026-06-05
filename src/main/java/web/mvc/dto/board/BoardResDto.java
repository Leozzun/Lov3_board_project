package web.mvc.dto.board;

import lombok.Getter;
import web.mvc.domain.Board;
import web.mvc.dto.member.MemberResDto;

import java.time.LocalDateTime;

/**
 * 게시글 응답 DTO (서버 → 클라이언트)
 */
@Getter
public class BoardResDto {

    private Long boardId;               // 게시글 번호
    private String title;               // 제목
    private String content;             // 내용
    private int viewCount;              // 조회수
    private MemberResDto member;        // 작성자 정보 (회원 응답 DTO 재사용)
    private LocalDateTime regDate;      // 작성일
    private LocalDateTime updateDate;   // 수정일
    private int commentCount;           // 댓글 수

    /**
     * Entity → DTO 변환 생성자
     */
    public BoardResDto(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.viewCount = board.getViewCount();
        this.member = new MemberResDto(board.getMember());
        this.regDate = board.getRegDate();
        this.updateDate = board.getUpdateDate();
        this.commentCount = board.getComments().size();
        // TODO: 필요한 필드 추가
    }
}
