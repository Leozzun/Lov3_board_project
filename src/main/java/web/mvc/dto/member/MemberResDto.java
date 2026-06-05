package web.mvc.dto.member;

import lombok.Getter;
import web.mvc.domain.Member;

// 기본 공개 프로필 (매칭 전 - 아이디, 사진만 노출)
@Getter
public class MemberResDto {

    private Long memberNo;
    private String id;
    private String profileImg;

    public MemberResDto(Member member) {
        this.memberNo = member.getMemberNo();
        this.id = member.getId();
        this.profileImg = member.getProfileImg();
    }
}
