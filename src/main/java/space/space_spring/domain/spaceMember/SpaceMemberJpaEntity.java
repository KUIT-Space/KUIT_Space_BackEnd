package space.space_spring.domain.spaceMember;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.space.SpaceJpaEntity;
import space.space_spring.domain.user.UserJpaEntity;
import space.space_spring.global.common.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Space_Member")
public class SpaceMemberJpaEntity extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "space_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_id")
    private SpaceJpaEntity space;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @Column(name = "discord_member_id")
    private Long discordId;         // 디스코드 id 값

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image")
    private String profileImageUrl;

    @Column(name = "is_manager")
    private boolean isManager;

}
