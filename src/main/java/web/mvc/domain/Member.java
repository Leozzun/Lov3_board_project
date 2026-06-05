package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter @Setter @NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long memberNo;

    @Column(name = "id", unique = true, nullable = false, length = 30)
    private String id;

    @Column(name = "pwd", nullable = false)
    private String pwd;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "email", unique = true, length = 50)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;                    // ROLE_USER / ROLE_ADMIN

    // ── 프로필 (기본 공개) ──────────────────────────
    @Column(name = "profile_img")
    private String profileImg;              // 프로필 사진 URL

    // ── 프로필 (매칭 후 공개) ───────────────────────
    @Column(name = "gender", length = 10)
    private String gender;                  // MALE / FEMALE / OTHER

    @Column(name = "age")
    private Integer age;

    @Column(name = "introduce", length = 300)
    private String introduce;              // 자기소개

    @CreationTimestamp
    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate;
}
