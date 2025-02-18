package space.space_spring.domain.post.application.port.in.createPost;

public interface CreatePostUseCase {

    Long createPost(CreatePostCommand command);
}
