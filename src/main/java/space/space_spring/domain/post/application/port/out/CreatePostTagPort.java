package space.space_spring.domain.post.application.port.out;

public interface CreatePostTagPort {

    void createPostTag(Long postId, Long tagId);
}
