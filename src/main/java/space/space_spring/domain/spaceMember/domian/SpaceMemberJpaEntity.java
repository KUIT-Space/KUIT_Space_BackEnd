package space.space_spring.domain.spaceMember.domian;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.domain.user.User;
import space.space_spring.domain.user.UserJpaEntity;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Space_Member")

public class SpaceMemberJpaEntity extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "space_member_id")
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_id")
    @NotNull
    private SpaceJpaEntity space;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private UserJpaEntity user;

    @Column(name = "discord_member_id")
    @NotNull
    private Long discordId;         // 디스코드 id 값

    @Column(name = "nickname")
    @NotNull
    private String nickname;

    @Column(name = "profile_image")
    @Nullable
    private String profileImageUrl;

    @Column(name = "is_manager")
    @NotNull
    private boolean isManager;

    @Builder
    private SpaceMemberJpaEntity(
            SpaceJpaEntity space, UserJpaEntity user, String nickname,String profileImageUrl,boolean isManager
    ){
        this.space=space;
        this.user = user;
        this.nickname=nickname;
        this.profileImageUrl=profileImageUrl;
        this.isManager=isManager;
    }

    public static SpaceMemberJpaEntity create(
            SpaceJpaEntity space,
            UserJpaEntity user,
            String nickname,
            String profileImageUrl,
            boolean isManager
    ){
        return SpaceMemberJpaEntity.builder()
                .isManager(isManager)
                .space(space)
                .user(user)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }

}
