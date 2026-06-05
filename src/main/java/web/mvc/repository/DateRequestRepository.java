package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import web.mvc.domain.DateRequest;

import java.util.List;

public interface DateRequestRepository extends JpaRepository<DateRequest, Long> {

    // 특정 게시글에 온 신청 목록 (게시글 작성자가 확인)
    List<DateRequest> findByBoard_BoardId(Long boardId);

    // 내가 보낸 신청 목록
    List<DateRequest> findBySender_MemberNo(Long memberNo);

    // 두 회원 간 수락된 매칭이 있는지 확인 (프로필 공개 여부 판단)
    // sender가 memberA이고 board 작성자가 memberB이거나 그 반대인 ACCEPTED 신청
    @Query("SELECT COUNT(r) > 0 FROM DateRequest r " +
           "WHERE r.status = 'ACCEPTED' AND (" +
           "  (r.sender.memberNo = :memberA AND r.board.member.memberNo = :memberB) OR " +
           "  (r.sender.memberNo = :memberB AND r.board.member.memberNo = :memberA)" +
           ")")
    boolean existsMatchBetween(Long memberA, Long memberB);
}
