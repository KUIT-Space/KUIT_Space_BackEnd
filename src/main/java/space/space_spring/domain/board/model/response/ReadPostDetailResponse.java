package space.space_spring.domain.board.model.response;

import lombok.*;
import space.space_spring.domain.board.model.entity.Post;
import space.space_spring.domain.board.model.entity.PostImage;
import space.space_spring.domain.userSpace.model.entity.UserSpace;
import space.space_spring.util.post.ConvertCreatedDate;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadPostDetailResponse {
    // 스페이스 관련
    private Long spaceId;

    // 유저 관련
    private Long userId;
    private String userProfileImg;
    private String userName;

    // 게시글 관련
    private Long postId;
    private String title;
    private String content;
    private List<String> postImage;
    private String time;
    private String type;

    // 댓글 및 좋아요
    private int commentCount;
    private List<ReadCommentsResponse> Comments;
    private int likeCount;
    private boolean isLike;

    public static ReadPostDetailResponse of(Post post, UserSpace userSpace, boolean isLike, List<ReadCommentsResponse> comments) {
        List<String> postImageUrls = post.getPostImages().stream()
                .map(PostImage::getPostImgUrl)
                .toList();

        return ReadPostDetailResponse.builder()
                .postId(post.getPostId())
                .spaceId(post.getSpace().getSpaceId())
                .userId(post.getUser().getUserId())
                .userProfileImg(userSpace != null ? userSpace.getUserProfileImg() : null)
                .userName(userSpace != null ? userSpace.getUserName() : null)
                .title(post.getTitle())
                .content(post.getContent())
                .time(ConvertCreatedDate.setCreatedDate(post.getCreatedAt()))
                .type(post.getType())
                .commentCount(post.getComments().size())
                .Comments(comments)
                .likeCount(post.getLikeCount())
                .isLike(isLike)
                .postImage(postImageUrls)
                .build();
    }

}
