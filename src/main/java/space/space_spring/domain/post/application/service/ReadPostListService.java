package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.ReadPostListUseCase;
import space.space_spring.domain.post.application.port.out.LoadPostPort;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReadPostListService implements ReadPostListUseCase {

    private final LoadPostPort loadPostPort;

    @Override
    public ListOfPostSummary readPostList(Long boardId) {
        return loadPostPort.loadPostList(boardId);
    }
}
