package space.space_spring.domain.spaceMember.application.port.out;

import space.space_spring.domain.spaceMember.domian.SpaceMember;

import java.util.List;

public interface LoadSpaceMemberPort {
    SpaceMember loadById(Long id);

    List<SpaceMember> loadAllById(List<Long> ids);
    SpaceMember loadSpaceMemberById(Long id);
    List<SpaceMember> loadSpaceMemberBySpaceId(Long spaceId);

    SpaceMember loadByDiscordId(Long discordId);
}
