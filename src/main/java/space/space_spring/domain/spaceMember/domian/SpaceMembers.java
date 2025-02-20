
package space.space_spring.domain.spaceMember.domian;


import java.util.List;
import java.util.stream.Stream;

public class SpaceMembers {

    private List<SpaceMember> spaceMembers;

    private SpaceMembers(List<SpaceMember> spaceMembers) {
        validateMembersInSameSpace(spaceMembers);           // 생성할 때 유효성 검증
        this.spaceMembers = spaceMembers;
    }

    public static SpaceMembers of(List<SpaceMember> spaceMembers) {
        return new SpaceMembers(spaceMembers);
    }

    /**
     * 멤버 변수인 List<SpaceMember> 가 모두 같은 Space에 속해있는지 검증하는 메서드
     */

    private void validateMembersInSameSpace(List<SpaceMember> spaceMembers) {
        Long spaceId = spaceMembers.get(0).getSpaceId();

        for (SpaceMember member : spaceMembers) {
            if (!member.getSpaceId().equals(spaceId)) {
                throw new IllegalArgumentException("[ERROR] : members are not in same space");
            }
        }
    }

    public Stream<SpaceMember> toStream(){
        return this.spaceMembers.stream();
    }
}
