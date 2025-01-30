package space.space_spring.domain.spaceMember;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SpaceMembersTest {

    private Space kuit;
    private Space alcon;

    private SpaceMember seongjun;
    private SpaceMember sangjun;
    private SpaceMember seohyun;
    private SpaceMember kyeongmin;
    private SpaceMember jihwan;

    @BeforeEach
    void setUp() {
        User commonUser = User.create(1L, 1L);          // User 도메인 엔티티는 그냥 하나로 공유해서 테스트 진행
        kuit = Space.create(1L, "쿠잇", 1L);
        alcon = Space.create(2L, "알콘", 2L);

        seongjun = SpaceMember.create(1L, kuit, commonUser, 1L);
        sangjun = SpaceMember.create(2L, kuit, commonUser, 2L);
        seohyun = SpaceMember.create(3L, kuit, commonUser, 3L);
        kyeongmin = SpaceMember.create(4L, kuit, commonUser, 4L);
        jihwan = SpaceMember.create(5L, alcon, commonUser, 5L);
    }

    @Test
    @DisplayName("List<SpaceMember>의 SpaceMember가 모두 같은 Space에 속해있는지 검증한다.")
    void validateMembersInSameSpace1() throws Exception {
        //given
        SpaceMembers spaceMembers = SpaceMembers.of(List.of(seongjun, sangjun, seohyun, kyeongmin));

        //when //then
        assertDoesNotThrow(spaceMembers::validateMembersInSameSpace);
    }

    @Test
    @DisplayName("List<SpaceMember>의 SpaceMember가 모두 같은 Space에 속해있지 않으면, IllegalStateException 을 던진다.")
    void validateMembersInSameSpace2() throws Exception {
        //given
        SpaceMembers spaceMembers = SpaceMembers.of(List.of(seongjun, sangjun, seohyun, kyeongmin, jihwan));

        //when //then
        assertThatThrownBy(spaceMembers::validateMembersInSameSpace)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("[ERROR] : members are not in same space");
    }

}