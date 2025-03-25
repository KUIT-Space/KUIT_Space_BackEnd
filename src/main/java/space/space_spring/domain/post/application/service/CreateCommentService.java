package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.in.createComment.CreateCommentInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.createComment.CreateCommentInDiscordUseCase;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentCommand;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentUseCase;
import space.space_spring.domain.post.application.port.out.*;
import space.space_spring.domain.post.application.port.out.comment.AnonymousCommentCreatorView;
import space.space_spring.domain.post.application.port.out.comment.CommentCreatorQueryPort;
import space.space_spring.domain.post.domain.*;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberInfoPort;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;
import space.space_spring.global.exception.CustomException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateCommentService implements CreateCommentUseCase {

    private static final String ANONYMOUS_COMMENT_CREATOR = "익명 스페이서";
    private static final String POST_CREATOR_NICKNAME = "게시글 작성자";

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final CreateCommentPort createCommentPort;
    private final LoadSpaceMemberInfoPort loadSpaceMemberInfoPort;
    private final CreateCommentInDiscordUseCase createCommentInDiscordUseCase;
    private final CommentCreatorQueryPort commentCreatorQueryPort;

    @Override
    @Transactional
    public Long createCommentFromWeb(CreateCommentCommand command) {
        // 1. Board, Post 조회 및 검증
        Board board = loadBoardPort.loadById(command.getBoardId());
        Post post = loadPostPort.loadById(command.getPostId());
        validateBoardAndPost(board, post, command);

        // 2. 댓글 작성자 정보 조회 및 필요시 변경
        NicknameAndProfileImage nicknameAndProfileImage = resolveCreatorInfo(command.getCommentCreatorId(), post, command.getIsAnonymous());

        // 3. 디스코드로 보내기
        Long discordId = createCommentInDiscordUseCase.send(CreateCommentInDiscordCommand.of(command, nicknameAndProfileImage.getNickname(), nicknameAndProfileImage.getProfileImageUrl()));

        // 4. db에 comment 저장
        return createCommentPort.createComment(command.toDomainEntity(discordId));
    }

    @Override
    @Transactional
    public Long createCommentFromDiscord(CreateCommentCommand command, Long discordId) {
        return createCommentPort.createComment(command.toDomainEntity(discordId));
    }

    private NicknameAndProfileImage resolveCreatorInfo(Long commentCreatorId, Post post, Boolean isAnonymousComment) {
        NicknameAndProfileImage nicknameAndProfileImage = loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(commentCreatorId);

        // 비익명 댓글인 경우
        if (!isAnonymousComment) {
            return nicknameAndProfileImage;
        }

        // 익명 댓글인 경우
        String creatorNickname = resolveAnonymousNickname(commentCreatorId, post);
        return NicknameAndProfileImage.of(creatorNickname, null);
    }

    private String resolveAnonymousNickname(Long commentCreatorId, Post post) {
        List<AnonymousCommentCreatorView> anonymousViews = commentCreatorQueryPort.loadAnonymousCommentCreators(post.getId());
        Map<Long, String> anonymousNicknameMap = new HashMap<>();
        int anonymousIndex = 1;

        for (AnonymousCommentCreatorView view : anonymousViews) {
            Long creatorId = view.getCreatorId();
            if (!anonymousNicknameMap.containsKey(creatorId) && !view.getIsPostOwner()) {
                anonymousNicknameMap.put(creatorId, ANONYMOUS_COMMENT_CREATOR + " " + anonymousIndex++);
            }
        }

        if (post.isPostCreator(commentCreatorId)) {
            return POST_CREATOR_NICKNAME;
        } else {
            return anonymousNicknameMap.getOrDefault(commentCreatorId, ANONYMOUS_COMMENT_CREATOR + " " + anonymousIndex);
        }
    }

    private void validateBoardAndPost(Board board, Post post, CreateCommentCommand command) {
        if (!board.isInSpace(command.getSpaceId())) {
            throw new CustomException(BOARD_IS_NOT_IN_SPACE);
        }

        if (!post.isInBoard(board.getId())) {
            throw new CustomException(POST_IS_NOT_IN_BOARD);
        }

        if (board.getBoardType() != BoardType.QUESTION && command.getIsAnonymous()) {      // 질문 게시글이 아닌데 게시글 작성자가 익명이라면
            throw new CustomException(CAN_NOT_BE_ANONYMOUS);
        }
    }
}
