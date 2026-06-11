package web.mvc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import web.mvc.jwt.JWTFilter;
import web.mvc.jwt.JWTUtil;
import web.mvc.jwt.LoginFilter;

/**
 * Spring Security 설정
 * - 어떤 URL에 누가 접근 가능한지 설정
 * - JWT 필터 등록
 * - 비밀번호 암호화 Bean 등록
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    // BCrypt 비밀번호 암호화 Bean 등록 (MemberServiceImpl에서 주입받아 사용)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager Bean 등록 (LoginFilter에서 사용)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정 (WebMvcConfig의 addCorsMappings를 참조)
        http.cors(Customizer.withDefaults());

        // CSRF 비활성화 (REST API + JWT 방식에서는 불필요)
        http.csrf(csrf -> csrf.disable());

        // 폼 로그인, 기본 로그인 비활성화 (JWT 사용)
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());

        // ===== URL별 접근 권한 설정 =====
        http.authorizeHttpRequests(auth -> auth
                // 회원가입, ID중복확인 - 누구나 가능
                .requestMatchers(HttpMethod.POST, "/members").permitAll()
                .requestMatchers(HttpMethod.GET, "/members/check").permitAll()

                // 게시글 조회, 검색 - 누구나 가능
                .requestMatchers(HttpMethod.GET, "/boards").permitAll()
                .requestMatchers(HttpMethod.GET, "/boards/**").permitAll()

                // 장소 이미지 - 누구나 가능
                .requestMatchers("/images/**").permitAll()

                // 장소 조회 - 누구나 가능
                .requestMatchers(HttpMethod.GET, "/places").permitAll()
                .requestMatchers(HttpMethod.GET, "/places/**").permitAll()

                // 장소 관리 (추가/수정/삭제) - ADMIN만 가능
                .requestMatchers(HttpMethod.POST, "/places").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/places/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/places/**").hasRole("ADMIN")

                // 관리자 기능 - ADMIN만 가능
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // 나머지 모든 요청 - 로그인 필요
                .anyRequest().authenticated()
        );

        // 세션 사용 안 함 (JWT는 Stateless)
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // LoginFilter: POST /login 처리 (JWT 발급)
        LoginFilter loginFilter = new LoginFilter(
                authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/login");

        // 필터 순서: JWTFilter → LoginFilter
        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
