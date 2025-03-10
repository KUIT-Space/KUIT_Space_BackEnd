package space.space_spring.domain.user.adapter.in.web;

import lombok.Getter;
import space.space_spring.domain.space.domain.Space;

@Getter
public class SpaceInfo {

    private Long id;

    private String name;

    private SpaceInfo(Space space) {
        this.id = space.getId();
        this.name = space.getName();
    }

    public static SpaceInfo create(Space space) {
        return new SpaceInfo(space);
    }
}
