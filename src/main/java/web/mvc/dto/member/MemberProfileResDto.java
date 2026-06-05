package web.mvc.dto.member;

import lombok.Getter;
import web.mvc.domain.Member;

import java.time.LocalDateTime;

// 매칭 성공 후 공개되는 상세 프로필
@Getter
public class MemberProfileResDto {

    private Long memberNo;
    private String id;
    private String name;
    private String email;
    private String profileImg;
    private String gender;
    private Integer age;
    private String introduce;
    private LocalDateTime regDate;

    public MemberProfileResDto(Member member) {
        this.memberNo = member.getMemberNo();
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.profileImg = member.getProfileImg();
        this.gender = member.getGender();
        this.age = member.getAge();
        this.introduce = member.getIntroduce();
        this.regDate = member.getRegDate();
    }
}
