package web.mvc.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import web.mvc.domain.Member;
import web.mvc.security.CustomMemberDetails;

import java.io.IOException;

/**
 * JWT 검증 필터
 * 모든 요청마다 한 번 실행되어 Authorization 헤더의 JWT 토큰을 검증
 *
 * 동작 흐름:
 * 1. 요청 헤더에서 "Authorization: Bearer {token}" 추출
 * 2. 토큰 유효성 검사 (만료 여부)
 * 3. 토큰에서 회원 정보 추출 후 SecurityContext에 저장
 * 4. 이후 컨트롤러에서 @AuthenticationPrincipal로 회원 정보 사용 가능
 */
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization 헤더 추출
        String authorization = request.getHeader("Authorization");

        // 헤더가 없거나 "Bearer "로 시작하지 않으면 다음 필터로 통과
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. "Bearer " 제거 → 순수 토큰 추출
        String token = authorization.split(" ")[1];

        // 3. 토큰 만료 확인
        if (jwtUtil.isExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 4. 토큰에서 회원 정보 추출
        Member member = new Member();
        member.setMemberNo(jwtUtil.getMemberNo(token));
        member.setId(jwtUtil.getId(token));
        member.setName(jwtUtil.getName(token));
        member.setRole(jwtUtil.getRole(token));

        // 5. SecurityContext에 인증 정보 저장
        CustomMemberDetails memberDetails = new CustomMemberDetails(member);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 6. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
