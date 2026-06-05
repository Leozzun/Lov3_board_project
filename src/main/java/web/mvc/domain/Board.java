package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 엔티티
 * DB 테이블명: board
 */
@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;                   // 게시글 번호 (PK, 자동 증가)

    @Column(name = "title", nullable = false, length = 100)
    private String title;                   // 제목

    @Column(name = "content", nullable = false, length = 2000)
    private String content;                 // 내용

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;              // 조회수 (기본값 0)

    // 회원(Member)과 다대일(N:1) 관계 - 하나의 회원이 여러 게시글 작성 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no", nullable = false)
    private Member member;                  // 작성자

    // 댓글(Comment)과 일대다(1:N) 관계
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate;          // 작성일 (자동 입력)

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDateTime updateDate;       // 수정일 (자동 업데이트)

    // =========================================================
    // TODO: 필요한 필드 추가 (예: 카테고리, 파일 첨부 등)
    // =========================================================
}
