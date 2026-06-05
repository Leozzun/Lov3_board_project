package web.mvc.service;

import web.mvc.dto.comment.CommentReqDto;
import web.mvc.dto.comment.CommentResDto;

import java.util.List;

/**
 * 댓글 서비스 인터페이스
 * 실제 구현은 CommentServiceImpl에서 작성
 */
public interface CommentService {

    // 특정 게시글의 댓글 목록 조회
    List<CommentResDto> getCommentsByBoard(Long boardId);

    // 댓글 등록
    CommentResDto addComment(Long boardId, CommentReqDto dto, Long memberNo);

    // 댓글 수정 (본인만 가능)
    CommentResDto updateComment(Long commentId, CommentReqDto dto, Long memberNo);

    // 댓글 삭제 (본인 또는 관리자)
    void deleteComment(Long commentId, Long memberNo, String role);
}
