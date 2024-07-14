package space.space_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUserSignupResponse {

    private Long userId;
    private String jwt;         // 이게 있어야할까?
}
