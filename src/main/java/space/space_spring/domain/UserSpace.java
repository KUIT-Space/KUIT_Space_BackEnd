package space.space_spring.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;

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

    @Column(name = "user_auth")
    private String userAuth;

    @Column(name = "space_order")
    private int spaceOrder;


}
