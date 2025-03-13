package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.in.deletePost.DeletePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.deletePost.DeletePostInDiscordUseCase;
import space.space_spring.domain.post.application.port.in.deletePost.DeletePostCommand;
import space.space_spring.domain.post.application.port.in.deletePost.DeletePostUseCase;
import space.space_spring.domain.post.application.port.out.*;
import space.space_spring.domain.post.domain.Attachment;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.global.exception.CustomException;

import java.util.List;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.UNAUTHORIZED_USER;

@Service
@RequiredArgsConstructor
public class DeletePostService implements DeletePostUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final LoadAttachmentPort loadAttachmentPort;
    private final LoadCommentPort loadCommentPort;
    private final DeletePostPort deletePostPort;
    private final DeleteCommentPort deleteCommentPort;
    private final DeleteAttachmentPort deleteAttachmentPort;
    private final DeletePostInDiscordUseCase deletePostInDiscordUseCase;

    @Override
    @Transactional
    public void deletePostFromWeb(DeletePostCommand command) {
        // 1. Board 조회
        Board board = loadBoardPort.loadById(command.getBoardId());

        // 2. Post 조회
        Post post = loadPostPort.loadById(command.getPostId());

        // 3. Attachment 조회
        List<Attachment> attachments = loadAttachmentPort.loadById(command.getPostId());

        // 4. Comment 조회
        List<Comment> comments = loadCommentPort.loadAllComments(command.getPostId());

        // 4. validate
        validate(board, post, command);

        // 5. 디스코드에서 해당 게시글 & 모든 댓글들 삭제
        deletePostInDiscordUseCase.deletePost(DeletePostInDiscordCommand.builder()
                .discordIdOfBoard(board.getDiscordId())
                .discordIdOfPost(post.getDiscordId())
                .discordIdOfComments(comments.stream().map(Comment::getDiscordId).toList())
                .build());

        // 6. 첨부파일 삭제
        deleteAttachmentPort.deleteAllAttachments(attachments);

        // 7. 댓글 삭제
        if (!comments.isEmpty()) {
            deleteCommentPort.deleteAllComments(comments);
        }

        // 8. 게시글 삭제
        deletePostPort.deletePost(command.getPostId());
    }

    @Override
    @Transactional
    public void deletePostFromDiscord(DeletePostCommand command) {
        /**
         * TODO : 디스코드에서 게시글 삭제 로직
         */
    }

    private void validate(Board board, Post post, DeletePostCommand command) {
        // 1. 해당 게시판이 스페이스에 속하는 지
        if (!board.isInSpace(command.getSpaceId())) {
            throw new CustomException(BOARD_IS_NOT_IN_SPACE);
        }

        // 2. 해당 게시글이 게시판에 속하는 지
        if (!post.isInBoard(board.getId())) {
            throw new CustomException(POST_IS_NOT_IN_BOARD);
        }

        // 3. 게시글 작성자가 본인이 맞는지
        if (!post.isPostCreator(command.getPostCreatorId())) {
            throw new CustomException(UNAUTHORIZED_USER);
        }
    }
}
