package space.space_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUserResponse {

    private Long userId;
    private String jwt;
}
