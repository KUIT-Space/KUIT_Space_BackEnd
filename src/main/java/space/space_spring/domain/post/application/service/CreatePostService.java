package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordUseCase;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.domain.Post;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePostService implements CreatePostUseCase {

    private final CreatePostPort createPostPort;
    private final CreatePostInDiscordUseCase createPostInDiscordUseCase;

    @Override
    @Transactional
    public Long createPostFromWeb(Long spaceMemberId, Long spaceId, CreatePostCommand command) {



        // 1. 디스코드로 게시글 정보 전송
        Long discordIdForPost = createPostInDiscordUseCase.createPostInDiscord(mapToDiscordCommand(command));

        // 2. Post 도메인 엔티티 생성 후 Adapter에 저장
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

}
