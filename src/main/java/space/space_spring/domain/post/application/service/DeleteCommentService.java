package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.post.application.port.in.deleteComment.DeleteCommentCommand;
import space.space_spring.domain.post.application.port.in.deleteComment.DeleteCommentUseCase;
import space.space_spring.domain.post.application.port.out.DeleteCommentPort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.application.port.out.LoadCommentPort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.COMMENT_CREATOR_MISMATCH;

@Service
@RequiredArgsConstructor
public class DeleteCommentService implements DeleteCommentUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final LoadCommentPort loadCommentPort;
    private final DeleteCommentPort deleteCommentPort;

    @Override
    public void deleteComment(DeleteCommentCommand command) {
        // 1. Board, Post 조회
        Board board = loadBoardPort.loadById(command.getBoardId());
        Post post = loadPostPort.loadById(command.getPostId());
        Comment comment = loadCommentPort.loadById(command.getCommentId());

        // 2. validation
        validate(board, post, comment, command);

        // 3. 댓글 삭제
        deleteCommentPort.deleteComment(command.getCommentId());
    }

    private void validate(Board board, Post post, Comment comment, DeleteCommentCommand command) {
        if (!board.isInSpace(command.getSpaceId())) {       // board가 스페이스에 속하는지 검증
            throw new CustomException(BOARD_IS_NOT_IN_SPACE);
        }

        if (!post.isInBoard(board.getId())) {       // post가 보드에 속하는지 검증
            throw new CustomException(POST_IS_NOT_IN_BOARD);
        }

        if (!comment.isCommentCreator(command.getCommentCreatorId())) {     // 댓글 작성자가 본인이 맞는지
            throw new CustomException(COMMENT_CREATOR_MISMATCH);
        }
    }
}
