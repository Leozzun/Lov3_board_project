package web.mvc.service;

import web.mvc.dto.member.MemberReqDto;
import web.mvc.dto.member.MemberResDto;

import java.util.List;

/**
 * 회원 서비스 인터페이스
 * 실제 구현은 MemberServiceImpl에서 작성
 */
public interface MemberService {

    // 회원가입
    MemberResDto signUp(MemberReqDto dto);

    // ID 중복 확인 (true: 사용 가능, false: 중복)
    boolean checkDuplicateId(String id);

    // 내 정보 조회
    MemberResDto getMyInfo(Long memberNo);

    // 내 정보 수정
    MemberResDto updateMyInfo(Long memberNo, MemberReqDto dto);

    // 회원 탈퇴
    void deleteMyAccount(Long memberNo);

    // 전체 회원 목록 (관리자용)
    List<MemberResDto> getAllMembers();

    // 회원 강제 탈퇴 (관리자용)
    void deleteMemberByAdmin(Long memberNo);
}
