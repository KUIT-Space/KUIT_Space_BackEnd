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
import space.space_spring.domain.post.application.port.out.like.LoadLikePort;
import space.space_spring.domain.post.application.port.out.post.PostDetailQueryPort;
import space.space_spring.domain.post.application.port.out.post.PostDetailView;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberInfoPort;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;
import space.space_spring.global.util.post.ConvertCreatedDate;

import java.util.List;
import java.util.Map;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadPostDetailService implements ReadPostDetailUseCase {

    private final PostDetailQueryPort loadPostDetailPort;
    private final CommentDetailQueryPort loadCommentDetailPort;

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
//    private final LoadCommentPort loadCommentPort;
//    private final LoadLikePort loadLikePort;
//    private final LoadSpaceMemberInfoPort loadSpaceMemberInfoPort;
//    private final LoadAttachmentPort loadAttachmentPort;

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
        List<CommentDetailView> commentDetailViews = loadCommentDetailPort.loadCommentDetail(post.getId(), command.getSpaceMemberId());

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
                .infoOfCommentDetails(commentDetailViews.stream()
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



//        // 3. comment 조회 -> status 상관없이 모든 댓글들 조회
//        List<Comment> comments = loadCommentPort.loadByPostIdWithoutStatusFilter(post.getId());
//
//        // 4. post, comment 들의 좋아요 개수 계산
//        NaturalNumber postLikeCount = loadLikePort.countLikeByPostId(post.getId());
//        Map<Long, NaturalNumber> commentLikeCountMap = loadLikePort.countLikesByPostIds(comments.stream().map(Comment::getId).toList());
//
//        // 5. 댓글 상세 정보 생성
//        List<InfoOfCommentDetail> infoOfCommentDetails = comments.stream()
//                .map(comment -> buildCommentDetail(comment, post, command, commentLikeCountMap))
//                .toList();
//
//        // 6. return
//        NicknameAndProfileImage postCreator = loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(post.getPostCreatorId());
//        List<String> attachmentUrls = loadAttachmentPort.loadAttachmentUrlByTargetId(post.getId());
//        boolean hasSpaceMemberLikedToPost = loadLikePort.hasSpaceMemberLiked(command.getSpaceMemberId(), post.getId());
//
//        String postCreatorNickname = post.getIsAnonymous() ? "익명 스페이서" : postCreator.getNickname();
//
//        return ResultOfReadPostDetail.builder()
//                .creatorName(postCreatorNickname)
//                .creatorProfileImageUrl(postCreator.getProfileImageUrl())
//                .createdAt(ConvertCreatedDate.setCreatedDate(post.getBaseInfo().getCreatedAt()))
//                .lastModifiedAt(ConvertCreatedDate.setCreatedDate(post.getBaseInfo().getLastModifiedAt()))
//                .title(post.getTitle())
//                .content(post.getContent())
//                .attachmentUrls(attachmentUrls)
//                .likeCount(postLikeCount)
//                .isLiked(hasSpaceMemberLikedToPost)
//                .infoOfCommentDetails(infoOfCommentDetails)
//                .build();
    }

    private void validate(Board board, Post post, ReadPostDetailCommand command) {
        if (!board.isInSpace(command.getSpaceId())) {       // board가 스페이스에 속하는지 검증
            throw new CustomException(BOARD_IS_NOT_IN_SPACE);
        }

        if (!post.isInBoard(board.getId())) {       // post가 보드에 속하는지 검증
            throw new CustomException(POST_IS_NOT_IN_BOARD);
        }
    }
//
//    private InfoOfCommentDetail buildCommentDetail(Comment comment, Post post, ReadPostDetailCommand command, Map<Long, NaturalNumber> commentLikeCountMap) {
//        // 댓글 작성자 정보 조회
//        NicknameAndProfileImage commentCreator = loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(comment.getCommentCreatorId());
//        String creatorNickname = commentCreator.getNickname();
//        String creatorProfileImageUrl = commentCreator.getProfileImageUrl();
//
//        // 익명 댓글인 경우 이름 재정의
//        // TODO : 익명인 댓글 작성자의 네이밍 알고리즘 개선
//        if (comment.getIsAnonymous()) {
//            creatorNickname = "익명 스페이서 " + (post.getId() * 10) + comment.getCommentCreatorId();
//            creatorProfileImageUrl = null;      // 프론트에서 익명 디폴트 이미지 보여주기?? 아니면 s3에 익명 디폴트 이미지 저장 & 로드?
//        }
//
//        // 댓글이 비활성화된 경우 내용 수정
//        boolean isActiveComment = comment.getBaseInfo().isActive();
//        if (!isActiveComment) {
//            comment.changeContent("삭제된 댓글입니다.");
//            creatorNickname = null;
//            creatorProfileImageUrl = null;
//        }
//
//        boolean hasSpaceMemberLikedToComment = loadLikePort.hasSpaceMemberLiked(command.getSpaceMemberId(), comment.getId());
//        boolean isPostOwner = post.isPostCreator(comment.getCommentCreatorId());
//
//        return InfoOfCommentDetail.builder()
//                .creatorName(creatorNickname)
//                .creatorProfileImageUrl(creatorProfileImageUrl)
//                .isPostOwner(isPostOwner)
//                .content(comment.getContent())
//                .createdAt(ConvertCreatedDate.setCreatedDate(comment.getBaseInfo().getCreatedAt()))
//                .lastModifiedAt(ConvertCreatedDate.setCreatedDate(comment.getBaseInfo().getLastModifiedAt()))
//                .likeCount(commentLikeCountMap.getOrDefault(comment.getId(), NaturalNumber.of(0)))
//                .isLiked(hasSpaceMemberLikedToComment)
//                .isActiveComment(isActiveComment)
//                .build();
//    }
}
