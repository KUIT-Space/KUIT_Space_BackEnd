package space.space_spring.domain.spaceMember.application.port.out;

public interface LoadSpaceMemberInfoPort {

    NicknameAndProfileImage loadNicknameAndProfileImageById(Long spaceMemberId);
}
