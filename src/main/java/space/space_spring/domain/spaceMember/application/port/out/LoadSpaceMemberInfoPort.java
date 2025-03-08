package space.space_spring.domain.spaceMember.application.port.out;

import java.util.List;
import java.util.Map;

public interface LoadSpaceMemberInfoPort {

    NicknameAndProfileImage loadNicknameAndProfileImageById(Long spaceMemberId);

    Map<Long, String> loadNicknamesByIds(List<Long> spaceMemberIds);
}
