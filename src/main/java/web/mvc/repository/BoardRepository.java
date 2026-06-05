package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.Board;

import java.util.List;
import java.util.Optional;

/**
 * 게시글 레포지토리
 */
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 작성자(memberNo)로 게시글 목록 조회
    List<Board> findByMember_MemberNo(Long memberNo);

    // 제목에 키워드가 포함된 게시글 검색
    List<Board> findByTitleContaining(String keyword);

    // 제목 또는 내용에 키워드가 포함된 게시글 검색
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
    List<Board> searchByKeyword(@Param("keyword") String keyword);

    // 게시글 + 작성자 정보 함께 조회 (N+1 문제 방지용 fetch join)
    @Query("SELECT b FROM Board b JOIN FETCH b.member")
    List<Board> findAllWithMember();

    // =========================================================
    // TODO: 필요한 쿼리 메서드 추가
    // 예: 조회수 많은 순 정렬 → List<Board> findAllByOrderByViewCountDesc();
    // =========================================================
}
