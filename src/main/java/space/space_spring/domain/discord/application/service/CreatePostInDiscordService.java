package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordUseCase;
import space.space_spring.domain.discord.application.port.out.createPost.CreatePostMessageCommand;
import space.space_spring.domain.discord.application.port.out.createPost.CreatePostMessagePort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePostInDiscordService implements CreatePostInDiscordUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadBoardPort loadBoardPort;
    private final CreatePostMessagePort createPostMessagePort;

    @Override
    @Transactional
    public Long createPostInDiscord(CreatePostInDiscordCommand command) {
        return createPostMessagePort.createPostMessage(mapToPostMessageCommand(command));
    }

    private CreatePostMessageCommand mapToPostMessageCommand(CreatePostInDiscordCommand command) {

        // 게시글 생성자의 DiscordId 조회
        SpaceMember postCreator = loadSpaceMemberPort.loadById(command.getPostCreatorId());
        Long postCreatorDiscordId = postCreator.getDiscordId();

        // Board 조회 및 DiscordId 가져오기
        Board board = loadBoardPort.loadById(command.getBoardId());
        Long boardDiscordId = board.getDiscordId();

        return CreatePostMessageCommand.builder()
                .spaceId(command.getSpaceId())
                .boardDiscordId(boardDiscordId)
                .postCreatorDiscordId(postCreatorDiscordId)
                .title(command.getTitle())
                .content(command.getContent())
                .attachments(command.getAttachments())
                .isAnonymous(command.getIsAnonymous())
                .build();
    }

}
