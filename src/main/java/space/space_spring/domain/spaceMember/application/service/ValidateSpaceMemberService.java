package space.space_spring.domain.spaceMember.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.spaceMember.application.port.in.ValidateSpaceMemberUseCase;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidateSpaceMemberService implements ValidateSpaceMemberUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;

    @Override
    public void validateSpaceMembersInSameSpace(List<Long> spaceMemberIds) {
        List<SpaceMember> allOfSpaceMember = loadSpaceMemberPort.loadAllById(spaceMemberIds);

        SpaceMembers.of(allOfSpaceMember);

    }
}
