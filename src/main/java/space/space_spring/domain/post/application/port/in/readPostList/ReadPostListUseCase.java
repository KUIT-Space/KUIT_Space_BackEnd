package space.space_spring.domain.post.application.port.in.readPostList;

import org.springframework.data.domain.Pageable;

public interface ReadPostListUseCase {

    ListOfPostSummary readPostList(Long spaceMemberId, Long boardId, Long tagId, Pageable pageable);
}
