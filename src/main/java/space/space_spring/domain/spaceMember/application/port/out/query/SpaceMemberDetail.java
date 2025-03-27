package space.space_spring.domain.spaceMember.application.port.out.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpaceMemberDetail {

    private Long spaceMemberId;

    private String nickname;

    private String profileImageUrl;

    private Boolean isManager;

    // QueryDsl을 위한 생성자
    public SpaceMemberDetail(Long spaceMemberId, String nickname, String profileImageUrl, Boolean isManager) {
        this.spaceMemberId = spaceMemberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.isManager = isManager;
    }
}
