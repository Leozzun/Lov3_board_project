package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 회원 엔티티
 * DB 테이블명: member
 */
@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long memberNo;                  // 회원 번호 (PK, 자동 증가)

    @Column(name = "id", unique = true, nullable = false, length = 30)
    private String id;                      // 로그인 ID (중복 불가)

    @Column(name = "pwd", nullable = false)
    private String pwd;                     // 비밀번호 (암호화 저장)

    @Column(name = "name", nullable = false, length = 20)
    private String name;                    // 이름

    @Column(name = "email", unique = true, length = 50)
    private String email;                   // 이메일

    @Column(name = "role", nullable = false)
    private String role;                    // 권한: ROLE_USER, ROLE_ADMIN

    @CreationTimestamp
    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate;          // 가입일 (자동 입력)

    // =========================================================
    // TODO: 필요한 필드 추가 (예: 프로필 사진, 전화번호 등)
    // =========================================================
}
