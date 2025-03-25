package space.space_spring.domain.user.adapter.in.web;

import lombok.Getter;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

@Getter
public class SpaceInfo {

    private Long id;

    private String name;

    private Boolean isManager;

    private SpaceInfo(Space space, SpaceMember spaceMember) {
        this.id = space.getId();
        this.name = space.getName();
        this.isManager = spaceMember.isManager();
    }

    public static SpaceInfo create(Space space, SpaceMember spaceMember) {
        return new SpaceInfo(space, spaceMember);
    }
}
