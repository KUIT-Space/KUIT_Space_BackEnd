package space.space_spring.domain.spaceMember;

import space.space_spring.global.exception.CustomException;

import java.util.List;

public class SpaceMembers {

    private List<SpaceMember> spaceMembers;

    private SpaceMembers(List<SpaceMember> spaceMembers) {
        this.spaceMembers = spaceMembers;
    }

    public static SpaceMembers of(List<SpaceMember> spaceMembers) {
        return new SpaceMembers(spaceMembers);
    }

    /**
     * 멤버 변수인 List<SpaceMember> 가 모두 같은 Space에 속해있는지 검증하는 메서드
     */
    public void validateMembersInSameSpace() {
        Long spaceId = spaceMembers.get(0).getSpace().getId();

        for (SpaceMember member : spaceMembers) {
            if (!member.getSpace().getId().equals(spaceId)) {
                throw new IllegalStateException("[ERROR] : members is not in same space");
            }
        }
    }
}
