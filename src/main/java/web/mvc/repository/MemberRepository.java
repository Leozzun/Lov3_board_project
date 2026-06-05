package web.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.mvc.domain.Member;

import java.util.Optional;

/**
 * 회원 레포지토리
 * JpaRepository를 상속받으면 기본 CRUD 메서드가 자동 생성됨
 * (save, findById, findAll, deleteById 등)
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    // ID로 회원 조회 (로그인, 중복체크에 사용)
    Optional<Member> findById(String id);

    // ID 존재 여부 확인 (중복체크)
    boolean existsById(String id);

    // 이메일로 회원 조회
    Optional<Member> findByEmail(String email);

    // =========================================================
    // TODO: 필요한 쿼리 메서드 추가
    // 예: 이름으로 검색 → List<Member> findByNameContaining(String name);
    // =========================================================
}
