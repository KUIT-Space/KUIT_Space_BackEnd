package space.space_spring.domain.spaceMember;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_CREATOR_AND_TARGETS_ARE_NOT_IN_SAME_SPACE;

@Service
@RequiredArgsConstructor
public class ValidateSpaceMemberService implements ValidateSpaceMemberUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;

    @Override
    public void validateSpaceMembersInSameSpace(List<Long> spaceMemberIds) {
        List<SpaceMember> allOfSpaceMember = new ArrayList<>();
        for (Long spaceMemberId : spaceMemberIds) {
            allOfSpaceMember.add(loadSpaceMemberPort.loadById(spaceMemberId));
        }

        SpaceMembers spaceMembers = SpaceMembers.of(allOfSpaceMember);
        try {
            spaceMembers.validateMembersInSameSpace();
        } catch (IllegalArgumentException e) {
            throw new CustomException(PAY_CREATOR_AND_TARGETS_ARE_NOT_IN_SAME_SPACE);           // 이 방식 괜찮은지 ??
        }
    }
}
