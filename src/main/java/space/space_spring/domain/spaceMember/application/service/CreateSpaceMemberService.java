package space.space_spring.domain.spaceMember.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.spaceMember.application.port.in.CreateSpaceMemberUseCase;
import space.space_spring.domain.spaceMember.application.port.out.CreateSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
@Service
@RequiredArgsConstructor
public class CreateSpaceMemberService implements CreateSpaceMemberUseCase {

    private final CreateSpaceMemberPort createSpaceMemberPort;
    @Override
    public SpaceMember create(SpaceMember spaceMember){
        return createSpaceMemberPort.createSpaceMember(spaceMember);
    }
}
