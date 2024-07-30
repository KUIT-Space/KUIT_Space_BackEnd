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

    private User user;

    private Space space;

    private String title;

    private String content;

    private String type;

    private int like;

}
