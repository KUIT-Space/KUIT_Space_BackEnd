package space.space_spring.domain.spaceMember.domian;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.domain.user.adapter.out.persistence.UserJpaEntity;
import space.space_spring.global.common.entity.BaseJpaEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Space_Member")
public class SpaceMemberJpaEntity extends BaseJpaEntity {

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
            SpaceJpaEntity space, UserJpaEntity user, Long discordId,String nickname,String profileImageUrl,boolean isManager
    ){
        this.space=space;
        this.user = user;
        this.nickname=nickname;
        this.discordId=discordId;
        this.profileImageUrl=profileImageUrl;
        this.isManager=isManager;
    }

    public static SpaceMemberJpaEntity create(
            SpaceJpaEntity space,
            UserJpaEntity user,
            Long discordId,
            String nickname,
            String profileImageUrl,
            boolean isManager
    ){
        return SpaceMemberJpaEntity.builder()
                .isManager(isManager)
                .space(space)
                .user(user)
                .discordId(discordId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public SpaceMemberJpaEntity updateNickName(String nickname){
        this.nickname=nickname;
        return this;

    }
    public SpaceMemberJpaEntity updateProfileImageUrl(String profileImageUrl){
        this.profileImageUrl =profileImageUrl;
        return this;
    }
    public SpaceMemberJpaEntity updateManager(boolean isManager){
        this.isManager=isManager;
        return this;
    }

}
