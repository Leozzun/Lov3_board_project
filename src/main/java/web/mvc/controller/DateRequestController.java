package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.dateRequest.DateRequestReqDto;
import web.mvc.dto.dateRequest.DateRequestResDto;
import web.mvc.security.CustomMemberDetails;
import web.mvc.service.DateRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DateRequestController {

    private final DateRequestService dateRequestService;

    @PostMapping("/boards/{boardId}/date-requests")
    public ResponseEntity<DateRequestResDto> sendRequest(@PathVariable Long boardId, @RequestBody DateRequestReqDto dto, @AuthenticationPrincipal CustomMemberDetails memberDetails) {

        DateRequestResDto result = dateRequestService.sendRequest(dto, boardId, memberDetails.getMember().getMemberNo());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/boards/{boardId}/date-requests")
    public ResponseEntity<List<DateRequestResDto>> getReceivedRequestsByBoard(@PathVariable Long boardId, @AuthenticationPrincipal CustomMemberDetails memberDetails) {

        List<DateRequestResDto> result = dateRequestService.getReceivedRequestsByBoard(boardId, memberDetails.getMember().getMemberNo());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/date-requests/received")
    public ResponseEntity<List<DateRequestResDto>> getAllReceivedRequests(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

        List<DateRequestResDto>  result = dateRequestService.getAllReceivedRequests(memberDetails.getMember().getMemberNo());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/date-requests/sent")
    public ResponseEntity<List<DateRequestResDto>> getSentRequests(@AuthenticationPrincipal CustomMemberDetails memberDetails) {

        List<DateRequestResDto> result = dateRequestService.getSentRequests(memberDetails.getMember().getMemberNo());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/date-requests/{requestId}/accept")
    public ResponseEntity<DateRequestResDto> acceptRequest(@AuthenticationPrincipal CustomMemberDetails memberDetails, @PathVariable Long requestId) {

        DateRequestResDto result = dateRequestService.acceptRequest(memberDetails.getMember().getMemberNo(), requestId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/date-requests/{requestId}/reject")
    public ResponseEntity<DateRequestResDto> rejectRequest(@AuthenticationPrincipal CustomMemberDetails memberDetails, @PathVariable Long requestId) {

        DateRequestResDto result = dateRequestService.rejectRequest(memberDetails.getMember().getMemberNo(), requestId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/date-requests/{requestId}")
    public ResponseEntity<Void> cancelRequest(@AuthenticationPrincipal CustomMemberDetails memberDetails, @PathVariable Long requestId) {

        dateRequestService.cancelRequest(requestId, memberDetails.getMember().getMemberNo());

        return ResponseEntity.noContent().build();
    }
}
