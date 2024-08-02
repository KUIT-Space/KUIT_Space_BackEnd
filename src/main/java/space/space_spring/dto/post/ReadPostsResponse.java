package space.space_spring.dto.post;

import lombok.*;

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

}
