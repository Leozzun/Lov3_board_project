package web.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Board;
import web.mvc.domain.Member;
import web.mvc.domain.Place;
import web.mvc.dto.board.BoardReqDto;
import web.mvc.dto.board.BoardResDto;
import web.mvc.exception.BoardException;
import web.mvc.exception.MemberException;
import web.mvc.repository.BoardRepository;
import web.mvc.repository.MemberRepository;
import web.mvc.repository.PlaceRepository;
import web.mvc.service.BoardService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;

    private final PlaceRepository placeRepository;

    @Override
    public List<BoardResDto> getAllBoards() {

        return boardRepository.findAllWithMemberAndPlace().stream().map(b -> new BoardResDto(b)).toList();
    }

    @Override
    public BoardResDto getBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("글번호 없음"));

        board.setViewCount(board.getViewCount() + 1);

        return new BoardResDto(board);
    }

    @Override
    public List<BoardResDto> searchBoards(String keyword) {

        return boardRepository.searchByKeyword(keyword).stream().map(b -> new BoardResDto(b)).toList();
    }

    @Override
    public List<BoardResDto> getBoardsByPlace(Long placeId) {

        return boardRepository.findByPlace_PlaceId(placeId).stream().map(b -> new BoardResDto(b)).toList();
    }

    @Override
    public BoardResDto addBoard(BoardReqDto boardReqDto, Long memberNo) {

        Member member = memberRepository.findById(memberNo).orElseThrow(() -> new MemberException("회원 번호 없음"));

        Place place = placeRepository.findById(boardReqDto.getPlaceId()).orElseThrow(() -> new BoardException("장소 없음"));

        Board board = new Board();

        board.setTitle(boardReqDto.getTitle());
        board.setContent(boardReqDto.getContent());
        board.setPlace(place);
        board.setMember(member);

        Board saved =  boardRepository.save(board);

        return new BoardResDto(saved);
    }

    @Override
    public BoardResDto updateBoard(Long boardId, BoardReqDto boardReqDto, Long memberNo) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("보드 없음"));

        if(!board.getMember().getMemberNo().equals(memberNo)) {
            throw new BoardException("본인 게시글만 수정 가능");
        }

        board.setTitle(boardReqDto.getTitle());
        board.setContent(boardReqDto.getContent());

        return new BoardResDto(board);
    }

    @Override
    public void deleteBoard(Long boardId, Long memberNo, String role) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("글번호 없음"));

        if(board.getMember().getMemberNo().equals(memberNo) || "ROLE_ADMIN".equals(role)) {
            boardRepository.delete(board);
        } else {
            throw new BoardException("권한 없음");
        }
    }
}

