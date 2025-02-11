package space.space_spring.domain.post.domain;

import lombok.Getter;
import space.space_spring.domain.spaceMember.SpaceMember;

@Getter
public class Like {

    private Long id;

    private PostBase postBaseId;

    private SpaceMember spaceMemberId;
}
