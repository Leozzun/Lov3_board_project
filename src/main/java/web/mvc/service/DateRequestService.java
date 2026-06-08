package web.mvc.service;

import web.mvc.dto.dateRequest.DateRequestReqDto;
import web.mvc.dto.dateRequest.DateRequestResDto;

import java.util.List;

public interface DateRequestService {

    DateRequestResDto sendRequest(DateRequestReqDto dateRequestReqDto, Long boardId, Long senderNo);

    // 특정 게시글에 들어온 데이트 신청 목록 조회 (게시글 작성자만 가능)
    List<DateRequestResDto> getReceivedRequestsByBoard(Long boardId, Long memberNo);

    // 내가 받은 데이트 신청 전체 목록 조회
    List<DateRequestResDto> getAllReceivedRequests(Long memberNo);

    // 내가 보낸 데이트 신청 목록 조회
    List<DateRequestResDto> getSentRequests(Long memberNo);

    // 데이트 신청 수락 → status: ACCEPTED
    DateRequestResDto acceptRequest(Long memberNo, Long requestId);

    // 데이트 신청 거절 → status: REJECTED
    DateRequestResDto rejectRequest(Long memberNo, Long requestId);

    // 내가 보낸 신청 취소 (삭제)
    void cancelRequest(Long requestId, Long memberNo);
}
