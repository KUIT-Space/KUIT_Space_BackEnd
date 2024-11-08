package space.space_spring.domain.userSpace.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.entity.BaseEntity;
import space.space_spring.entity.enumStatus.UserSpaceAuth;

@Entity
@Getter
@Table(name = "User_Space")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSpace extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_space_id")
    private Long userSpaceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id")
    private Space space;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_profile_img")
    @Nullable
    private String userProfileImg;

    @Column(name = "user_profile_msg")
    @Nullable
    private String userProfileMsg;

    // 스페이스의 관리자 vs 일반 멤버
    @Column(name = "user_auth")
    private String userSpaceAuth;

    // 스페이스 선택화면에서의 배치 순서 -> 일단 개발 후순위 (계속 0으로 저장)
    @Column(name = "space_order")
    private int spaceOrder;


    @Builder
    private UserSpace(User user,  Space space, UserSpaceAuth userSpaceAuth) {
        this.user = user;
        this.space = space;
        this.userName = user.getUserName();
        this.userSpaceAuth = userSpaceAuth.getAuth();
    }

    public static UserSpace create(User user, Space space, UserSpaceAuth userSpaceAuth) {
        return UserSpace.builder()
                .user(user)
                .space(space)
                .userSpaceAuth(userSpaceAuth)
                .build();
    }

    public void changeUserName(String userName) {
        this.userName = userName;
    }

    public void changeUserProfileImg(String profileImg) {
        this.userProfileImg = profileImg;
    }

    public void changeUserProfileMsg(String profileMsg) {
        this.userProfileMsg = profileMsg;
    }

    // 유저 권한 변경 기능 아직 개발 X
    public void changeUserSpaceAuth(String auth) {
        this.userSpaceAuth = auth;
    }
}
