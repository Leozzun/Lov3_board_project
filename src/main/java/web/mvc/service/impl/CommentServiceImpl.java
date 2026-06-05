package web.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Board;
import web.mvc.domain.Comment;
import web.mvc.domain.Member;
import web.mvc.dto.comment.CommentReqDto;
import web.mvc.dto.comment.CommentResDto;
import web.mvc.exception.BoardException;
import web.mvc.exception.CommentException;
import web.mvc.exception.MemberException;
import web.mvc.repository.BoardRepository;
import web.mvc.repository.CommentRepository;
import web.mvc.repository.MemberRepository;
import web.mvc.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 댓글 서비스 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 특정 게시글의 댓글 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommentResDto> getCommentsByBoard(Long boardId) {
        // TODO: 구현
        // return commentRepository.findAllWithMemberByBoardId(boardId).stream()
        //     .map(CommentResDto::new)
        //     .collect(Collectors.toList());
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 댓글 등록
     */
    @Override
    public CommentResDto addComment(Long boardId, CommentReqDto dto, Long memberNo) {
        // TODO: 구현
        // Board board = boardRepository.findById(boardId)
        //     .orElseThrow(() -> new BoardException("게시글을 찾을 수 없습니다."));
        // Member member = memberRepository.findById(memberNo)
        //     .orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다."));
        // Comment comment = new Comment();
        // comment.setContent(dto.getContent());
        // comment.setBoard(board);
        // comment.setMember(member);
        // return new CommentResDto(commentRepository.save(comment));
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 댓글 수정 (본인만 가능)
     */
    @Override
    public CommentResDto updateComment(Long commentId, CommentReqDto dto, Long memberNo) {
        // TODO: 구현
        // Comment comment = commentRepository.findById(commentId)
        //     .orElseThrow(() -> new CommentException("댓글을 찾을 수 없습니다."));
        // if (!comment.getMember().getMemberNo().equals(memberNo)) {
        //     throw new CommentException("본인의 댓글만 수정할 수 있습니다.");
        // }
        // comment.setContent(dto.getContent());
        // return new CommentResDto(comment);
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 댓글 삭제 (본인 또는 관리자)
     */
    @Override
    public void deleteComment(Long commentId, Long memberNo, String role) {
        // TODO: 구현
        // Comment comment = commentRepository.findById(commentId)
        //     .orElseThrow(() -> new CommentException("댓글을 찾을 수 없습니다."));
        // boolean isOwner = comment.getMember().getMemberNo().equals(memberNo);
        // boolean isAdmin = "ROLE_ADMIN".equals(role);
        // if (!isOwner && !isAdmin) {
        //     throw new CommentException("삭제 권한이 없습니다.");
        // }
        // commentRepository.delete(comment);
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }
}
