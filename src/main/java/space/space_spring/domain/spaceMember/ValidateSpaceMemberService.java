package space.space_spring.domain.spaceMember;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidateSpaceMemberService implements ValidateSpaceMemberUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;

    @Override
    public void validateSpaceMembersInSameSpace(List<Long> spaceMemberIds) {
        List<SpaceMember> allOfSpaceMember = loadSpaceMemberPort.loadAllById(spaceMemberIds);

        SpaceMembers.of(allOfSpaceMember);

//        try {
//            spaceMembers.validateMembersInSameSpace();
//        } catch (IllegalStateException e) {
//            throw new CustomException(PAY_CREATOR_AND_TARGETS_ARE_NOT_IN_SAME_SPACE);           // 이 방식 괜찮은지 ??
//        }
    }
}
