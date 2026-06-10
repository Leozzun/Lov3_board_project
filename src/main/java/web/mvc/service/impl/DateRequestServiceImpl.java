package web.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Board;
import web.mvc.domain.DateRequest;
import web.mvc.domain.Member;
import web.mvc.dto.dateRequest.DateRequestReqDto;
import web.mvc.dto.dateRequest.DateRequestResDto;
import web.mvc.exception.BoardException;
import web.mvc.exception.DateRequestException;
import web.mvc.exception.MemberException;
import web.mvc.repository.BoardRepository;
import web.mvc.repository.DateRequestRepository;
import web.mvc.repository.MemberRepository;
import web.mvc.service.DateRequestService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DateRequestServiceImpl implements DateRequestService {

    private final DateRequestRepository dateRequestRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    public DateRequestResDto sendRequest(DateRequestReqDto dateRequestReqDto, Long boardId, Long senderNo) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("보드 아이디 없음"));

        if(board.getMember().getMemberNo().equals(senderNo)) {
            throw new BoardException("본인 게시글에 신청 불가");
        }

        Member member = memberRepository.findById(senderNo).orElseThrow(() -> new MemberException("회원 없음"));

        DateRequest date = new DateRequest();

        date.setMessage(dateRequestReqDto.getMessage());
        date.setBoard(board);
        date.setSender(member);

        DateRequest saved = dateRequestRepository.save(date);

        return new DateRequestResDto(saved);
    }

    @Override
    public List<DateRequestResDto> getReceivedRequestsByBoard(Long boardId, Long memberNo) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("보드 없음"));

        if(!board.getMember().getMemberNo().equals(memberNo)) {
            throw new BoardException("본인 게시글만 조회 가능");
        }

        return dateRequestRepository.findByBoard_BoardId(boardId).stream().map(d -> new DateRequestResDto(d)).toList();
    }

    @Override
    public List<DateRequestResDto> getAllReceivedRequests(Long memberNo) {

        Member member = memberRepository.findById(memberNo).orElseThrow(() -> new MemberException("회원 없음"));

        return dateRequestRepository.findByBoard_Member_MemberNo(memberNo).stream().map(d -> new DateRequestResDto(d)).toList();
    }

    @Override
    public List<DateRequestResDto> getSentRequests(Long memberNo) {

        Member member = memberRepository.findById(memberNo).orElseThrow(() -> new MemberException("회원 없음"));

        return dateRequestRepository.findBySender_MemberNo(memberNo).stream().map(d -> new DateRequestResDto(d)).toList();
    }

    @Override
    public DateRequestResDto acceptRequest(Long memberNo, Long requestId) {

        DateRequest date = dateRequestRepository.findById(requestId).orElseThrow(() -> new DateRequestException("없음"));

        if(!date.getBoard().getMember().getMemberNo().equals(memberNo)) {
            throw new DateRequestException("권한 없음");
        }

        date.setStatus("ACCEPTED");

        return new DateRequestResDto(date);
    }

    @Override
    public DateRequestResDto rejectRequest(Long memberNo, Long requestId) {

        DateRequest date = dateRequestRepository.findById(requestId).orElseThrow(() -> new DateRequestException("없음"));

        if(!date.getBoard().getMember().getMemberNo().equals(memberNo)) {
            throw new DateRequestException("권한 없음");
        }

        date.setStatus("REJECTED");

        return new DateRequestResDto(date);
    }

    @Override
    public void cancelRequest(Long requestId, Long memberNo) {

        DateRequest date = dateRequestRepository.findById(requestId).orElseThrow(() -> new DateRequestException("없음"));

        if(!date.getSender().getMemberNo().equals(memberNo)) {
            throw new DateRequestException("본인만 취소 가능");
        }

        dateRequestRepository.delete(date);
    }
}
