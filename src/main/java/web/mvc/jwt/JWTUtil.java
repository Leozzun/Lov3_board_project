package web.mvc.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import web.mvc.domain.Member;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 토큰 생성 및 파싱 유틸리티
 * - 토큰 생성: 로그인 성공 시 LoginFilter에서 호출
 * - 토큰 파싱: 요청마다 JWTFilter에서 호출
 */
@Component
public class JWTUtil {

    private final SecretKey secretKey;

    // application.properties의 spring.jwt.secret 값을 주입
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    // 토큰에서 memberNo 추출
    public Long getMemberNo(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberNo", Long.class);
    }

    // 토큰에서 로그인 ID 추출
    public String getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", String.class);
    }

    // 토큰에서 이름 추출
    public String getName(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // 토큰에서 권한 추출
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // 토큰 만료 여부 확인 (true: 만료됨)
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    /**
     * JWT 토큰 생성
     *
     * @param member    회원 엔티티
     * @param role      권한 (ROLE_USER, ROLE_ADMIN)
     * @param expiredMs 만료 시간 (밀리초) 예: 1000L * 60 * 60 = 1시간
     */
    public String createJwt(Member member, String role, Long expiredMs) {
        return Jwts.builder()
                .subject(member.getName())                              // 이름
                .claim("memberNo", member.getMemberNo())                // 회원번호
                .claim("id", member.getId())                            // 로그인 ID
                .claim("role", role)                                     // 권한
                .issuedAt(new Date(System.currentTimeMillis()))          // 발급 시각
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시각
                .signWith(secretKey)
                .compact();
    }
}
