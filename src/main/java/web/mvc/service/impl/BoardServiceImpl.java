package web.mvc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.mvc.domain.Board;
import web.mvc.domain.Member;
import web.mvc.dto.board.BoardReqDto;
import web.mvc.dto.board.BoardResDto;
import web.mvc.exception.BoardException;
import web.mvc.exception.MemberException;
import web.mvc.repository.BoardRepository;
import web.mvc.repository.MemberRepository;
import web.mvc.service.BoardService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 서비스 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 전체 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<BoardResDto> getAllBoards() {
        // TODO: 구현
        // return boardRepository.findAllWithMember().stream()
        //     .map(BoardResDto::new)
        //     .collect(Collectors.toList());
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 게시글 단건 조회 (조회수 +1)
     */
    @Override
    public BoardResDto getBoard(Long boardId) {
        // TODO: 구현
        // Board board = boardRepository.findById(boardId)
        //     .orElseThrow(() -> new BoardException("게시글을 찾을 수 없습니다."));
        // board.setViewCount(board.getViewCount() + 1);  // 조회수 증가
        // return new BoardResDto(board);
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 게시글 검색 (제목/내용 키워드)
     */
    @Override
    @Transactional(readOnly = true)
    public List<BoardResDto> searchBoards(String keyword) {
        // TODO: 구현
        // return boardRepository.searchByKeyword(keyword).stream()
        //     .map(BoardResDto::new)
        //     .collect(Collectors.toList());
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 게시글 등록
     */
    @Override
    public BoardResDto addBoard(BoardReqDto dto, Long memberNo) {
        // TODO: 구현
        // Member member = memberRepository.findById(memberNo)
        //     .orElseThrow(() -> new MemberException("회원을 찾을 수 없습니다."));
        // Board board = new Board();
        // board.setTitle(dto.getTitle());
        // board.setContent(dto.getContent());
        // board.setMember(member);
        // return new BoardResDto(boardRepository.save(board));
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 게시글 수정 (본인만 가능)
     */
    @Override
    public BoardResDto updateBoard(Long boardId, BoardReqDto dto, Long memberNo) {
        // TODO: 구현
        // Board board = boardRepository.findById(boardId)
        //     .orElseThrow(() -> new BoardException("게시글을 찾을 수 없습니다."));
        // if (!board.getMember().getMemberNo().equals(memberNo)) {
        //     throw new BoardException("본인의 게시글만 수정할 수 있습니다.");
        // }
        // board.setTitle(dto.getTitle());
        // board.setContent(dto.getContent());
        // return new BoardResDto(board);
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }

    /**
     * 게시글 삭제 (본인 또는 관리자)
     */
    @Override
    public void deleteBoard(Long boardId, Long memberNo, String role) {
        // TODO: 구현
        // Board board = boardRepository.findById(boardId)
        //     .orElseThrow(() -> new BoardException("게시글을 찾을 수 없습니다."));
        // boolean isOwner = board.getMember().getMemberNo().equals(memberNo);
        // boolean isAdmin = "ROLE_ADMIN".equals(role);
        // if (!isOwner && !isAdmin) {
        //     throw new BoardException("삭제 권한이 없습니다.");
        // }
        // boardRepository.delete(board);
        throw new UnsupportedOperationException("TODO: 구현 필요");
    }
}
