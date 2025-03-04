package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.ReadPostListUseCase;
import space.space_spring.domain.post.application.port.out.LoadLikePort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.domain.Post;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReadPostListService implements ReadPostListUseCase {

    private final LoadPostPort loadPostPort;
    private final LoadLikePort loadLikePort;

    @Override
    public ListOfPostSummary readPostList(Long boardId) {
        // 1. Post 도메인 객체 리스트 가져오기
        List<Post> posts = loadPostPort.loadPostList(boardId);

        // 2. postId 리스트 추출(postId = postBaseId)
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        // 3. 좋아요 수 조회
        Map<Long, Long> likeCounts = loadLikePort.countLikesByPostIds(postIds);

        // 4. 댓글 수 조회
        Map<Long, Long> commentCounts = loadCommentPort.countCommentsByPostIds(postIds);

        // 5. ListOfPostSummary 생성
        return ListOfPostSummary.of();
    }
}
