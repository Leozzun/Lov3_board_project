package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.comment.CommentReqDto;
import web.mvc.dto.comment.CommentResDto;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.CommentService;

import java.util.List;

/**
 * 댓글 컨트롤러
 * 기본 URL: /boards/{boardId}/comments
 */
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * [GET] /boards/{boardId}/comments
     * 특정 게시글의 댓글 목록 조회
     * 인증 불필요
     *
     * @param boardId 게시글 번호 (Path Variable)
     * @return 200 OK + 댓글 목록
     */
    @GetMapping("/boards/{boardId}/comments")
    public ResponseEntity<List<CommentResDto>> getComments(@PathVariable Long boardId) {
        // TODO: 구현
        // return ResponseEntity.ok(commentService.getCommentsByBoard(boardId));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [POST] /boards/{boardId}/comments
     * 댓글 등록
     * 인증 필요
     *
     * @param boardId       게시글 번호 (Path Variable)
     * @param memberDetails 로그인 회원 정보
     * @param dto           댓글 내용 (Body: JSON)
     * @return 201 Created + 등록된 댓글
     */
    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<CommentResDto> addComment(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @RequestBody CommentReqDto dto) {
        // TODO: 구현
        // Long memberNo = memberDetails.getMember().getMemberNo();
        // CommentResDto result = commentService.addComment(boardId, dto, memberNo);
        // return ResponseEntity.status(HttpStatus.CREATED).body(result);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [PUT] /boards/{boardId}/comments/{commentId}
     * 댓글 수정 (본인만 가능)
     * 인증 필요
     *
     * @param boardId       게시글 번호 (Path Variable, 경로 구조용)
     * @param commentId     댓글 번호 (Path Variable)
     * @param memberDetails 로그인 회원 정보
     * @param dto           수정할 내용 (Body: JSON)
     * @return 200 OK + 수정된 댓글
     */
    @PutMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<CommentResDto> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @RequestBody CommentReqDto dto) {
        // TODO: 구현
        // Long memberNo = memberDetails.getMember().getMemberNo();
        // return ResponseEntity.ok(commentService.updateComment(commentId, dto, memberNo));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [DELETE] /boards/{boardId}/comments/{commentId}
     * 댓글 삭제 (본인 또는 관리자)
     * 인증 필요
     *
     * @param boardId       게시글 번호 (Path Variable, 경로 구조용)
     * @param commentId     댓글 번호 (Path Variable)
     * @param memberDetails 로그인 회원 정보
     * @return 204 No Content
     */
    @DeleteMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        // TODO: 구현
        // Long memberNo = memberDetails.getMember().getMemberNo();
        // String role = memberDetails.getMember().getRole();
        // commentService.deleteComment(commentId, memberNo, role);
        // return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
