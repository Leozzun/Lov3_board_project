package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.Comment;

import java.util.List;

/**
 * 댓글 레포지토리
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 댓글 목록 조회 (작성일 오름차순)
    List<Comment> findByBoard_BoardIdOrderByRegDateAsc(Long boardId);

    // 특정 게시글의 댓글 + 작성자 정보 함께 조회 (fetch join)
    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.board.boardId = :boardId ORDER BY c.regDate ASC")
    List<Comment> findAllWithMemberByBoardId(Long boardId);

    // 특정 회원이 작성한 댓글 목록
    List<Comment> findByMember_MemberNo(Long memberNo);

    // =========================================================
    // TODO: 필요한 쿼리 메서드 추가
    // =========================================================
}
