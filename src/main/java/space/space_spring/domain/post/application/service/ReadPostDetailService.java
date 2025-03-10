package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.post.application.port.in.readPostDetail.InfoOfCommentDetail;
import space.space_spring.domain.post.application.port.in.readPostDetail.ReadPostDetailCommand;
import space.space_spring.domain.post.application.port.in.readPostDetail.ReadPostDetailUseCase;
import space.space_spring.domain.post.application.port.in.readPostDetail.ResultOfReadPostDetail;
import space.space_spring.domain.post.application.port.out.LoadAttachmentPort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.application.port.out.LoadCommentPort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.application.port.out.like.LoadLikePort;
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
public class ReadPostDetailService implements ReadPostDetailUseCase {

    private final LoadBoardPort loadBoardPort;
    private final LoadPostPort loadPostPort;
    private final LoadCommentPort loadCommentPort;
    private final LoadLikePort loadLikePort;
    private final LoadSpaceMemberInfoPort loadSpaceMemberInfoPort;
    private final LoadAttachmentPort loadAttachmentPort;

    @Override
    public ResultOfReadPostDetail readPostDetail(ReadPostDetailCommand command) {
        // 1. board, post 조회
        Board board = loadBoardPort.loadById(command.getBoardId());
        Post post = loadPostPort.loadById(command.getPostId());

        // 2. validation
        validate(board, post, command);

        // 3. comment 조회 -> status 상관없이 모든 댓글들 조회 -> 삭제된 댓글은
        List<Comment> comments = loadCommentPort.loadByPostIdWithoutStatusFilter(post.getId());

        // 4. post, comment 들의 좋아요 개수 계산
        // -> 이거 매번 db 조회 ??
        // 일단 경민이가 만들어둔 메서드 사용
        NaturalNumber postLikeCount = loadLikePort.countLikeByPostId(post.getId());
        Map<Long, NaturalNumber> commentLikeCountMap = loadLikePort.countLikesByPostIds(comments.stream().map(Comment::getId).toList());

        // 5. 댓글 상세 정보 생성
        List<InfoOfCommentDetail> infoOfCommentDetails = comments.stream()
                .map(comment -> buildCommentDetail(comment, post, command, commentLikeCountMap))
                .toList();

        // 6. return
        NicknameAndProfileImage postCreator = loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(post.getPostCreatorId());
        List<String> attachmentUrls = loadAttachmentPort.loadAttachmentUrlByTargetId(post.getId());
        boolean hasSpaceMemberLikedToPost = loadLikePort.hasSpaceMemberLiked(command.getSpaceMemberId(), post.getId());

        String postCreatorNickname = post.getIsAnonymous() ? "익명 스페이서" : postCreator.getNickname();

        return ResultOfReadPostDetail.builder()
                .creatorName(postCreatorNickname)
                .creatorProfileImageUrl(postCreator.getProfileImageUrl())
                .createdAt(ConvertCreatedDate.setCreatedDate(post.getBaseInfo().getCreatedAt()))
                .lastModifiedAt(ConvertCreatedDate.setCreatedDate(post.getBaseInfo().getLastModifiedAt()))
                .title(post.getTitle())
                .content(post.getContent())
                .attachmentUrls(attachmentUrls)
                .likeCount(postLikeCount)
                .isLiked(hasSpaceMemberLikedToPost)
                .infoOfCommentDetails(infoOfCommentDetails)
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

    private InfoOfCommentDetail buildCommentDetail(Comment comment, Post post, ReadPostDetailCommand command, Map<Long, NaturalNumber> commentLikeCountMap) {
        // 댓글이 비활성화된 경우 내용 수정
        if (!comment.getBaseInfo().isActive()) {
            comment.changeContent("삭제된 댓글입니다.");
        }

        // 댓글 작성자 정보 조회
        NicknameAndProfileImage commentCreator = loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(comment.getCommentCreatorId());
        boolean hasSpaceMemberLikedToComment = loadLikePort.hasSpaceMemberLiked(command.getSpaceMemberId(), comment.getId());
        boolean isPostOwner = post.isPostCreator(comment.getCommentCreatorId());

        // 익명 댓글인 경우 이름 재정의
        // TODO : 익명인 댓글 작성자의 네이밍 알고리즘 개선
        String creatorNickname = comment.getIsAnonymous() ?
                "익명 스페이서 " + (post.getId() * 10) + comment.getCommentCreatorId() :
                commentCreator.getNickname();

        return InfoOfCommentDetail.builder()
                .creatorName(creatorNickname)
                .creatorProfileImageUrl(commentCreator.getProfileImageUrl())
                .isPostOwner(isPostOwner)
                .content(comment.getContent())
                .createdAt(ConvertCreatedDate.setCreatedDate(comment.getBaseInfo().getCreatedAt()))
                .lastModifiedAt(ConvertCreatedDate.setCreatedDate(comment.getBaseInfo().getLastModifiedAt()))
                .likeCount(commentLikeCountMap.get(comment.getId()))
                .isLiked(hasSpaceMemberLikedToComment)
                .build();
    }
}
