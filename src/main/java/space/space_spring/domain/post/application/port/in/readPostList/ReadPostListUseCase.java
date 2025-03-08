package space.space_spring.domain.post.application.port.in.readPostList;

public interface ReadPostListUseCase {

    ListOfPostSummary readPostList(Long boardId);
}
