package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordUseCase;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePostService implements CreatePostUseCase {

    private final CreatePostPort createPostPort;
    private final LoadBoardPort loadBoardPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final CreatePostInDiscordUseCase createPostInDiscordUseCase;

    @Override
    @Transactional
    public Long createPostFromWeb(CreatePostCommand command) {
        // 1. Board 조회
        Board board = loadBoardPort.loadById(command.getBoardId());

        // 2. SpaceMember 조회
        SpaceMember spaceMember = loadSpaceMemberPort.loadById(command.getPostCreatorId());

        // 3. validate
        validateBoardAndSpaceMember(board, spaceMember, command);

        // 4. 디스코드로 게시글 정보 전송
        Long discordIdForPost = createPostInDiscordUseCase.createPostInDiscord(mapToDiscordCommand(command));

        // 5. Post 도메인 엔티티 생성 후 Adapter에 저장
        Post post = command.toPostDomainEntity(discordIdForPost);

        return createPostPort.createPost(post);
    }

    @Override
    @Transactional
    public Long createPostFromDiscord(CreatePostCommand command, Long discordId) {
        // 1. Post 도메인 엔티티 생성 후 Adapter에 저장
        Post post = command.toPostDomainEntity(discordId);

        return createPostPort.createPost(post);
    }


    private CreatePostInDiscordCommand mapToDiscordCommand(CreatePostCommand command) {
        return CreatePostInDiscordCommand.builder()
                .postCreatorId(command.getPostCreatorId())
                .boardId(command.getBoardId())
                .title(command.getTitle())
                .attachments(command.getAttachments())
                .build();
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
}
