package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.service.MemberService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers() {

        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @DeleteMapping("/members/{memberNo}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberNo) {

        memberService.deleteMemberByAdmin(memberNo);

        return ResponseEntity.noContent().build();
    }
}
