package space.space_spring.dto.post.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.entity.Post;
import space.space_spring.entity.PostImage;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    private String title;
    private String content;
    private String type; // "notice" or "general"
    private List<MultipartFile> postImages;

    public Post toEntity(User user, Space space, List<PostImage> postImages){
        return Post.builder()
                .user(user)
                .space(space)
                .title(title)
                .content(content)
                .type(type)
                .postImages(postImages)
                .build();
    }
}


