package web.mvc.service;

import web.mvc.dto.member.MemberReqDto;
import web.mvc.dto.member.MemberResDto;

import java.util.List;

public interface MemberService {

    MemberResDto signUp(MemberReqDto dto);

    boolean checkDuplicateId(String id);

    MemberResDto getMyInfo(Long memberNo);

    MemberResDto updateMyInfo(Long memberNo, MemberReqDto dto);

    void deleteMyAccount(Long memberNo);

    List<MemberResDto> getAllMembers();

    void deleteMemberByAdmin(Long memberNo);
}
