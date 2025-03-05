package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.PostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.ReadPostListUseCase;
import space.space_spring.domain.post.application.port.out.LoadAttachmentPort;
import space.space_spring.domain.post.application.port.out.LoadCommentPort;
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
    private final LoadCommentPort loadCommentPort;
    private final LoadAttachmentPort loadAttachmentPort;

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

//        // 5. 작성자 닉네임 조회
//        Map<Long, String> postCreators = load


        //  6. 게시글 썸네일 이미지 조회
        Map<Long, String> thumbnailImages = loadAttachmentPort.findFirstImageByPostIds(postIds);

        // 7. PostSummary 리스트 생성
        List<PostSummary> postSummaries = posts.stream()
                .map(post -> PostSummary.of(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        likeCounts.getOrDefault(post.getId(), 0L).intValue(),
                        commentCounts.getOrDefault(post.getId(), 0L).intValue(),
                        post.getBaseInfo().getCreatedAt(),
//                        post.getSpaceMemberId(),
                        thumbnailImages.getOrDefault(post.getId(), null)
                )).toList();

        // 8. ListOfPostSummary 생성
        return ListOfPostSummary.of(postSummaries);
    }
}
