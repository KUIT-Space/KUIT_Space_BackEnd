package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentCommand;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentUseCase;
import space.space_spring.domain.post.application.port.in.createComment.CreateAttachmentCommand;
import space.space_spring.domain.post.application.port.out.*;
import space.space_spring.domain.post.domain.*;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateCommentService implements CreateCommentUseCase {

    private final UploadAttachmentPort uploadAttachmentPort;
    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final CreateCommentPort createCommentPort;
    private final CreateAttachmentPort createAttachmentPort;

    @Override
    @Transactional
    public Long createCommentFromWeb(CreateCommentCommand command) {
        // 1. Board, Post 조회
        Board board = loadBoardPort.loadById(command.getBoardId());
        Post post = loadPostPort.loadByPostBaseId(command.getPostId());

        // 2. validation -> 게시판이 space에 속하는게 맞는지, 게시글이 게시판에 속하는게 맞는지
        validateBoardAndPost(board, post, command);

        // 3. s3에 댓글 첨부파일 upload & db에 attachment 저장
        Map<AttachmentType, List<MultipartFile>> attachmentsMap = command.getAttachmentCommands().stream()
                .collect(Collectors.groupingBy(
                        CreateAttachmentCommand::getAttachmentType,
                        Collectors.mapping(CreateAttachmentCommand::getAttachment, Collectors.toUnmodifiableList())
                ));
        Map<AttachmentType, List<String>> savedAttachmentUrls = uploadAttachmentPort.uploadAllAttachments(attachmentsMap, "comment");

        List<Attachment> attachments = new ArrayList<>();
        for (Map.Entry<AttachmentType, List<String>> entry : savedAttachmentUrls.entrySet()) {
            for (String savedAttachmentUrl : entry.getValue()) {
                attachments.add(Attachment.withoutId(command.getPostId(), entry.getKey(), savedAttachmentUrl));
            }
        }
        createAttachmentPort.createAttachments(attachments);

        // 4. discord 로 보내기
        Long discordId = 0L;        // 상준님과 협의

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

        if (board.getBoardType() != BoardType.QUESTION && command.isAnonymous()) {      // 질문 게시글이 아닌데 게시글 작성자가 익명이라면
            throw new CustomException(CAN_NOT_BE_ANONYMOUS);
        }
    }
}
