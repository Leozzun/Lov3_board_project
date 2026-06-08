package web.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Board;
import web.mvc.dto.board.BoardReqDto;
import web.mvc.dto.board.BoardResDto;
import web.mvc.exception.BoardException;
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

        return boardRepository.findAll().stream().map(b -> new BoardResDto(b)).toList();
    }

    @Override
    public BoardResDto getBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException("글번호 없음"));


        return new BoardResDto(board);
    }

    @Override
    public List<BoardResDto> searchBoards(String keyword) {

        boardRepository.searchByKeyword(keyword);

        return List.of();
    }

    @Override
    public List<BoardResDto> getBoardsByPlace(Long placeId) {
        return List.of();
    }

    @Override
    public BoardResDto addBoard(BoardReqDto boardReqDto, Long memberNo) {
        return null;
    }

    @Override
    public BoardResDto updateBoard(Long boardId, BoardReqDto boardReqDto, Long memberNo) {

        return null;
    }

    @Override
    public void deleteBoard(Long boardId, Long memberNo, String role) {

        //Board board = boardRepository.findByMember_MemberNo()

        boardRepository.deleteById(boardId);
    }
}

