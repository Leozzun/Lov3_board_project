package web.mvc.jwt;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import web.mvc.domain.Member;
import web.mvc.security.CustomMemberDetails;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 로그인 처리 필터
 * POST /login 요청을 가로채서 아이디/비밀번호 인증 처리
 *
 * 동작 흐름:
 * 1. POST /login 요청 수신 (Body: username, password)
 * 2. CustomMemberDetailsService.loadUserByUsername() 호출 → DB에서 회원 조회
 * 3. 비밀번호 일치 확인
 * 4. 성공 시 JWT 토큰 발급 → Authorization 헤더에 담아 응답
 * 5. 실패 시 401 Unauthorized 응답
 */
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    // JWT 만료 시간: 1시간 (밀리초)
    private static final Long JWT_EXPIRE_MS = 1000L * 60 * 60;

    /**
     * 로그인 시도 (아이디/비밀번호 추출 및 인증 요청)
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {

        // 요청 Body에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // AuthenticationManager에게 인증 위임
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    /**
     * 로그인 성공 시 JWT 발급
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication)
            throws IOException {

        CustomMemberDetails memberDetails = (CustomMemberDetails) authentication.getPrincipal();
        Member member = memberDetails.getMember();
        String role = memberDetails.getAuthorities().iterator().next().getAuthority();

        // JWT 토큰 생성
        String token = jwtUtil.createJwt(member, role, JWT_EXPIRE_MS);

        // 응답 헤더에 토큰 추가
        response.addHeader("Authorization", "Bearer " + token);

        // 응답 Body에 회원 정보 추가
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> result = new HashMap<>();
        result.put("memberNo", member.getMemberNo());
        result.put("id", member.getId());
        result.put("name", member.getName());
        result.put("role", role);
        result.put("message", "로그인 성공");

        response.getWriter().write(new Gson().toJson(result));
    }

    /**
     * 로그인 실패 시 401 응답
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, String> result = new HashMap<>();
        result.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
        result.put("error", "UNAUTHORIZED");

        response.getWriter().write(new Gson().toJson(result));
    }
}
