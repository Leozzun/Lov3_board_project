package web.mvc.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 게시글 + 작성자 + 장소 한번에 조회 (목록용, 페이징)
    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member JOIN FETCH b.place",
           countQuery = "SELECT COUNT(b) FROM Board b")
    Page<Board> findAllWithMemberAndPlace(Pageable pageable);

    // 제목 또는 내용으로 게시글 검색
    @Query("SELECT b FROM Board b JOIN FETCH b.member JOIN FETCH b.place " +
           "WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
    List<Board> searchByKeyword(@Param("keyword") String keyword);

    // 특정 장소의 게시글 목록
    List<Board> findByPlace_PlaceId(Long placeId);

    // 특정 회원의 게시글 목록 (최신순)
    List<Board> findByMember_MemberNoOrderByBoardIdDesc(Long memberNo);
}
