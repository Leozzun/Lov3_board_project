package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.member.MemberResDto;
import web.mvc.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<List<MemberResDto>> getAllMembers() {

        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @DeleteMapping("/members/{memberNo}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberNo) {

        memberService.deleteMemberByAdmin(memberNo);

        return ResponseEntity.noContent().build();
    }
}
