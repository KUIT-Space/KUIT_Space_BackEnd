package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.readPostDetail.InfoOfCommentDetail;
import space.space_spring.domain.post.application.port.in.readPostDetail.ReadPostDetailCommand;
import space.space_spring.domain.post.application.port.in.readPostDetail.ReadPostDetailUseCase;
import space.space_spring.domain.post.application.port.in.readPostDetail.ResultOfReadPostDetail;
import space.space_spring.domain.post.application.port.out.*;
import space.space_spring.domain.post.application.port.out.comment.CommentDetailQueryPort;
import space.space_spring.domain.post.application.port.out.comment.CommentDetailView;
import space.space_spring.domain.post.application.port.out.post.PostDetailQueryPort;
import space.space_spring.domain.post.application.port.out.post.PostDetailView;
import space.space_spring.domain.post.application.service.strategy.AnonymousCommentProcessingStrategy;
import space.space_spring.domain.post.application.service.strategy.CommentDetailProcessingStrategy;
import space.space_spring.domain.post.application.service.strategy.InactiveCommentProcessingStrategy;
import space.space_spring.domain.post.application.service.strategy.NormalCommentProcessingStrategy;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.post.ConvertCreatedDate;

import java.util.ArrayList;
import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadPostDetailService implements ReadPostDetailUseCase {

    private final PostDetailQueryPort loadPostDetailPort;
    private final CommentDetailQueryPort loadCommentDetailPort;
    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;

    @Override
    public ResultOfReadPostDetail readPostDetail(ReadPostDetailCommand command) {
        // 1. board, post 조회
        Board board = loadBoardPort.loadById(command.getBoardId());
        Post post = loadPostPort.loadById(command.getPostId());

        // 2. validation
        validate(board, post, command);

        // 3. 해당 게시글의 상세 정보 조회
        PostDetailView postDetailView = loadPostDetailPort.loadPostDetail(post.getId(), command.getSpaceMemberId());

        // 4. 해당 게시글에 달린 댓글 정보 조회 및 후처리
        List<CommentDetailView> rawCommentDetailViews = loadCommentDetailPort.loadCommentDetail(post.getId(), command.getSpaceMemberId());
        List<CommentDetailView> processedCommentDetailViews = processCommentDetails(rawCommentDetailViews);

        // 5. return
        return ResultOfReadPostDetail.builder()
                .creatorName(postDetailView.getCreatorName())
                .creatorProfileImageUrl(postDetailView.getCreatorProfileImageUrl())
                .createdAt(ConvertCreatedDate.setCreatedDate(postDetailView.getCreatedAt()))
                .lastModifiedAt(ConvertCreatedDate.setCreatedDate(postDetailView.getLastModifiedAt()))
                .title(postDetailView.getTitle())
                .content(postDetailView.getContent())
                .attachmentUrls(postDetailView.getAttachmentUrls())
                .likeCount(postDetailView.getLikeCount().intValue())
                .isLiked(postDetailView.getIsLiked())
                .infoOfCommentDetails(mapToInfoOfCommentDetails(processedCommentDetailViews))
                .build();
    }

    private List<InfoOfCommentDetail> mapToInfoOfCommentDetails(List<CommentDetailView> commentDetailViews) {
        List<InfoOfCommentDetail> result = new ArrayList<>();
        for (CommentDetailView comment : commentDetailViews) {
            result.add(InfoOfCommentDetail.builder()
                    .commentId(comment.getCommentId())
                    .creatorName(comment.getCreatorName())
                    .creatorProfileImageUrl(comment.getCreatorProfileImageUrl())
                    .isPostOwner(comment.getIsPostOwner())
                    .content(comment.getContent())
                    .createdAt(ConvertCreatedDate.setCreatedDate(comment.getCreatedAt()))
                    .lastModifiedAt(ConvertCreatedDate.setCreatedDate(comment.getLastModifiedAt()))
                    .likeCount(comment.getLikeCount().intValue())
                    .isLiked(comment.getIsLiked())
                    .isActiveComment(comment.getIsActiveComment())
                    .build());
        }
        return result;
    }

    private List<CommentDetailView> processCommentDetails(List<CommentDetailView> rawCommentDetailViews) {
        List<CommentDetailView> processed = new ArrayList<>();

        // 전략 리스트
        List<CommentDetailProcessingStrategy> strategies = List.of(
                new InactiveCommentProcessingStrategy(),
                new AnonymousCommentProcessingStrategy(),
                new NormalCommentProcessingStrategy()
        );

        for (CommentDetailView view : rawCommentDetailViews) {
            for (CommentDetailProcessingStrategy strategy : strategies) {
                if (strategy.supports(view)) {
                    processed.add(strategy.process(view));
                    break;
                }
            }
        }

        return processed;
    }

    private void validate(Board board, Post post, ReadPostDetailCommand command) {
        if (!board.isInSpace(command.getSpaceId())) {       // board가 스페이스에 속하는지 검증
            throw new CustomException(BOARD_IS_NOT_IN_SPACE);
        }

        if (!post.isInBoard(board.getId())) {       // post가 보드에 속하는지 검증
            throw new CustomException(POST_IS_NOT_IN_BOARD);
        }
    }
}
