package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordUseCase;
import space.space_spring.domain.post.application.port.in.createPost.AttachmentInDiscordCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostFromDiscordCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.domain.post.application.port.out.*;
import space.space_spring.domain.post.domain.*;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberInfoPort;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.validator.AllowedDocumentFileExtensions;
import space.space_spring.global.validator.AllowedImageFileExtensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePostService implements CreatePostUseCase {

    private final CreatePostPort createPostPort;
    private final CreatePostTagPort createPostTagPort;
    private final LoadBoardPort loadBoardPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadSpaceMemberInfoPort loadSpaceMemberInfoPort;
    private final UploadAttachmentPort uploadAttachmentPort;
    private final CreatePostInDiscordUseCase createPostInDiscordUseCase;
    private final CreateAttachmentPort createAttachmentPort;

    @Override
    @Transactional
    public Long createPostFromWeb(CreatePostCommand command) {
        // 1. Board 조회
        Board board = loadBoardPort.loadById(command.getBoardId());

        // 2. SpaceMember 조회
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(command.getPostCreatorId());

        // 3. validate
        validateBoardAndSpaceMember(board, spaceMember, command);

        // 4. S3에 게시글 첨부파일 upload
        Map<AttachmentType, List<MultipartFile>> attachmentsMap = command.getAttachments().stream()
                .collect(Collectors.groupingBy(file -> {
                    if (isImageFile(file)) {
                        return AttachmentType.IMAGE; // 만약 BoardType.Image가 필요하다면 해당 enum으로 변경하세요.
                    } else if (isDocumentFile(file)) {
                        return AttachmentType.FILE; // 마찬가지로 BoardType.File로 변경 가능.
                    } else {
                        throw new CustomException(UNSUPPORTED_FILE_TYPE); // 지원하지 않는 파일 형식일 경우 예외 처리
                    }
                }));

        Map<AttachmentType, List<String>> attachmentUrlsMap = uploadAttachmentPort.uploadAllAttachments(attachmentsMap, "post");

        // 5. 디스코드로 게시글 정보 전송
        NicknameAndProfileImage nicknameAndProfileImage = loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(command.getPostCreatorId());
        String creatorNickname = nicknameAndProfileImage.getNickname();
        String creatorProfileImageUrl = nicknameAndProfileImage.getProfileImageUrl();

        if (command.getIsAnonymous()) {
            creatorNickname = "익명 스페이서";
            creatorProfileImageUrl = null;
        }

        List<AttachmentInDiscordCommand> discordAttachments = new ArrayList<>();
        attachmentUrlsMap.forEach((type, urls) -> urlsToAttachmentCommands(type, urls, discordAttachments));

        Long discordIdForPost = createPostInDiscordUseCase.CreateMessageInDiscord(CreatePostInDiscordCommand.of(command, creatorNickname, creatorProfileImageUrl, discordAttachments));

        // 6. Post 도메인 엔티티 생성 후 db에 저장
        Post post = command.toPostDomainEntity(discordIdForPost);
        Long postId = createPostPort.createPost(post);

        // 7. 태그 저장
        if (!command.getTagIds().isEmpty()) {
            createPostTagPort.createPostTag(postId, command.getTagIds());
        }

        // 8. Attachment 도메인 엔티티 생성 후 db에 저장
        List<Attachment> attachments = new ArrayList<>();
        attachmentUrlsMap.forEach((type, urls) -> urls.forEach(url ->
                attachments.add(Attachment.withoutId(postId, type, url))
        ));
        createAttachmentPort.createAttachments(attachments);
        return postId;
    }

    @Override
    @Transactional
    public Long createPostFromDiscord(CreatePostFromDiscordCommand command, Long discordId) {
        // 1. Post 도메인 엔티티 생성 후 db에 저장
        Post post = command.toPostDomainEntity(discordId);
        Long postId = createPostPort.createPost(post);

        // 2. Attachment 도메인 엔티티 생성 후 db에 저장
        /**
         * TODO : 게시글 생성 Discord Input, attachment 파일 형식 정해진 후 리팩토링
         */
        // 7. 태그 저장
        if (!command.getTagIds().isEmpty()) {
            createPostTagPort.createPostTag(postId, command.getTagIds());
        }
        // 8. Attachment 도메인 엔티티 생성 후 db에 저장
        List<Attachment> attachments = new ArrayList<>();
        command.getAttachments().forEach((url,type ) ->
                attachments.add(Attachment.withoutId(postId, type, url))
        );
        createAttachmentPort.createAttachments(attachments);

        return postId;
    }

    private void validateBoardAndSpaceMember(Board board, SpaceMember spaceMember, CreatePostCommand command) {
        // 1. 해당 게시판이 스페이스에 속하는 지
        if (!board.isInSpace(command.getSpaceId())) {
            throw new CustomException(BOARD_IS_NOT_IN_SPACE);
        }

        // 2. 질문 게시판이 아닌 게시판의 익명 여부가 FALSE인지
        if (board.getBoardType() != BoardType.QUESTION && command.getIsAnonymous()) {
            throw new CustomException(CAN_NOT_BE_ANONYMOUS);
        }

        // 3. 공지사항, 기수별 공지사항의 작성자가 운영진인지
        if ((board.getBoardType() == BoardType.NOTICE || board.getBoardType() == BoardType.SEASON_NOTICE)
            && !spaceMember.isManager()) {
            throw new CustomException(UNAUTHORIZED_USER);
        }
    }

    private void urlsToAttachmentCommands(AttachmentType attachmentType, List<String> attachmentUrls, List<AttachmentInDiscordCommand> commands) {
        attachmentUrls.forEach(url -> commands.add(AttachmentInDiscordCommand.create(attachmentType.name(), url)));
    }

    // MultipartFile이 지원하는 이미지 파일 형식인지 검증
    private boolean isImageFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        log.info("extension : {}", extension);

        return AllowedImageFileExtensions.contains(extension);
    }

    // MultipartFile이 지원하는 문서 파일 형식인지 검증
    private boolean isDocumentFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        log.info("extension : {}", extension);

        return AllowedDocumentFileExtensions.contains(extension);
    }
}
