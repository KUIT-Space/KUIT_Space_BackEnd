package space.space_spring.dto.post;

import lombok.*;
import space.space_spring.entity.Post;
import space.space_spring.entity.PostImage;
import space.space_spring.entity.UserSpace;
import space.space_spring.util.post.ConvertCreatedDate;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadPostsResponse {

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
    private int post_count;

    // 댓글 및 좋아요
    private int comment_count;
    private int like_count;

    public static ReadPostsResponse of(Post post, int postCount, UserSpace userSpace) {
        List<String> postImageUrls = post.getPostImages().stream()
                .map(PostImage::getPostImgUrl)
                .toList();

        return ReadPostsResponse.builder()
                .postId(post.getPostId())
                .spaceId(post.getSpace().getSpaceId())
                .userId(post.getUser().getUserId())
                .userProfileImg(userSpace != null ? userSpace.getUserProfileImg() : null)
                .userName(userSpace != null ? userSpace.getUserName() : null)
                .title(post.getTitle())
                .content(post.getContent())
                .time(ConvertCreatedDate.setCreatedDate(post.getCreatedAt()))
                .type(post.getType())
                .post_count(postCount)
                .comment_count(post.getPostComments().size())
                .like_count(post.getLike())
                .postImage(postImageUrls)
                .build();
    }
}
