package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.in.deletePost.DeletePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.deletePost.DeletePostInDiscordUseCase;
import space.space_spring.domain.post.application.port.in.deletePost.DeletePostCommand;
import space.space_spring.domain.post.application.port.in.deletePost.DeletePostUseCase;
import space.space_spring.domain.post.application.port.out.*;
import space.space_spring.domain.post.application.port.out.like.DeleteLikePort;
import space.space_spring.domain.post.application.port.out.like.LoadLikePort;
import space.space_spring.domain.post.domain.*;
import space.space_spring.global.exception.CustomException;

import java.util.List;
import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.UNAUTHORIZED_USER;

@Service
@RequiredArgsConstructor
public class DeletePostService implements DeletePostUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final LoadAttachmentPort loadAttachmentPort;
    private final LoadCommentPort loadCommentPort;
    private final LoadLikePort loadLikePort;
    private final DeletePostPort deletePostPort;
    private final DeleteCommentPort deleteCommentPort;
    private final DeleteAttachmentPort deleteAttachmentPort;
    private final DeleteLikePort deleteLikePort;
    private final DeletePostInDiscordUseCase deletePostInDiscordUseCase;

    @Override
    @Transactional
    public void deletePostFromWeb(DeletePostCommand command) {
        // 1. Board 조회
        Board board = loadBoardPort.loadById(command.getBoardId());

        // 2. Post 조회
        Post post = loadPostPort.loadById(command.getPostId());

        // validate
        validate(board, post, command);

        // Attachment 조회
        List<Attachment> attachments = loadAttachmentPort.loadByPostId(command.getPostId());

        // Comment 조회
        List<Comment> comments = loadCommentPort.loadAllComments(command.getPostId());

        // Like 조회
        List<Like> likes = loadLikePort.loadAllLikes(command.getPostId());

        // 디스코드에서 해당 게시글 & 모든 댓글들 삭제
        deletePostInDiscordUseCase.deletePostInDiscord(DeletePostInDiscordCommand.builder()
                .discordIdOfBoard(board.getDiscordId())
                .discordIdOfPost(post.getDiscordId())
                .discordIdOfComments(comments.stream().map(Comment::getDiscordId).toList())
                .build());

        // 첨부파일 삭제
        deleteAttachmentPort.deleteAllAttachments(attachments);

        // 댓글 좋아요 삭제
        comments.forEach(comment -> {
            List<Like> commentLikes = loadLikePort.loadAllLikes(comment.getId());
            deleteLikePort.deleteAllLikes(commentLikes);
        });

        // 댓글 삭제
        deleteCommentPort.deleteAllComments(comments);

        // 게시글 좋아요 삭제
        deleteLikePort.deleteAllLikes(likes);

        // 게시글 삭제
        deletePostPort.deletePost(command.getPostId());
    }

    @Override
    @Transactional
    public void deletePostFromDiscord(Long targetDiscordId) {
        // 제거할 post 조회
        Optional<Post> optionalPost = loadPostPort.loadByDiscordId(targetDiscordId);
        if (!optionalPost.isPresent()) {
            throw new CustomException(POST_NOT_FOUND);
        }

        Post post = optionalPost.get();

        // post와 연관된 attachment, like, comment 조회 & 삭제
        List<Attachment> attachments = loadAttachmentPort.loadByPostId(post.getId());
        List<Comment> comments = loadCommentPort.loadAllComments(post.getId());
        List<Like> likes = loadLikePort.loadAllLikes(post.getBoardId());

        // 삭제
        deleteAttachmentPort.deleteAllAttachments(attachments);
        deleteCommentPort.deleteAllComments(comments);
        deleteLikePort.deleteAllLikes(likes);
        deletePostPort.deletePost(post.getId());
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
