package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.application.port.in.createPost.AttachmentOfCreateCommand;
import space.space_spring.domain.post.application.port.in.updatePost.UpdatePostAttachmentCommand;
import space.space_spring.domain.post.application.port.in.updatePost.UpdatePostCommand;
import space.space_spring.domain.post.application.port.in.updatePost.UpdatePostUseCase;
import space.space_spring.domain.post.application.port.out.*;
import space.space_spring.domain.post.domain.*;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdatePostService implements UpdatePostUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadAttachmentPort loadAttachmentPort;
    private final DeleteAttachmentPort deleteAttachmentPort;
    private final UploadAttachmentPort uploadAttachmentPort;
    private final CreateAttachmentPort createAttachmentPort;
    private final UpdatePostPort updatePostPort;

    @Override
    @Transactional
    public void updatePostFromWeb(UpdatePostCommand command) {
        // 1. Board 조회
        Board board = loadBoardPort.loadById(command.getBoardId());

        // 2. Post 조회
        Post post = loadPostPort.loadById(command.getPostId());

        // 3. SpaceMember 조회
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(command.getPostCreatorId());

        // 4. validate
        validate(board, post, spaceMember, command);

        // 5. 이미지 수정
        updateAttachments(command);

        // 6. 디스코드로 게시글 수정 정보 전송

        // 7. 게시글 update
        post.updatePost(command.getTitle(), command.getContent(), command.getIsAnonymous());
        updatePostPort.updatePost(post);
    }

    private void validate(Board board, Post post, SpaceMember spaceMember, UpdatePostCommand command) {
        // 1. 해당 게시판이 스페이스에 속하는 지
        if (!board.isInSpace(command.getSpaceId())) {
            throw new CustomException(BOARD_IS_NOT_IN_SPACE);
        }

        // 2. 해당 게시글이 게시판에 속하는 지
        if (!post.isInBoard(board.getId())) {
            throw new CustomException(POST_IS_NOT_IN_BOARD);
        }

        // 3. 질문 게시판이 아닌 게시판의 익명 여부가 FALSE인지
        if (board.getBoardType() != BoardType.QUESTION && command.getIsAnonymous()) {
            throw new CustomException(CAN_NOT_BE_ANONYMOUS);
        }

        // 4. 공지사항, 기수별 공지사항의 작성자가 운영진인지
        if ((board.getBoardType() == BoardType.NOTICE || board.getBoardType() == BoardType.SEASON_NOTICE)
                && !spaceMember.isManager()) {
            throw new CustomException(UNAUTHORIZED_USER);
        }

        // 5. 게시글 작성자가 본인이 맞는지
        if (!post.isPostCreator(command.getPostCreatorId())) {
            throw new CustomException(UNAUTHORIZED_USER);
        }
    }

    @Override
    @Transactional
    public void updatePostFromDiscord(UpdatePostCommand command, Long discordId) {
        /**
         * TODO : 디스코드에서 게시글 수정 로직
         */
    }

    private void updateAttachments(UpdatePostCommand command) {
        // 1. Attachment 조회
        List<Attachment> attachments = loadAttachmentPort.loadById(command.getPostId());

        // 2. S3에서 기존 첨부파일 삭제
        List<String> attachmentUrls = attachments.stream()
                .map(Attachment::getAttachmentUrl)
                .toList();
        deleteAttachmentPort.deleteAllAttachments(attachmentUrls);

        // 3. 새로운 첨부파일 업데이트
        if (!command.getAttachments().isEmpty()) {
            Map<AttachmentType, List<MultipartFile>> attachmentsMap = command.getAttachments().stream()
                    .collect(Collectors.groupingBy(
                            UpdatePostAttachmentCommand::getAttachmentType,
                            Collectors.mapping(UpdatePostAttachmentCommand::getAttachment, Collectors.toUnmodifiableList())
                    ));
            Map<AttachmentType, List<String>> attachmentUrlsMap = uploadAttachmentPort.uploadAllAttachments(attachmentsMap, "post");

            // Attachment 도메인 엔티티 생성 후 db에 저장
            List<Attachment> newAttachments = new ArrayList<>();
            attachmentUrlsMap.forEach((type, urls) -> urls.forEach(url ->
                    attachments.add(Attachment.withoutId(command.getPostId(), type, url))
            ));
            createAttachmentPort.createAttachments(newAttachments);
        }
    }
}
