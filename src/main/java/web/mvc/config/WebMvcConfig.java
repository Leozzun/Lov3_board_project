package web.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS(Cross-Origin Resource Sharing) 설정
 * 프론트엔드(React, Vue 등)에서 이 서버로 API 요청이 가능하도록 허용
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // TODO: 운영환경에서는 실제 프론트엔드 URL로 변경
                // 예: .allowedOrigins("https://your-frontend.com")
                .allowedOrigins(
                        "http://localhost:3000",     // React 개발 서버
                        "http://localhost:5173",     // Vite 개발 서버
                        "http://localhost:8080"      // 기타
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")    // JWT 토큰 헤더를 클라이언트에 노출
                .allowCredentials(true)
                .maxAge(3600);
    }
}
