package space.space_spring.domain.spaceMember;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.user.domain.User;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.domain.spaceMember.domian.SpaceMembers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SpaceMembersTest {

    /**
     * 바뀐 SpaceMember 도메인 엔티티 코드와 충돌이 발생해 컴파일 에러가 날 거 같은데,
     * 이 코드 전부 주석처리 해주시면 됩니닷
     */

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

        seongjun = SpaceMember.create(1L, kuit, commonUser, 1L, "노성준", "image_111", true);
        sangjun = SpaceMember.create(2L, kuit, commonUser, 2L, "개구리비안", "image_222", false);
        seohyun = SpaceMember.create(3L, kuit, commonUser, 3L, "정서현", "image_333", false);
        kyeongmin = SpaceMember.create(4L, kuit, commonUser, 4L, "김경민", "image_444", false);
        jihwan = SpaceMember.create(5L, alcon, commonUser, 5L, "김지환", "image_555", false);
    }

    @Test
    @DisplayName("List<SpaceMember>의 SpaceMember가 모두 같은 Space에 속해있는지 검증한다.")
    void validateMembersInSameSpace1() throws Exception {
        //given
        List<SpaceMember> validSpaceMembers = List.of(seongjun, sangjun, seohyun, kyeongmin);

        //when //then
        assertDoesNotThrow(() -> SpaceMembers.of(validSpaceMembers));
    }

    @Test
    @DisplayName("List<SpaceMember>의 SpaceMember가 모두 같은 Space에 속해있지 않으면, IllegalStateException 을 던진다.")
    void validateMembersInSameSpace2() throws Exception {
        //given
        List<SpaceMember> inValidSpaceMembers = List.of(seongjun, sangjun, seohyun, kyeongmin, jihwan);

        //when //then
        assertThatThrownBy(() -> SpaceMembers.of(inValidSpaceMembers))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] : members are not in same space");
    }

}