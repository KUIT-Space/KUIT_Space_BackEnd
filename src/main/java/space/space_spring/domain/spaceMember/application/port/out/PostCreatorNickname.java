package space.space_spring.domain.spaceMember.application.port.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreatorNickname {

    private Long spaceMemberId;

    private String nickname;
}
