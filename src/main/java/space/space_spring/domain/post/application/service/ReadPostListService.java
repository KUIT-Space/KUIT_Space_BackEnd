package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.PostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.ReadPostListUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.application.port.out.post.PostListQueryPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TAG_IS_NOT_REQUIRED_FOR_THIS_BOARD;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TAG_IS_REQUIRED_FOR_THIS_BOARD;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReadPostListService implements ReadPostListUseCase {

    private final LoadBoardPort loadBoardPort;
    private final PostListQueryPort postListQueryPort;

    @Override
    public ListOfPostSummary readPostList(Long spaceMemberId, Long boardId, Long tagId, Pageable pageable) {
        // 1. Board 조회
        Board board = loadBoardPort.loadById(boardId);

        // 2. 태그 검증
        if ((board.getBoardType() == BoardType.QUESTION || board.getBoardType() == BoardType.TIP) && tagId == null) {
            throw new CustomException(TAG_IS_REQUIRED_FOR_THIS_BOARD);
        }
        if ((board.getBoardType() == BoardType.POST || board.getBoardType() == BoardType.PAY || board.getBoardType() == BoardType.NOTICE || board.getBoardType() == BoardType.SEASON_NOTICE) && tagId != null) {
            throw new CustomException(TAG_IS_NOT_REQUIRED_FOR_THIS_BOARD);
        }

        // 3. 태그 필터링
        Page<PostSummary> postSummaries = postListQueryPort.queryPostSummaries(spaceMemberId, boardId, tagId,  pageable);

        // 4. ListOfPostSummary 생성
        return ListOfPostSummary.of(postSummaries, postSummaries.getContent());
    }
}
