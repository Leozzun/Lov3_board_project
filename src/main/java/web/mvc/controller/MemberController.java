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

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody MemberReqDto dto) {

        MemberResDto result =  memberService.signUp(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkDuplicateId(@RequestParam String id) {
        boolean result =  memberService.checkDuplicateId(id);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

        MemberResDto result =  memberService.getMyInfo(memberDetails.getMember().getMemberNo());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/my")
    public ResponseEntity<?> updateMyInfo(@AuthenticationPrincipal CustomMemberDetails memberDetails, @RequestBody MemberReqDto dto) {

        MemberResDto result =  memberService.updateMyInfo(memberDetails.getMember().getMemberNo(), dto);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/my")
    public ResponseEntity<?> deleteMyAccount(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

        memberService.deleteMyAccount(memberDetails.getMember().getMemberNo());

        return ResponseEntity.noContent().build();
    }

}
