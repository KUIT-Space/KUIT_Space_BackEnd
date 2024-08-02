package space.space_spring.dto.post;

import lombok.*;
import space.space_spring.entity.Post;
import space.space_spring.entity.PostImage;

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

    public static ReadPostsResponse from(Post post) {
        List<String> postImageUrls = post.getPostImages().stream()
                .map(PostImage::getPostImgUrl)
                .toList();

        return ReadPostsResponse.builder()
                .postId(post.getPostId())
                .spaceId(post.getSpace().getSpaceId())
                .title(post.getTitle())
                .content(post.getContent())
                .type(post.getType())
                .like_count(post.getLike())
                .postImage(postImageUrls)
                .build();
    }
}
