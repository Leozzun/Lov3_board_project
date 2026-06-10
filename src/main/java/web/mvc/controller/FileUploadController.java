package web.mvc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

@RestController
public class FileUploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 관리자 전용 (장소 사진)
    @PostMapping("/admin/upload/image")
    public ResponseEntity<Map<String, String>> adminUploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return upload(file);
    }

    // 로그인한 유저 전용 (프로필 사진 등)
    @PostMapping("/upload/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        return upload(file);
    }

    private ResponseEntity<Map<String, String>> upload(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.jpg";
        String filename = System.currentTimeMillis() + "_" + original;

        Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        return ResponseEntity.ok(Map.of("imageUrl", "/images/" + filename));
    }
}
