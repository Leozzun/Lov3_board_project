package web.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Member;
import web.mvc.dto.member.MemberReqDto;
import web.mvc.dto.member.MemberResDto;
import web.mvc.exception.MemberException;
import web.mvc.repository.MemberRepository;
import web.mvc.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 회원 서비스 구현체
 * 실제 비즈니스 로직을 작성하는 곳
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * 1. ID 중복 확인
     * 2. 비밀번호 암호화
     * 3. 권한(ROLE_USER) 설정
     * 4. DB 저장
     */
    @Override
    public MemberResDto signUp(MemberReqDto dto) {
        // TODO: 회원가입 로직 구현
        // 예시:
        // if (memberRepository.existsById(dto.getId())) {
        //     throw new MemberException("이미 사용 중인 ID입니다.");
        // }
        // Member member = dto.toMember();
        // member.setPwd(passwordEncoder.encode(dto.getPwd()));
        // member.setRole("ROLE_USER");
        // return new MemberResDto(memberRepository.save(member));
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * ID 중복 확인
     * true: 사용 가능 / false: 이미 사용 중
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkDuplicateId(String id) {
        // TODO: 구현
        // return !memberRepository.existsById(id);
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 내 정보 조회
     */
    @Override
    @Transactional(readOnly = true)
    public MemberResDto getMyInfo(Long memberNo) {
        // TODO: 구현
        // Member member = memberRepository.findById(memberNo)
        //     .orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다."));
        // return new MemberResDto(member);
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 내 정보 수정
     */
    @Override
    public MemberResDto updateMyInfo(Long memberNo, MemberReqDto dto) {
        // TODO: 구현
        // Member member = memberRepository.findById(memberNo)
        //     .orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다."));
        // member.setName(dto.getName());
        // member.setEmail(dto.getEmail());
        // if (dto.getPwd() != null && !dto.getPwd().isEmpty()) {
        //     member.setPwd(passwordEncoder.encode(dto.getPwd()));
        // }
        // return new MemberResDto(member); // @Transactional이면 save() 없이도 변경 감지
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 회원 탈퇴
     */
    @Override
    public void deleteMyAccount(Long memberNo) {
        // TODO: 구현
        // Member member = memberRepository.findById(memberNo)
        //     .orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다."));
        // memberRepository.delete(member);
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 전체 회원 목록 (관리자용)
     */
    @Override
    @Transactional(readOnly = true)
    public List<MemberResDto> getAllMembers() {
        // TODO: 구현
        // return memberRepository.findAll().stream()
        //     .map(MemberResDto::new)
        //     .collect(Collectors.toList());
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 회원 강제 탈퇴 (관리자용)
     */
    @Override
    public void deleteMemberByAdmin(Long memberNo) {
        // TODO: 구현
        // memberRepository.deleteById(memberNo);
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }
}
