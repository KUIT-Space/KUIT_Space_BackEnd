package space.space_spring.domain.spaceMember;

import lombok.Getter;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.user.User;

@Getter
public class SpaceMember {

    private Long id;

    private Space space;            // 소속된 Space -> 이거도 도메인 엔티티로 ??

    private User user;              // spaceMember의 본캐 유저

    private Long discordId;



}
