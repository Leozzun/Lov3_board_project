package web.mvc.dto.member;

import lombok.Getter;
import web.mvc.domain.Member;

import java.time.LocalDateTime;

/**
 * 회원 응답 DTO (서버 → 클라이언트)
 * 비밀번호 같은 민감한 정보는 절대 포함하지 않음
 */
@Getter
public class MemberResDto {

    private Long memberNo;          // 회원 번호
    private String id;              // 로그인 ID
    private String name;            // 이름
    private String email;           // 이메일
    private String role;            // 권한
    private LocalDateTime regDate;  // 가입일

    /**
     * Entity → DTO 변환 생성자
     */
    public MemberResDto(Member member) {
        this.memberNo = member.getMemberNo();
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.role = member.getRole();
        this.regDate = member.getRegDate();
        // TODO: 필요한 필드 추가
    }
}
