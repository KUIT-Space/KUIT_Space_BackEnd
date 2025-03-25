package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(CreateCommentService.class);
    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final CreateCommentPort createCommentPort;
    private final LoadSpaceMemberInfoPort loadSpaceMemberInfoPort;
    private final CreateCommentInDiscordUseCase createCommentInDiscordUseCase;
    private final CommentCreatorQueryPort commentCreatorQueryPort;

    @Override
    @Transactional
    public Long createCommentFromWeb(CreateCommentCommand command) {
        // 1. Board, Post 조회
        Board board = loadBoardPort.loadById(command.getBoardId());
        Post post = loadPostPort.loadById(command.getPostId());

        // 2. validation -> 게시판이 space에 속하는게 맞는지, 게시글이 게시판에 속하는게 맞는지
        validateBoardAndPost(board, post, command);

        /**
         * space 2.0 v1 에서는 댓글 수정 시에 첨부파일 update 요구사항 없음
         */

//        // 3. s3에 댓글 첨부파일 upload & db에 attachment 저장
//        Map<AttachmentType, List<MultipartFile>> attachmentsMap = command.getAttachmentCommands().stream()
//                .collect(Collectors.groupingBy(
//                        CreateAttachmentCommand::getAttachmentType,
//                        Collectors.mapping(CreateAttachmentCommand::getAttachment, Collectors.toUnmodifiableList())
//                ));
//        Map<AttachmentType, List<String>> savedAttachmentUrls = uploadAttachmentPort.uploadAllAttachments(attachmentsMap, "comment");
//
//        List<Attachment> attachments = new ArrayList<>();
//        for (Map.Entry<AttachmentType, List<String>> entry : savedAttachmentUrls.entrySet()) {
//            for (String savedAttachmentUrl : entry.getValue()) {
//                attachments.add(Attachment.withoutId(command.getPostId(), entry.getKey(), savedAttachmentUrl));
//            }
//        }
//        createAttachmentPort.createAttachments(attachments);

        // 4. discord 로 보내기
        NicknameAndProfileImage nicknameAndProfileImage = loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(command.getCommentCreatorId());
        String creatorNickname = nicknameAndProfileImage.getNickname();
        String creatorProfileImageUrl = nicknameAndProfileImage.getProfileImageUrl();

        if (command.getIsAnonymous()) {
            List<AnonymousCommentCreatorView> anonymousCommentCreatorViews = commentCreatorQueryPort.loadAnonymousCommentCreators(command.getPostId());

            Map<Long, String> anonymousNicknameMap = new HashMap<>();
            int anonymousCount = 1;

            for (AnonymousCommentCreatorView view : anonymousCommentCreatorViews) {
                Long creatorId = view.getCreatorId();
                if (!anonymousNicknameMap.containsKey(creatorId)) {
                    if (!view.getIsPostOwner()) {
                        anonymousNicknameMap.put(creatorId, "익명 스페이서" + " " + anonymousCount++);
                    }
                }
            }

            if (post.isPostCreator(command.getCommentCreatorId())) creatorNickname = "게시글 작성자";
            else {
                if (anonymousNicknameMap.containsKey(command.getCommentCreatorId())) {
                    creatorNickname = anonymousNicknameMap.get(command.getCommentCreatorId());
                } else {
                    creatorNickname = "익명 스페이서" + " " + anonymousCount;
                }
            }

            creatorProfileImageUrl = null;
        }

        Long discordId = createCommentInDiscordUseCase.send(CreateCommentInDiscordCommand.of(command, creatorNickname, creatorProfileImageUrl));

        // 5. db에 comment 저장
        return createCommentPort.createComment(command.toDomainEntity(discordId));
    }

    @Override
    @Transactional
    public Long createCommentFromDiscord(CreateCommentCommand command, Long discordId) {
        return createCommentPort.createComment(command.toDomainEntity(discordId));
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
