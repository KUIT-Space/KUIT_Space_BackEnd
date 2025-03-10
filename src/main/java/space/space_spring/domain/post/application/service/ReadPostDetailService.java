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

import java.util.ArrayList;
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

        // 5. return
        // 익명인 경우 작성자 네이밍 변경 -> 익명 스페이서 + PK
        // 또한 comment의 status 가 INACTIVE 일 경우 댓글 내용을 삭제된 댓글입니다. 로 수정 -> 이거 다시 한번 확인?? (뭔가 지우는게 나을수도 ??)
        // 추가로 게시글 작성자와 댓글 작성자가 같은지도 확인
        List<InfoOfCommentDetail> infoOfCommentDetails = new ArrayList<>();
        for (Comment comment : comments) {
            if (!comment.getBaseInfo().isActive()) {
                // TODO : comment 내용 수정 -> "삭제된 댓글입니다."
            }

            NicknameAndProfileImage commentCreator = loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(comment.getCommentCreatorId());
            boolean hasSpaceMemberLikedToComment = loadLikePort.hasSpaceMemberLiked(command.getSpaceMemberId(), comment.getId());

            boolean isPostOwner;
            if (post.isPostCreator(comment.getCommentCreatorId())) isPostOwner = true;
            else isPostOwner = false;

            InfoOfCommentDetail infoOfCommentDetail = InfoOfCommentDetail.builder()
                    .creatorName(commentCreator.getNickname())          // TODO : 익명 댓글인 경우 네이밍 수정
                    .creatorProfileImageUrl(commentCreator.getProfileImageUrl())
                    .isPostOwner(isPostOwner)
                    .content(comment.getContent())
                    .createdAt(ConvertCreatedDate.setCreatedDate(comment.getBaseInfo().getCreatedAt()))
                    .lastModifiedAt(ConvertCreatedDate.setCreatedDate(comment.getBaseInfo().getLastModifiedAt()))
                    .likeCount(commentLikeCountMap.get(comment.getId()))
                    .isLiked(hasSpaceMemberLikedToComment)
                    .build();

            infoOfCommentDetails.add(infoOfCommentDetail);
        }

        NicknameAndProfileImage postCreator = loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(post.getPostCreatorId());
        List<String> attachmentUrls = loadAttachmentPort.loadAttachmentUrlByTargetId(post.getId());
        boolean hasSpaceMemberLikedToPost = loadLikePort.hasSpaceMemberLiked(command.getSpaceMemberId(), post.getId());

        return ResultOfReadPostDetail.builder()
                .creatorName(postCreator.getNickname())         // TODO : 익명 게시글인 경우 네이밍 수정
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
}
