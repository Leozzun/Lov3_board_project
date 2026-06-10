package web.mvc.service;

import org.springframework.data.domain.Page;
import web.mvc.dto.board.BoardReqDto;
import web.mvc.dto.board.BoardResDto;

import java.util.List;

public interface BoardService {

    Page<BoardResDto> getAllBoards(int page, int size);

    BoardResDto getBoard(Long boardId);

    List<BoardResDto> getMyBoards(Long memberNo);

    List<BoardResDto> searchBoards(String keyword);

    List<BoardResDto> getBoardsByPlace(Long placeId);

    BoardResDto addBoard(BoardReqDto boardReqDto, Long memberNo);

    BoardResDto updateBoard(Long boardId, BoardReqDto boardReqDto, Long memberNo);

    void deleteBoard(Long boardId, Long memberNo, String role);
}
