package web.mvc.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 프로필 수정 요청 DTO
@Getter @Setter @NoArgsConstructor
public class MemberProfileReqDto {

    private String profileImg;
    private String gender;      // MALE / FEMALE / OTHER
    private Integer age;
    private String introduce;
}
