package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import web.mvc.dto.board.BoardReqDto;
import web.mvc.dto.board.BoardResDto;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.BoardService;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<Page<BoardResDto>> getAllBoards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(boardService.getAllBoards(page, size));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResDto> getBoard(@PathVariable Long boardId) {

        BoardResDto result = boardService.getBoard(boardId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/my")
    public ResponseEntity<List<BoardResDto>> getMyBoards(@AuthenticationPrincipal CustomMemberDetails memberDetails) {
        return ResponseEntity.ok(boardService.getMyBoards(memberDetails.getMember().getMemberNo()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BoardResDto>> searchBoards(@RequestParam String keyword) {

        List<BoardResDto> result = boardService.searchBoards(keyword);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<BoardResDto>> getBoardsByPlace(@PathVariable Long placeId) {

        List<BoardResDto> result = boardService.getBoardsByPlace(placeId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<BoardResDto> addBoard(@AuthenticationPrincipal CustomMemberDetails memberDetails, @RequestBody BoardReqDto boardReqDto) {

        BoardResDto result = boardService.addBoard(boardReqDto, memberDetails.getMember().getMemberNo());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResDto> updateBoard(@PathVariable Long boardId, @RequestBody BoardReqDto boardReqDto, @AuthenticationPrincipal CustomMemberDetails memberDetails) {

        BoardResDto result = boardService.updateBoard(boardId, boardReqDto, memberDetails.getMember().getMemberNo());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal CustomMemberDetails memberDetails) {

        boardService.deleteBoard(boardId, memberDetails.getMember().getMemberNo(), memberDetails.getMember().getRole());

        return ResponseEntity.noContent().build();
    }
}
