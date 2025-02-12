package space.space_spring.domain.spaceMember;

public interface LoadSpaceMemberInfoPort {

    NicknameAndProfileImage loadNicknameAndProfileImageById(Long spaceMemberId);
}
