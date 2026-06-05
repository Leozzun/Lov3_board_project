package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.member.MemberReqDto;
import web.mvc.dto.member.MemberResDto;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.MemberService;

/**
 * 회원 컨트롤러
 * 기본 URL: /members
 */
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * [POST] /members
     * 회원가입
     * 인증 불필요 (누구나 가입 가능)
     *
     * @param dto 회원가입 정보 (Body: JSON)
     * @return 201 Created + 가입된 회원 정보
     */
    @PostMapping
    public ResponseEntity<MemberResDto> signUp(@RequestBody MemberReqDto dto) {
        // TODO: 구현
        // MemberResDto result = memberService.signUp(dto);
        // return ResponseEntity.status(HttpStatus.CREATED).body(result);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [GET] /members/check?id={id}
     * ID 중복 확인
     * 인증 불필요
     *
     * @param id 확인할 로그인 ID (Query Parameter)
     * @return 200 OK + true(사용가능) / false(중복)
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkDuplicateId(@RequestParam String id) {
        // TODO: 구현
        // return ResponseEntity.ok(memberService.checkDuplicateId(id));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [GET] /members/me
     * 내 정보 조회
     * 인증 필요 (JWT 토큰)
     *
     * @param memberDetails JWT에서 추출된 로그인 회원 정보
     * @return 200 OK + 내 정보
     */
    @GetMapping("/me")
    public ResponseEntity<MemberResDto> getMyInfo(
            @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        // TODO: 구현
        // Long memberNo = memberDetails.getMember().getMemberNo();
        // return ResponseEntity.ok(memberService.getMyInfo(memberNo));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [PUT] /members/me
     * 내 정보 수정
     * 인증 필요
     *
     * @param memberDetails 로그인 회원 정보
     * @param dto           수정할 정보 (Body: JSON)
     * @return 200 OK + 수정된 정보
     */
    @PutMapping("/me")
    public ResponseEntity<MemberResDto> updateMyInfo(
            @AuthenticationPrincipal CustomMemberDetails memberDetails,
            @RequestBody MemberReqDto dto) {
        // TODO: 구현
        // Long memberNo = memberDetails.getMember().getMemberNo();
        // return ResponseEntity.ok(memberService.updateMyInfo(memberNo, dto));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * [DELETE] /members/me
     * 회원 탈퇴
     * 인증 필요
     *
     * @param memberDetails 로그인 회원 정보
     * @return 204 No Content
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(
            @AuthenticationPrincipal CustomMemberDetails memberDetails) {
        // TODO: 구현
        // Long memberNo = memberDetails.getMember().getMemberNo();
        // memberService.deleteMyAccount(memberNo);
        // return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
