package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.DateRequest;

import java.util.List;

public interface DateRequestRepository extends JpaRepository<DateRequest, Long> {

    List<DateRequest> findByBoard_BoardId(Long boardId);

    List<DateRequest> findBySender_MemberNo(Long memberNo);

    List<DateRequest> findByBoard_Member_MemberNo(Long memberNo);

    @Modifying
    @Query("DELETE FROM DateRequest d WHERE d.sender.memberNo = :memberNo")
    void deleteBySenderMemberNo(@Param("memberNo") Long memberNo);

    @Modifying
    @Query("DELETE FROM DateRequest d WHERE d.board.member.memberNo = :memberNo")
    void deleteByBoardMemberMemberNo(@Param("memberNo") Long memberNo);
}
