package web.mvc.service;

import web.mvc.dto.board.BoardReqDto;
import web.mvc.dto.board.BoardResDto;

import java.util.List;

/**
 * 게시글 서비스 인터페이스
 * 실제 구현은 BoardServiceImpl에서 작성
 */
public interface BoardService {

    // 게시글 목록 전체 조회
    List<BoardResDto> getAllBoards();

    // 게시글 단건 조회 (조회수 증가 포함)
    BoardResDto getBoard(Long boardId);

    // 게시글 검색 (제목/내용 키워드)
    List<BoardResDto> searchBoards(String keyword);

    // 게시글 등록
    BoardResDto addBoard(BoardReqDto dto, Long memberNo);

    // 게시글 수정 (본인만 가능)
    BoardResDto updateBoard(Long boardId, BoardReqDto dto, Long memberNo);

    // 게시글 삭제 (본인 또는 관리자)
    void deleteBoard(Long boardId, Long memberNo, String role);
}
