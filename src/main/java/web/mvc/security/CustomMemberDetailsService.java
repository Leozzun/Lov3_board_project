package web.mvc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.mvc.domain.Member;
import web.mvc.repository.MemberRepository;

/**
 * 로그인 시 DB에서 회원 정보를 조회하는 서비스
 * Spring Security의 AuthenticationManager가 자동으로 호출
 *
 * 흐름: POST /login → LoginFilter → AuthenticationManager → 이 클래스 → MemberRepository
 */
@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 로그인 ID로 회원 조회
     * 없으면 UsernameNotFoundException 발생 → Spring Security가 인증 실패 처리
     */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다: " + id));
        return new CustomMemberDetails(member);
    }
}
