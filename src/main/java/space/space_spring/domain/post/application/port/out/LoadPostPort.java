package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.PostSummary;

import java.util.List;

public interface LoadPostPort {

    ListOfPostSummary loadPostList(Long boardId);
}
