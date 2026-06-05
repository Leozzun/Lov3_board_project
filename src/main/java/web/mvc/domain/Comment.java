package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 댓글 엔티티
 * DB 테이블명: comment
 */
@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;                 // 댓글 번호 (PK, 자동 증가)

    @Column(name = "content", nullable = false, length = 500)
    private String content;                 // 댓글 내용

    // 게시글(Board)과 다대일(N:1) 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;                    // 어느 게시글의 댓글인지

    // 회원(Member)과 다대일(N:1) 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;                  // 댓글 작성자

    @CreationTimestamp
    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate;          // 작성일 (자동 입력)

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;       // 수정일 (자동 업데이트)

    // =========================================================
    // TODO: 필요한 필드 추가 (예: 대댓글을 위한 parent_id 등)
    // =========================================================
}
