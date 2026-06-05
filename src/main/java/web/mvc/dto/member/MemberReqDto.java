package web.mvc.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.mvc.domain.Member;

/**
 * 회원가입 요청 DTO (클라이언트 → 서버)
 * POST /members 요청 시 Body에 담아 전달
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberReqDto {

    private String id;          // 로그인 ID
    private String pwd;         // 비밀번호 (평문, 서버에서 암호화)
    private String name;        // 이름
    private String email;       // 이메일

    /**
     * DTO → Entity 변환 메서드
     * 서비스에서 회원을 저장할 때 사용
     */
    public Member toMember() {
        Member member = new Member();
        member.setId(this.id);
        member.setPwd(this.pwd);       // 서비스에서 암호화 후 저장
        member.setName(this.name);
        member.setEmail(this.email);
        // TODO: 필요한 필드 추가 매핑
        return member;
    }
}
