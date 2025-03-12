package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.updateComment.UpdateCommentCommand;
import space.space_spring.domain.post.application.port.in.updateComment.UpdateCommentFromDiscordCommand;
import space.space_spring.domain.post.application.port.in.updateComment.UpdateCommentUseCase;
import space.space_spring.domain.post.application.port.out.*;
import space.space_spring.domain.post.domain.*;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateCommentService implements UpdateCommentUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final LoadCommentPort loadCommentPort;
    private final UpdateCommentPort updateCommentPort;

    @Override
    @Transactional
    public void updateComment(UpdateCommentCommand command) {
        // 1. Board, Post 조회
        Board board = loadBoardPort.loadById(command.getBoardId());
        Post post = loadPostPort.loadById(command.getPostId());
        Comment comment = loadCommentPort.loadById(command.getCommentId());

        // 2. validation
        validate(board, post, comment, command);

        // 3. 댓글 update
        comment.changeContent(command.getContent());
        comment.changeAnonymous(command.isAnonymous());
        updateCommentPort.updateComment(comment);
    }
    @Override
    public void updateCommentFromDiscord(UpdateCommentFromDiscordCommand command){

        //Todo discord에서 수정된 댓글 수정

        return;
    }

    private void validate(Board board, Post post, Comment comment, UpdateCommentCommand command) {
        if (!board.isInSpace(command.getSpaceId())) {       // board가 스페이스에 속하는지 검증
            throw new CustomException(BOARD_IS_NOT_IN_SPACE);
        }

        if (!post.isInBoard(board.getId())) {       // post가 보드에 속하는지 검증
            throw new CustomException(POST_IS_NOT_IN_BOARD);
        }

        if (board.getBoardType() != BoardType.QUESTION && command.isAnonymous()) {      // 질문 게시글이 아닌데 댓글 작성자가 익명이라면
            throw new CustomException(CAN_NOT_BE_ANONYMOUS);
        }

        if (!comment.isCommentCreator(command.getCommentCreatorId())) {     // 댓글 작성자가 본인이 맞는지
            throw new CustomException(COMMENT_CREATOR_MISMATCH);
        }
    }
}
