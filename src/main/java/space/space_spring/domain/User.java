package space.space_spring.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "Users")
@Getter
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

    @Column(name = "jwt")
    @Nullable
    private String jwt;

    public void saveUser(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        initializeBaseEntityFields();
    }

    public void saveJWTtoLoginUser(String jwt) {
        this.jwt = jwt;
    }

}
