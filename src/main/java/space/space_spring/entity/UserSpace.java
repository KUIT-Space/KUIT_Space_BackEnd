package space.space_spring.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import space.space_spring.entity.enumStatus.UserSpaceAuth;

@Entity
@Getter
@Table(name = "User_Space")
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

    public void createUserSpace(User user, Space space, UserSpaceAuth userSpaceAuth) {
        this.user = user;
        this.space = space;
        this.userName = user.getUserName();
        this.userSpaceAuth = userSpaceAuth.getAuth();
    }

}
