
package space.space_spring.domain.spaceMember.domian;


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
        Long spaceId = spaceMembers.get(0).getSpaceId();

        for (SpaceMember member : spaceMembers) {
            if (!member.getSpaceId().equals(spaceId)) {
                throw new IllegalStateException("[ERROR] : members are not in same space");
            }
        }
    }
}
