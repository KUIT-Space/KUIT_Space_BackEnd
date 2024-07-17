package space.space_spring.dto.space;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpaceCreateDto {

    private Long spaceId;
    private String jwtUserSpace;
}
