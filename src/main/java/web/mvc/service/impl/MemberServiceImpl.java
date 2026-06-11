package web.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Member;
import web.mvc.dto.member.MemberReqDto;
import web.mvc.dto.member.MemberResDto;
import web.mvc.exception.MemberException;
import web.mvc.repository.BoardRepository;
import web.mvc.repository.DateRequestRepository;
import web.mvc.repository.MemberRepository;
import web.mvc.service.MemberService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardRepository boardRepository;
    private final DateRequestRepository dateRequestRepository;

    @Override
    public MemberResDto signUp(MemberReqDto dto) {
        if(memberRepository.existsById(dto.getId())) {
            throw new MemberException("이미 사용중인 아이디입니다.");
        }

        Member member = dto.toMember();

        // 비번 암호화
        member.setPwd(passwordEncoder.encode(dto.getPwd()));

        // ROLE 설정
        member.setRole("ROLE_USER");

        Member saved =  memberRepository.save(member);

        return new MemberResDto(saved);
    }

    @Override
    public boolean checkDuplicateId(String id) {

        boolean exists = memberRepository.existsById(id);

        return !exists;
    }

    @Override
    public MemberResDto getMyInfo(Long memberNo) {

        Member member = memberRepository.findById(memberNo).orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다."));

        return new MemberResDto(member);
    }

    @Override
    public MemberResDto updateMyInfo(Long memberNo, MemberReqDto dto) {
        Member member = memberRepository.findById(memberNo).orElseThrow(() -> new MemberException("수정할 정보가 없음"));
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setProfileImg(dto.getProfileImg());
        member.setGender(dto.getGender());
        member.setAge(dto.getAge());
        member.setIntroduce(dto.getIntroduce());

        return new MemberResDto(member);
    }

    @Override
    public void deleteMyAccount(Long memberNo) {
        memberRepository.findById(memberNo)
                .orElseThrow(() -> new MemberException("삭제할 계정이 없음"));

        dateRequestRepository.deleteBySenderMemberNo(memberNo);
        dateRequestRepository.deleteByBoardMemberMemberNo(memberNo);
        boardRepository.deleteByMemberMemberNo(memberNo);
        memberRepository.deleteById(memberNo);
    }

    @Override
    public List<MemberResDto> getAllMembers() {

        return memberRepository.findAll().stream().map(m -> new MemberResDto(m)).toList();
    }

    @Override
    public void deleteMemberByAdmin(Long memberNo) {
        memberRepository.findById(memberNo)
                .orElseThrow(() -> new MemberException("삭제할 계정이 없음"));

        // FK 제약 순서: 데이트신청 → 게시글 → 회원
        dateRequestRepository.deleteBySenderMemberNo(memberNo);
        dateRequestRepository.deleteByBoardMemberMemberNo(memberNo);
        boardRepository.deleteByMemberMemberNo(memberNo);
        memberRepository.deleteById(memberNo);
    }
}
