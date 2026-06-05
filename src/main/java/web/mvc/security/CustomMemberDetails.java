package web.mvc.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import web.mvc.domain.Member;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security가 사용하는 회원 정보 래퍼 클래스
 * UserDetails 인터페이스를 구현하여 Spring Security와 연동
 */
@Getter
@RequiredArgsConstructor
public class CustomMemberDetails implements UserDetails {

    private final Member member;

    // 권한 목록 반환 (ROLE_USER, ROLE_ADMIN 등)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(member.getRole()));
    }

    @Override
    public String getPassword() {
        return member.getPwd();
    }

    // Spring Security가 사용하는 사용자 식별자 (여기선 로그인 ID 사용)
    @Override
    public String getUsername() {
        return member.getId();
    }

    // 계정 만료 여부 (false: 만료됨, true: 유효)
    @Override
    public boolean isAccountNonExpired() { return true; }

    // 계정 잠금 여부 (false: 잠김, true: 정상)
    @Override
    public boolean isAccountNonLocked() { return true; }

    // 자격증명 만료 여부 (false: 만료됨, true: 유효)
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    // 계정 활성화 여부 (false: 비활성, true: 활성)
    @Override
    public boolean isEnabled() { return true; }
}
