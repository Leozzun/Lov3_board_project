package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.board.BoardReqDto;
import web.mvc.dto.board.BoardResDto;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.BoardService;

import java.util.List;

/**
 * 게시글 컨트롤러
 * 기본 URL: /boards
 */
@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * [GET] /boards
     * 게시글 전체 목록 조회
     * 인증 불필요 (누구나 볼 수 있음)
     *
     * @return 200 OK + 게시글 목록
     */
    @GetMapping
    public ResponseEntity<List<BoardResDto>> getAllBoards() {
        // TODO: 구현
        // return ResponseEntity.ok(boardService.getAllBoards());
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [GET] /boards/{boardId}
     * 게시글 단건 조회
     * 인증 불필요
     *
     * @param boardId 게시글 번호 (Path Variable)
     * @return 200 OK + 게시글 상세 정보
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResDto> getBoard(@PathVariable Long boardId) {
        // TODO: 구현
        // return ResponseEntity.ok(boardService.getBoard(boardId));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [GET] /boards/search?keyword={keyword}
     * 게시글 검색 (제목/내용)
     * 인증 불필요
     *
     * @param keyword 검색어 (Query Parameter)
     * @return 200 OK + 검색 결과 목록
     */
    @GetMapping("/search")
    public ResponseEntity<List<BoardResDto>> searchBoards(@RequestParam String keyword) {
        // TODO: 구현
        // return ResponseEntity.ok(boardService.searchBoards(keyword));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [POST] /boards
     * 게시글 등록
     * 인증 필요 (ROLE_USER 이상)
     *
     * @param memberDetails 로그인 회원 정보 (JWT에서 자동 추출)
     * @param dto           게시글 내용 (Body: JSON)
     * @return 201 Created + 등록된 게시글
     */
    @PostMapping
    public ResponseEntity<BoardResDto> addBoard(
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @RequestBody BoardReqDto dto) {
        // TODO: 구현
        // Long memberNo = memberDetails.getMember().getMemberNo();
        // BoardResDto result = boardService.addBoard(dto, memberNo);
        // return ResponseEntity.status(HttpStatus.CREATED).body(result);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [PUT] /boards/{boardId}
     * 게시글 수정 (본인만 가능)
     * 인증 필요
     *
     * @param boardId       게시글 번호 (Path Variable)
     * @param memberDetails 로그인 회원 정보
     * @param dto           수정할 내용 (Body: JSON)
     * @return 200 OK + 수정된 게시글
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResDto> updateBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @RequestBody BoardReqDto dto) {
        // TODO: 구현
        // Long memberNo = memberDetails.getMember().getMemberNo();
        // return ResponseEntity.ok(boardService.updateBoard(boardId, dto, memberNo));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [DELETE] /boards/{boardId}
     * 게시글 삭제 (본인 또는 관리자)
     * 인증 필요
     *
     * @param boardId       게시글 번호 (Path Variable)
     * @param memberDetails 로그인 회원 정보
     * @return 204 No Content
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        // TODO: 구현
        // Long memberNo = memberDetails.getMember().getMemberNo();
        // String role = memberDetails.getMember().getRole();
        // boardService.deleteBoard(boardId, memberNo, role);
        // return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
