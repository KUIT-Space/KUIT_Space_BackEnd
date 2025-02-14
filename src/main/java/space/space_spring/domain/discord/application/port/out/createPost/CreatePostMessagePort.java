package space.space_spring.domain.discord.application.port.out.createPost;

public interface CreatePostMessagePort {

    Long createPostMessage(CreatePostMessageCommand command);
}
