package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.PostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.ReadPostListUseCase;
import space.space_spring.domain.post.application.port.out.LoadAttachmentPort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.application.port.out.LoadCommentPort;
import space.space_spring.domain.post.application.port.out.like.LoadLikePort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberInfoPort;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TAG_IS_REQUIRED_FOR_THIS_BOARD;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReadPostListService implements ReadPostListUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final LoadLikePort loadLikePort;
    private final LoadCommentPort loadCommentPort;
    private final LoadAttachmentPort loadAttachmentPort;
    private final LoadSpaceMemberInfoPort loadSpaceMemberInfoPort;

    @Override
    public ListOfPostSummary readPostList(Long spaceMemberId, Long boardId, Long tagId, Pageable pageable) {
        // 1. Board 조회
        Board board = loadBoardPort.loadById(boardId);

        // 2. 태그 검증
        if ((board.getBoardType() == BoardType.QUESTION || board.getBoardType() == BoardType.TIP) && tagId == null) {
            throw new CustomException(TAG_IS_REQUIRED_FOR_THIS_BOARD);
        }

        // 3. 태그 필터링
        Page<Post> postPage = tagId != null
                ? loadPostPort.loadPostListByTagId(tagId, pageable)
                : loadPostPort.loadPostListByBoardId(boardId, pageable);

        List<Post> posts = postPage.getContent();

        // 4. postId 리스트 추출(postId = postBaseId)
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        // 5. 좋아요 수 조회
        Map<Long, NaturalNumber> likeCounts = loadLikePort.countLikesByPostIds(postIds);

        // 6. 댓글 수 조회
        Map<Long, NaturalNumber> commentCounts = loadCommentPort.countCommentsByPostIds(postIds);

        // 7. 작성자 닉네임 조회
        Map<Long, String> creatorNicknames = loadSpaceMemberInfoPort.loadNicknamesByIds(posts.stream().map(Post::getPostCreatorId).toList());

        // 8. 게시글 썸네일 이미지 조회
        Map<Long, String> thumbnailImages = loadAttachmentPort.findFirstImageByPostIds(postIds);

        // 9. PostSummary 리스트 생성
        List<PostSummary> postSummaries = posts.stream()
                .map(post -> PostSummary.of(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        likeCounts.getOrDefault(post.getId(), NaturalNumber.of(0)).getNumber(),
                        commentCounts.getOrDefault(post.getId(), NaturalNumber.of(0)).getNumber(),
                        post.getBaseInfo().getCreatedAt(),
                        creatorNicknames.getOrDefault(post.getPostCreatorId(), "알 수 없음"),
                        thumbnailImages.getOrDefault(post.getId(), null),
                        post.getPostCreatorId().equals(spaceMemberId)
                )).toList();

        // 9. ListOfPostSummary 생성
        return ListOfPostSummary.of(postPage, postSummaries);
    }
}
