package web.mvc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드된 이미지를 파일시스템에서 직접 서빙 (캐시 없음)
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().toString();
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + absolutePath + "/")
                .setCacheControl(CacheControl.noCache());
    }

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
