package space.space_spring.domain.user.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.entity.BaseEntity;
import space.space_spring.entity.enumStatus.UserSignupType;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "signup_type")
    private String signupType;              // 유저가 회원가입을 진행한 방식 (local, kakao, naver, google 등등)

    public void saveUser(String email, String password, String userName, UserSignupType signupType) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.signupType = signupType.getSignupType();
        initializeBaseEntityFields();
    }

    @Builder
    private User(String email, String password, String userName, UserSignupType signupType) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.signupType = signupType.getSignupType();
    }

    public static User create(String email, String password, String userName, UserSignupType signupType) {
        return User.builder()
                .email(email)
                .password(password)
                .userName(userName)
                .signupType(signupType)
                .build();
    }

}
