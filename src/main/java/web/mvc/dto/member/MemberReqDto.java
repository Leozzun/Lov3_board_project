package web.mvc.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import web.mvc.domain.Member;

// 회원가입 / 정보수정 / 프로필 수정 요청 DTO
@Getter @Setter @NoArgsConstructor
public class MemberReqDto {

    private String id;
    private String pwd;
    private String name;
    private String email;
    private String profileImg;
    private String gender;      // MALE / FEMALE / OTHER
    private Integer age;
    private String introduce;

    public Member toMember() {
        Member member = new Member();
        member.setId(this.id);
        member.setPwd(this.pwd);
        member.setName(this.name);
        member.setEmail(this.email);
        return member;
    }
}
