package web.mvc.dto.board;

import lombok.Getter;
import web.mvc.domain.Board;
import web.mvc.dto.member.MemberResDto;
import web.mvc.dto.place.PlaceResDto;

import java.time.LocalDateTime;

// 게시글 응답 DTO
@Getter
public class BoardResDto {

    private Long boardId;
    private String title;
    private String content;
    private int viewCount;
    private MemberResDto member;       // 작성자 기본 프로필 (아이디, 사진만)
    private PlaceResDto place;         // 연결된 장소 정보
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    public BoardResDto(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.viewCount = board.getViewCount();
        this.member = new MemberResDto(board.getMember());
        this.place = new PlaceResDto(board.getPlace());
        this.regDate = board.getRegDate();
        this.updateDate = board.getUpdateDate();
    }
}
