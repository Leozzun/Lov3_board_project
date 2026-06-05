package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

// 데이트 신청 엔티티
// status 값: PENDING(대기중) / ACCEPTED(수락=매칭성공) / REJECTED(거절)
@Entity
@Table(name = "date_request")
@Getter @Setter @NoArgsConstructor
public class DateRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "message", nullable = false, length = 500)
    private String message;                // 신청 멘트

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";     // PENDING / ACCEPTED / REJECTED

    // 신청 대상 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // 신청을 보낸 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_no", nullable = false)
    private Member sender;

    @CreationTimestamp
    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
