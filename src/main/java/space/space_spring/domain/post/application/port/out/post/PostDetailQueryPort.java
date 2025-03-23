package space.space_spring.domain.post.application.port.out.post;

public interface PostDetailQueryPort {

    PostDetailView loadPostDetail(Long postId);
}
