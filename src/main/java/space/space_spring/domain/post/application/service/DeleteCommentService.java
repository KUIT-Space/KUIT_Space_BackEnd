package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.in.deleteComment.DeleteCommentInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.deleteComment.DeleteCommentInDiscordUseCase;
import space.space_spring.domain.post.application.port.in.deleteComment.DeleteCommentCommand;
import space.space_spring.domain.post.application.port.in.deleteComment.DeleteCommentUseCase;
import space.space_spring.domain.post.application.port.out.DeleteCommentPort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.application.port.out.LoadCommentPort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.application.port.out.like.DeleteLikePort;
import space.space_spring.domain.post.application.port.out.like.LoadLikePort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.post.domain.Like;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.COMMENT_CREATOR_MISMATCH;

@Service
@RequiredArgsConstructor
public class DeleteCommentService implements DeleteCommentUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final LoadCommentPort loadCommentPort;
    private final LoadLikePort loadLikePort;
    private final DeleteCommentPort deleteCommentPort;
    private final DeleteLikePort deleteLikePort;
    private final DeleteCommentInDiscordUseCase deleteCommentInDiscordUseCase;

    @Transactional
    @Override
    public void deleteCommentFromWeb(DeleteCommentCommand command) {
        // 1. Board, Post 조회
        Board board = loadBoardPort.loadById(command.getBoardId());
        Post post = loadPostPort.loadById(command.getPostId());
        Comment comment = loadCommentPort.loadById(command.getCommentId());
        List<Like> likes = loadLikePort.loadAllLikes(command.getCommentId());

        // 2. validation
        validate(board, post, comment, command);

        // 3. 삭제할 댓글 디스코드에 반영
        deleteCommentInDiscordUseCase.deleteCommentInDiscord(DeleteCommentInDiscordCommand.builder()
                        .discordIdOfBoard(board.getDiscordId())
                        .discordIdOfPost(post.getDiscordId())
                        .discordIdOfComment(comment.getDiscordId()).build());

        // 4. 댓글 삭제
        deleteCommentPort.deleteComment(command.getCommentId());

        // 5. 댓글 좋아요 삭제
        deleteLikePort.deleteAllLikes(likes);
    }

    @Transactional
    @Override
    public void deleteCommentFromDiscord(Long commentId){
        deleteCommentPort.deleteComment(commentId);
        // TODO 댓글 좋아요 삭제 처리
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
