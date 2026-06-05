package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.member.MemberResDto;
import web.mvc.service.MemberService;

import java.util.List;

/**
 * 관리자 전용 컨트롤러
 * 기본 URL: /admin
 * SecurityConfig에서 ROLE_ADMIN만 접근 허용으로 설정
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    /**
     * [GET] /admin/members
     * 전체 회원 목록 조회
     * 인증 필요 (ROLE_ADMIN만)
     *
     * @return 200 OK + 전체 회원 목록
     */
    @GetMapping("/members")
    public ResponseEntity<List<MemberResDto>> getAllMembers() {
        // TODO: 구현
        // return ResponseEntity.ok(memberService.getAllMembers());
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [DELETE] /admin/members/{memberNo}
     * 특정 회원 강제 탈퇴
     * 인증 필요 (ROLE_ADMIN만)
     *
     * @param memberNo 탈퇴시킬 회원 번호 (Path Variable)
     * @return 204 No Content
     */
    @DeleteMapping("/members/{memberNo}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberNo) {
        // TODO: 구현
        // memberService.deleteMemberByAdmin(memberNo);
        // return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
