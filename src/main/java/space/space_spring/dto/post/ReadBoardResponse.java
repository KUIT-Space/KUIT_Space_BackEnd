package space.space_spring.dto.post;

import lombok.*;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadBoardResponse {

    private Long postId;

    private Long userId;

    private Long spaceId;

    private String title;

    private String content;

    private String type;

    private int like;

}
