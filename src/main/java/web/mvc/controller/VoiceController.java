package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.service.LiveKitService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/voice")
@RequiredArgsConstructor
public class VoiceController {

    private final LiveKitService liveKitService;

    @Value("${livekit.url}")
    private String livekitUrl;

    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> getToken(
            @RequestParam String roomName,
            @RequestParam String userName) {

        String token = liveKitService.generateToken(roomName, userName);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("url", livekitUrl);

        return ResponseEntity.ok(response);
    }
}
