package space.space_spring.domain.post.application.port.in.createPost;

public interface CreatePostUseCase {

    Long createPostFromWeb(Long spaceMemberId, Long spaceId, CreatePostCommand command);

    Long createPostFromDiscord(CreatePostCommand command, Long discordId);
}
