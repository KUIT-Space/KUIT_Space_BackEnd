package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.createPost.CreatePostInDiscordUseCase;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.domain.post.application.port.out.CreatePostBasePort;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.post.domain.PostBase;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePostService implements CreatePostUseCase {

    private final CreatePostBasePort createPostBasePort;
    private final CreatePostPort createPostPort;
    private final CreatePostInDiscordUseCase createPostInDiscordUseCase;

    @Override
    @Transactional
    public Long createPost(CreatePostCommand command) {

        // 1. 디스코드로 보내기
        Long discordIdForPost = createPostInDiscordUseCase.createPostInDiscord(mapToDiscordCommand(command));

        // 2. PostBase 도메인 엔티티 생성 및 저장
        Long postBaseId = savePostBase(command, discordIdForPost);

        // 3. Post 도메인 엔티티 생성 및 저장
        Long postId = savePost(command, postBaseId);

        return postId;
    }

    private CreatePostInDiscordCommand mapToDiscordCommand(CreatePostCommand command) {
        return CreatePostInDiscordCommand.builder()
                .postCreatorId(command.getPostCreatorId())
                .boardId(command.getBoardId())
                .title(command.getTitle())
                .attachments(command.getAttachments())
                .build();
    }

    private Long savePostBase(CreatePostCommand command, Long discordIdForPost) {
        PostBase postBase = command.toPostBaseDomainEntity(discordIdForPost);
        return createPostBasePort.createPostBase(postBase);
    }

    private Long savePost(CreatePostCommand command, Long postBaseId) {
        Post post = command.toPostDomainEntity(postBaseId);
        return createPostPort.createPost(post);
    }

}
