package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.mvc.domain.DateRequest;

import java.util.List;

public interface DateRequestRepository extends JpaRepository<DateRequest, Long> {

    List<DateRequest> findByBoard_BoardId(Long boardId);

    List<DateRequest> findBySender_MemberNo(Long memberNo);
}
