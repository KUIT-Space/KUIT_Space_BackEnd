package space.space_spring.domain.spaceMember.application.port.out;

import java.util.Optional;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

import java.util.List;

public interface LoadSpaceMemberPort {
    SpaceMember loadById(Long id);
    List<SpaceMember> loadByUserId(Long userId);

    List<SpaceMember> loadAllById(List<Long> ids);
    SpaceMember loadSpaceMemberById(Long id);
    List<SpaceMember> loadSpaceMemberBySpaceId(Long spaceId);

    SpaceMember loadByDiscord(Long spaceDiscordId , Long spaceMemberDiscordId);

    Optional<SpaceMember> loadDefaultSpaceMember(Long userId, String defaultSpaceName);

    List<SpaceMember> loadAllByIdInOrder(List<Long> ids);
}
