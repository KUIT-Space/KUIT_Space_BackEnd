package space.space_spring.domain.post.application.port.in.readPostList;

import space.space_spring.domain.post.adapter.in.web.readPostList.ResponseOfReadPostList;

import java.util.List;

public interface ReadPostListUseCase {

    List<ResponseOfReadPostList> readPostList(Long boardId);
}
