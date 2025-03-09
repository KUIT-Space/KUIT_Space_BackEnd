package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.post.application.port.in.deletePost.DeletePostCommand;
import space.space_spring.domain.post.application.port.in.deletePost.DeletePostUseCase;
import space.space_spring.domain.post.application.port.out.DeletePostPort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.UNAUTHORIZED_USER;

@Service
@RequiredArgsConstructor
public class DeletePostService implements DeletePostUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final DeletePostPort deletePostPort;

    @Override
    public void deletePostFromWeb(DeletePostCommand command) {

        // 1. Board 조회
        Board board = loadBoardPort.loadById(command.getBoardId());

        // 2. Post 조회
        Post post = loadPostPort.loadById(command.getPostId());

        // 3. validate
        validate(board, post, command);

        // 4. 댓글 삭제
        deletePostPort.deletePost(command.getPostId());
    }

    @Override
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
