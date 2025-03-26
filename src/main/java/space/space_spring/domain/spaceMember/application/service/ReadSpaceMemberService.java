package space.space_spring.domain.spaceMember.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.spaceMember.application.port.in.ReadSpaceMemberUseCase;
import space.space_spring.domain.spaceMember.application.port.out.query.SpaceMemberDetail;
import space.space_spring.domain.spaceMember.application.port.out.query.SpaceMemberQueryPort;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadSpaceMemberService implements ReadSpaceMemberUseCase {

    private final SpaceMemberQueryPort spaceMemberQueryPort;

    @Override
    public List<SpaceMemberDetail> readAllSpaceMembers(Long spaceId) {
        return spaceMemberQueryPort.loadSpaceMemberDetails(spaceId);
    }
}
