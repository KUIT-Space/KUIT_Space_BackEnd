package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.application.port.in.readPostList.SummaryOfPost;
import space.space_spring.domain.post.domain.Post;

import java.util.List;

public interface LoadPostPort {

    List<SummaryOfPost> loadPostList(Long boardId);
}
