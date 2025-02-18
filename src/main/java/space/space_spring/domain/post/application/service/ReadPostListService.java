package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.adapter.in.web.readPostList.ResponseOfReadPostList;
import space.space_spring.domain.post.application.port.in.readPostList.ReadPostListUseCase;
import space.space_spring.domain.post.application.port.in.readPostList.SummaryOfPost;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.domain.Post;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReadPostListService implements ReadPostListUseCase {

    private final LoadPostPort loadPostPort;

    @Override
    public List<ResponseOfReadPostList> readPostList(Long boardId) {
        List<SummaryOfPost> summaryOfPosts = loadPostPort.loadPostList(boardId);
        return summaryOfPosts.stream()
                .map(ResponseOfReadPostList::of)
                .collect(Collectors.toList());
    }
}
