package web.mvc.service;

import web.mvc.dto.board.BoardReqDto;
import web.mvc.dto.board.BoardResDto;

import java.util.List;

public interface BoardService {

    List<BoardResDto> getAllBoards();

    BoardResDto getBoard(Long boardId);

    List<BoardResDto> searchBoards(String keyword);

    List<BoardResDto> getBoardsByPlace(Long placeId);

    BoardResDto addBoard(BoardReqDto boardReqDto, Long memberNo);

    BoardResDto updateBoard(Long boardId, BoardReqDto boardReqDto, Long memberNo);

    void deleteBoard(Long boardId, Long memberNo, String role);
}
