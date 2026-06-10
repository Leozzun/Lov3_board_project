package web.mvc.dto.dateRequest;

import lombok.Getter;
import web.mvc.domain.DateRequest;
import web.mvc.dto.member.MemberResDto;

import java.time.LocalDateTime;

// 데이트 신청 응답 DTO
@Getter
public class DateRequestResDto {

    private Long requestId;
    private String message;            // 신청 멘트
    private String status;             // PENDING / ACCEPTED / REJECTED
    private MemberResDto sender;       // 신청 보낸 사람
    private MemberResDto receiver;     // 게시글 작성자 (신청 받는 사람)
    private Long boardId;              // 어느 게시글에 대한 신청인지
    private String boardTitle;         // 게시글 제목
    private LocalDateTime regDate;

    public DateRequestResDto(DateRequest dateRequest) {
        this.requestId = dateRequest.getRequestId();
        this.message = dateRequest.getMessage();
        this.status = dateRequest.getStatus();
        this.sender = new MemberResDto(dateRequest.getSender());
        this.receiver = new MemberResDto(dateRequest.getBoard().getMember());
        this.boardId = dateRequest.getBoard().getBoardId();
        this.boardTitle = dateRequest.getBoard().getTitle();
        this.regDate = dateRequest.getRegDate();
    }
}
