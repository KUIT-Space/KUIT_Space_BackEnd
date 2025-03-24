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
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.post.ConvertCreatedDate;

import java.lang.management.ManagementPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadPostDetailService implements ReadPostDetailUseCase {

    private static final String ANONYMOUS_COMMENT_CREATOR_NICKNAME = "익명 스페이서";
    private static final String INACTIVE_COMMENT_CONTENT = "삭제된 댓글입니다.";

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

        // 4. 해당 게시글에 달린 댓글 정보 조회
        List<CommentDetailView> rawCommentDetailViews = loadCommentDetailPort.loadCommentDetail(post.getId(), command.getSpaceMemberId());

        // 5. 후처리: 익명 댓글 및 삭제된 댓글 처리
        List<CommentDetailView> processedCommentDetailViews = new ArrayList<>();
        Map<Long, String> anonymousNicknameMap = new HashMap<>();
        int anonymousCount = 1;
        for (CommentDetailView view : rawCommentDetailViews) {
            if (!view.getIsActiveComment()) { // 삭제된 댓글인 경우
                processedCommentDetailViews.add(CommentDetailView.builder()
                        .creatorId(view.getCreatorId())
                        .creatorName(null)
                        .creatorProfileImageUrl(null)
                        .isPostOwner(view.getIsPostOwner())
                        .content(INACTIVE_COMMENT_CONTENT)
                        .createdAt(view.getCreatedAt())
                        .lastModifiedAt(view.getLastModifiedAt())
                        .likeCount(view.getLikeCount())
                        .isLiked(view.getIsLiked())
                        .isActiveComment(view.getIsActiveComment())
                        .isAnonymousComment(view.getIsAnonymousComment())
                        .build());
            } else if (view.getIsAnonymousComment()) { // 익명 댓글인 경우
                Long creatorId = view.getCreatorId();
                String nickname;
                if (anonymousNicknameMap.containsKey(creatorId)) {
                    nickname = anonymousNicknameMap.get(creatorId);
                } else {
                    nickname = ANONYMOUS_COMMENT_CREATOR_NICKNAME + " " + anonymousCount;
                    anonymousNicknameMap.put(creatorId, nickname);
                    anonymousCount++;
                }

                processedCommentDetailViews.add(CommentDetailView.builder()
                        .creatorId(view.getCreatorId())
                        .creatorName(nickname)
                        .creatorProfileImageUrl(null)
                        .isPostOwner(view.getIsPostOwner())
                        .content(view.getContent())
                        .createdAt(view.getCreatedAt())
                        .lastModifiedAt(view.getLastModifiedAt())
                        .likeCount(view.getLikeCount())
                        .isLiked(view.getIsLiked())
                        .isActiveComment(view.getIsActiveComment())
                        .isAnonymousComment(view.getIsAnonymousComment())
                        .build());
            } else { // 정상 댓글은 그대로 추가
                processedCommentDetailViews.add(view);
            }
        }

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
                .infoOfCommentDetails(processedCommentDetailViews.stream()
                        .map(commentDetailView -> InfoOfCommentDetail.builder()
                                .creatorName(commentDetailView.getCreatorName())
                                .creatorProfileImageUrl(commentDetailView.getCreatorProfileImageUrl())
                                .isPostOwner(commentDetailView.getIsPostOwner())
                                .content(commentDetailView.getContent())
                                .createdAt(ConvertCreatedDate.setCreatedDate(commentDetailView.getCreatedAt()))
                                .lastModifiedAt(ConvertCreatedDate.setCreatedDate(commentDetailView.getLastModifiedAt()))
                                .likeCount(commentDetailView.getLikeCount().intValue())
                                .isLiked(commentDetailView.getIsLiked())
                                .isActiveComment(commentDetailView.getIsActiveComment())
                                .build())
                        .toList())
                .build();
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
