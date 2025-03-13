package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.in.updateComment.UpdateCommentInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.updateComment.UpdateCommentInDiscordUseCase;
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
    private final UpdateCommentInDiscordUseCase updateCommentInDiscordUseCase;

    @Override
    @Transactional
    public void updateComment(UpdateCommentCommand command) {
        // 1. Board, Post 조회
        Board board = loadBoardPort.loadById(command.getBoardId());
        Post post = loadPostPort.loadById(command.getPostId());
        Comment comment = loadCommentPort.loadById(command.getCommentId());

        // 2. validation
        validate(board, post, comment, command);

        // 3. 디스코드로 수정된 댓글 정보 보내기
        updateCommentInDiscordUseCase.updateCommentInDiscord(UpdateCommentInDiscordCommand.builder()
                .discordIdOfBoard(board.getDiscordId())
                .discordIdOfPost(post.getDiscordId())
                .discordIdOfComment(comment.getDiscordId())
                .build());

        // 3. 댓글 update
        comment.changeContent(command.getContent());
        updateCommentPort.updateComment(comment);
    }

    @Transactional
    @Override
    public void updateCommentFromDiscord(UpdateCommentFromDiscordCommand command) {
        /**
         * Comment 도메인 엔티티를 조회하지 말고, 그냥 바로 update port로 보내는게 더 좋은건가??
         * -> 어차피 db 한번 찔러서 CommentJpaEntity 만드는건 똑같긴하다
         */
        // 1. discordId로 수정할 comment 찾기
        Comment comment = loadCommentPort.loadByDiscordId(command.getDiscordMessageId());

        // 2. comment update
        comment.changeContent(command.getContent());
        updateCommentPort.updateComment(comment);
    }

    private void validate(Board board, Post post, Comment comment, UpdateCommentCommand command) {
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
